package kpn.server.analyzer.engine.monitor.changes

import kpn.api.base.ObjectId
import kpn.api.common.LatLonImpl
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.core.util.Util
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerHelper
import kpn.server.analyzer.engine.context.ElementIdMap
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport.toMeters
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteAnalysis
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteSegmentData
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import kpn.server.repository.MonitorRouteRepository
import org.locationtech.jts.densify.Densifier
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.springframework.stereotype.Component

@Component
class MonitorChangeProcessorImpl(
  monitorRouteRepository: MonitorRouteRepository,
  monitorRouteLoader: MonitorRouteLoader,
  monitorChangeImpactAnalyzer: MonitorChangeImpactAnalyzer
) extends MonitorChangeProcessor {

  private val log = Log(classOf[MonitorChangeProcessorImpl])
  private val elementIdMap = ElementIdMap()
  private val geomFactory = new GeometryFactory
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
    monitorRouteRepository.routeReferenceKey("TODO KEY" + routeId) match {
      case None => log.warn(s"$routeId TODO routeReferenceKey not available ")
      case Some(referenceKey) =>

        val referenceOption = monitorRouteRepository.routeReference(ObjectId("TODO MON") /*, routeId, referenceKey*/)
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
    reference: MonitorRouteReference
  ): Unit = {

    val beforeRouteSegments = log.infoElapsed {
      ("toRouteSegments before", MonitorRouteAnalysisSupport.toRouteSegments(beforeRelation))
    }
    val beforeRoute = log.infoElapsed {
      ("analyze change before", analyzeChange(reference, beforeRelation, beforeRouteSegments))
    }

    val afterRouteSegments = log.infoElapsed {
      ("toRouteSegments after", MonitorRouteAnalysisSupport.toRouteSegments(afterRelation))
    }
    val afterRoute = log.infoElapsed {
      ("analyze change after", analyzeChange(reference, afterRelation, afterRouteSegments))
    }

    val wayIdsBefore = beforeRelation.wayMembers.map(_.way.id).toSet
    val wayIdsAfter = afterRelation.wayMembers.map(_.way.id).toSet

    val wayIdsAdded = (wayIdsAfter -- wayIdsBefore).size
    val wayIdsRemoved = (wayIdsBefore -- wayIdsAfter).size

    val wayIdsUpdated = wayIdsAfter.intersect(wayIdsBefore).count { wayId =>
      val wayBefore = beforeRelation.wayMembers.filter(_.way.id == wayId).head.way
      val wayAfter = afterRelation.wayMembers.filter(_.way.id == wayId).head.way
      val latLonsBefore = wayBefore.nodes.map(node => LatLonImpl(node.latitude, node.longitude))
      val latLonsAfter = wayAfter.nodes.map(node => LatLonImpl(node.latitude, node.longitude))
      !latLonsBefore.equals(latLonsAfter)
    }

    if ((wayIdsAdded + wayIdsRemoved + wayIdsUpdated) == 0) {
      log.info("No geometry changes, no further analysis")
    }
    else {
      val beforeGeoJons = beforeRoute.deviations.map(_.geoJson)
      val afterGeoJons = afterRoute.deviations.map(_.geoJson)

      val newSegments = afterRoute.deviations.filterNot(nokSegment => beforeGeoJons.contains(nokSegment.geoJson))
      val resolvedSegments = beforeRoute.deviations.filterNot(nokSegment => afterGeoJons.contains(nokSegment.geoJson))

      val message = s"ways=${afterRoute.wayCount} $wayIdsAdded/$wayIdsRemoved/$wayIdsUpdated," ++
        s" osm=${afterRoute.osmDistance}," ++
        s" gpx=${afterRoute.gpxDistance}," ++
        s" osmSegments=${afterRoute.osmSegments.size}," ++
        s" nokSegments=${afterRoute.deviations.size}," ++
        s" new=${newSegments.size}," ++
        s" resolved=${resolvedSegments.size}"

      val key = context.buildChangeKey(routeId)

      val routeSegments = if (newSegments.nonEmpty || resolvedSegments.nonEmpty) {
        afterRoute.osmSegments
      }
      else {
        Seq.empty
      }

      val change = MonitorRouteChange(
        ObjectId(),
        ObjectId("TODO"), // key.toId,
        key,
        afterRoute.wayCount,
        wayIdsAdded,
        wayIdsRemoved,
        wayIdsUpdated,
        afterRoute.osmDistance,
        afterRoute.osmSegments.size,
        afterRoute.deviations.size,
        resolvedSegments.size,
        happy = resolvedSegments.nonEmpty,
        investigate = newSegments.nonEmpty
      )

      monitorRouteRepository.saveRouteChange(change)

      val routeChangeGeometry = MonitorRouteChangeGeometry(
        ObjectId(),
        ObjectId("TODO"), // key.toId,
        key,
        routeSegments,
        newSegments,
        resolvedSegments,
      )
      monitorRouteRepository.saveRouteChangeGeometry(routeChangeGeometry)

      val happy = false
      val routeState = MonitorRouteState(
        ObjectId(),
        null, // routeId,
        afterRoute.relation.timestamp,
        afterRoute.wayCount,
        afterRoute.osmDistance,
        afterRoute.gpxDistance,
        afterRoute.bounds,
        Some(reference._id),
        afterRoute.osmSegments,
        afterRoute.matchesGeometry,
        afterRoute.deviations,
        happy,
        osmSuperSegments = Seq.empty // TODO add osm super segments
      )

      monitorRouteRepository.saveRouteState(routeState)

      log.info(message)
    }
  }

  private def analyzeChange(reference: MonitorRouteReference, routeRelation: Relation, osmRouteSegments: Seq[MonitorRouteSegmentData]): MonitorRouteAnalysis = {

    val gpxLineString = new GeoJsonReader().read(reference.geometry)

    val (okOption: Option[MultiLineString], deviations: Seq[MonitorRouteDeviation]) = {

      val distanceBetweenSamples = sampleDistanceMeters.toDouble * gpxLineString.getLength / MonitorRouteAnalysisSupport.toMeters(gpxLineString.getLength)
      val densifiedGpx = Densifier.densify(gpxLineString, distanceBetweenSamples)
      val sampleCoordinates = densifiedGpx.getCoordinates.toSeq

      val distances = sampleCoordinates.toList.map { coordinate =>
        val point = geomFactory.createPoint(coordinate)
        toMeters(osmRouteSegments.map(segment => segment.lineString.distance(point)).min)
      }

      val withinTolerance = distances.map(distance => distance < toleranceMeters)
      val okAndIndexes = withinTolerance.zipWithIndex.map { case (ok, index) => ok -> index }
      val splittedOkAndIndexes = MonitorRouteAnalysisSupport.split(okAndIndexes)

      val ok: MultiLineString = MonitorRouteAnalysisSupport.toMultiLineString(sampleCoordinates, splittedOkAndIndexes.filter(_.head._1))

      val noks = splittedOkAndIndexes.filterNot(_.head._1)

      val nok = noks.zipWithIndex.map { case (segment, segmentIndex) =>
        val segmentIndexes = segment.map(_._2)
        val maxDistance = distances.zipWithIndex.filter { case (distance, index) =>
          segmentIndexes.contains(index)
        }.map { case (distance, index) =>
          distance
        }.max

        val lineString = MonitorRouteAnalysisSupport.toLineString(sampleCoordinates, segment)
        val meters: Long = Math.round(toMeters(lineString.getLength))
        val bounds = MonitorRouteAnalysisSupport.toBounds(lineString.getCoordinates.toSeq)
        val geoJson = MonitorRouteAnalysisSupport.toGeoJson(lineString)

        MonitorRouteDeviation(
          segmentIndex + 1,
          meters,
          maxDistance.toLong,
          bounds,
          geoJson
        )
      }

      val xx: Seq[MonitorRouteDeviation] = nok.sortBy(_.distance).reverse.zipWithIndex.map { case (s, index) =>
        s.copy(id = index + 1)
      }

      (Some(ok), xx)
    }

    val gpxDistance = Math.round(toMeters(gpxLineString.getLength / 1000))
    val osmDistance = Math.round(osmRouteSegments.map(_.segment.meters).sum.toDouble / 1000)

    val gpxGeometry = MonitorRouteAnalysisSupport.toGeoJson(gpxLineString)
    val okGeometry = okOption.map(geometry => MonitorRouteAnalysisSupport.toGeoJson(geometry))

    // TODO merge gpx bounds + ok
    val bounds = Util.mergeBounds(osmRouteSegments.map(_.segment.bounds) ++ deviations.map(_.bounds))

    MonitorRouteAnalysis(
      routeRelation,
      routeRelation.wayMembers.size,
      osmDistance,
      gpxDistance,
      bounds,
      osmRouteSegments.map(_.segment),
      Some(gpxGeometry),
      okGeometry,
      deviations,
      relations = Seq.empty // TODO add analysis result per sub-relation where applicable
    )
  }

}
