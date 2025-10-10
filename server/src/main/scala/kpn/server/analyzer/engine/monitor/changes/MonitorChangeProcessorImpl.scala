package kpn.server.analyzer.engine.monitor.changes

import kpn.api.common.Bounds
import kpn.api.common.LatLonImpl
import kpn.api.common.Relation
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerHelper
import kpn.server.analyzer.engine.context.ElementIdMap
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteAnalysis
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteSegmentData
import kpn.server.analyzer.engine.monitor.state.MonitorStateStore
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRouteChange
import kpn.server.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.repository.MonitorRouteRepository
import org.bson.types.ObjectId
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class MonitorChangeProcessorImpl(
  monitorRouteRepository: MonitorRouteRepository,
  monitorRouteLoader: MonitorRouteLoader,
  monitorChangeImpactAnalyzer: MonitorChangeImpactAnalyzer,
  monitorStateStore: MonitorStateStore
) extends MonitorChangeProcessor {

  private val log = Log(classOf[MonitorChangeProcessorImpl])
  private val elementIdMap = new ElementIdMap()
  private val geometryFactory = new GeometryFactory
  private val sampleDistanceMeters = 10
  private val toleranceMeters = 10

  // TODO call from AnalyzerEngineImpl.load
  override def load(timestamp: Timestamp): Unit = {
    Log.context(timestamp.yyyymmddhhmmss) {
      Log.context("load") {
        log.info("Start loading monitor routes")
        log.infoElapsed {
          monitorRouteRepository.allRouteIds.foreach { routeId =>
            monitorRouteLoader.loadInitial(timestamp, routeId) match {
              case Some(routeRelation) =>
                val elementIds = RelationAnalyzerHelper.toElementIds(routeRelation)
                elementIdMap.add(routeId, elementIds)
              case None =>
                log.warn(s"Could not load route $routeId")
            }
          }
          (s"Loaded monitor routes", ())
        }
      }
    }
  }

  override def process(changeSetContext: ChangeSetContext): Unit = {
    elementIdMap.foreach { (routeId, elementIds) =>
      if (monitorChangeImpactAnalyzer.hasImpact(changeSetContext.changeSet, routeId, elementIds)) {
        Log.context(routeId.toString) {
          log.infoElapsed {
            processRoute(changeSetContext, routeId)
            ("process route", ())
          }
        }
      }
    }
  }

  private def processRoute(changeSetContext: ChangeSetContext, routeId: Long): Unit = {
    monitorRouteRepository.routeReferenceKey(s"TODO KEY$routeId") match {
      case None => log.warn(s"$routeId TODO routeReferenceKey not available ")
      case Some(referenceKey) =>

        val referenceOption = monitorRouteRepository.reference(new ObjectId("TODO MON") /*, routeId, referenceKey*/ , None)
        monitorRouteLoader.loadBefore(changeSetContext.changeSet.id, changeSetContext.changeSet.timestampBefore, routeId) match {
          case None => log.warn(s"$routeId TODO route did not exist before --> create change ???")
          case Some(beforeRelation) =>

            monitorRouteLoader.loadAfter(changeSetContext.changeSet.id, changeSetContext.changeSet.timestampAfter, routeId) match {
              case None => log.warn(s"$routeId TODO route did not exist anymore after --> delete change ???")
              case Some(afterRelation) =>

                referenceOption match {
                  case None => log.warn(s"$routeId TODO geen reference --> alleen andere changes loggen ???")
                  case Some(reference) =>
                    log.infoElapsed {
                      analyze(
                        changeSetContext,
                        routeId,
                        beforeRelation,
                        afterRelation,
                        reference
                      )
                      ("analyze", ())
                    }
                }
            }
        }
    }
  }

  private def analyze(
    context: ChangeSetContext,
    routeId: Long,
    beforeRelation: Relation,
    afterRelation: Relation,
    reference: MonitorReference
  ): Unit = {

    val beforeRouteSegments = log.infoElapsed {
      ("toRouteSegments before", Seq.empty /* MonitorRouteAnalysisSupport.toRouteSegments(beforeRelation) */ )
    }
    val beforeRouteAnalysis = log.infoElapsed {
      ("analyze change before", analyzeChange(reference, beforeRelation, beforeRouteSegments))
    }

    val afterRouteSegments = log.infoElapsed {
      ("toRouteSegments after", Seq.empty /* MonitorRouteAnalysisSupport.toRouteSegments(afterRelation) */ )
    }
    val afterRouteAnalysis = log.infoElapsed {
      ("analyze change after", analyzeChange(reference, afterRelation, afterRouteSegments))
    }

    val wayIdsBefore = beforeRelation.wayMembers.map(_.memberId).toSet
    val wayIdsAfter = afterRelation.wayMembers.map(_.memberId).toSet

    val wayIdsAdded = (wayIdsAfter -- wayIdsBefore).size
    val wayIdsRemoved = (wayIdsBefore -- wayIdsAfter).size

    val wayIdsUpdated = wayIdsAfter.intersect(wayIdsBefore).count { wayId =>
      val wayBefore = beforeRelation.wayMembers.filter(_.memberId == wayId).head.way.get
      val wayAfter = afterRelation.wayMembers.filter(_.memberId == wayId).head.way.get
      val latLonsBefore = wayBefore.nodes.map(node => LatLonImpl(node.latitude, node.longitude))
      val latLonsAfter = wayAfter.nodes.map(node => LatLonImpl(node.latitude, node.longitude))
      !latLonsBefore.equals(latLonsAfter)
    }

    if ((wayIdsAdded + wayIdsRemoved + wayIdsUpdated) == 0) {
      log.info("No geometry changes, no further analysis")
    }
    else {
      val beforeDeviations = beforeRouteAnalysis.deviations
      val afterDeviations = afterRouteAnalysis.deviations

      val newDeviations = afterRouteAnalysis.deviations.filterNot(deviation => beforeDeviations.exists(_.sameAs(deviation)))
      val resolvedDeviations = beforeRouteAnalysis.deviations.filterNot(deviation => afterDeviations.exists(_.sameAs(deviation)))

      val message = s"ways=${afterRouteAnalysis.wayCount} $wayIdsAdded/$wayIdsRemoved/$wayIdsUpdated," ++
        s" osm=${afterRouteAnalysis.osmDistance}," ++
        s" gpx=${afterRouteAnalysis.gpxDistance}," ++
        s" osmSegments=${afterRouteAnalysis.osmSegments.size}," ++
        s" nokSegments=${afterRouteAnalysis.deviations.size}," ++
        s" new=${newDeviations.size}," ++
        s" resolved=${resolvedDeviations.size}"

      val key = context.buildChangeKey(routeId)

      val routeSegments = if (newDeviations.nonEmpty || resolvedDeviations.nonEmpty) {
        afterRouteAnalysis.osmSegments
      }
      else {
        Seq.empty
      }

      val change = MonitorRouteChange(
        ObjectId.get(),
        new ObjectId("TODO"), // key.toId,
        key,
        afterRouteAnalysis.wayCount,
        wayIdsAdded,
        wayIdsRemoved,
        wayIdsUpdated,
        afterRouteAnalysis.osmDistance,
        afterRouteAnalysis.osmSegments.size,
        afterRouteAnalysis.deviations.size,
        resolvedDeviations.size,
        happy = resolvedDeviations.nonEmpty,
        investigate = newDeviations.nonEmpty
      )

      monitorRouteRepository.saveRouteChange(change)

      val routeChangeGeometry = MonitorRouteChangeGeometry(
        _id = ObjectId.get(),
        routeId = new ObjectId("TODO"), // key.toId,
        key = key,
        routeSegments = routeSegments,
        newDeviations = newDeviations,
        resolvedDeviations = resolvedDeviations,
      )
      monitorRouteRepository.saveRouteChangeGeometry(routeChangeGeometry)

      val happy = false

      monitorStateStore.saveState(
        MonitorState(
          ObjectId.get(),
          null, // TODO routeId,
          1L, // TODO relationId
          afterRouteAnalysis.relation.timestamp,
          afterRouteAnalysis.deviations,
          afterRouteAnalysis.matchesDistance,
          afterRouteAnalysis.matchesLines,
          Seq.empty,
        )
      )

      log.info(message)
    }
  }

  private def analyzeChange(reference: MonitorReference, routeRelation: Relation, osmRouteSegments: Seq[MonitorRouteSegmentData]): MonitorRouteAnalysis = {
    MonitorRouteAnalysis(
      relation = routeRelation,
      wayCount = routeRelation.wayMembers.size,
      startNodeId = None, // TODO
      endNodeId = None, // TODO
      osmDistance = 0,
      gpxDistance = 0,
      bounds = Bounds(),
      osmSegments = osmRouteSegments.map(_.segment),
      gpxGeometry = None,
      matchesDistance = 0L,
      matchesLines = Seq.empty,
      deviations = Seq.empty,
      relations = Seq.empty
    )
  }
}
