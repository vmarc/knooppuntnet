package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteSubRelation
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.doc.RouteDoc
import kpn.core.util.CoordinateUtil
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.analyzer.engine.monitor.analysis.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.state.MonitorStateStore
import kpn.server.analyzer.engine.tiles.domain.CoordinateCodec
import kpn.server.monitor.MonitorUtil
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.bson.types.ObjectId
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class MonitorOsmAnalyze(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorRouteStructureLoader: MonitorRouteStructureLoader,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer,
  monitorReferenceBuilder: MonitorReferenceBuilder,
  monitorStateStore: MonitorStateStore
) {

  private val log = Log(classOf[MonitorOsmAnalyze])
  val geometryFactory = new GeometryFactory

  def execute(
    route: MonitorRoute,
    now: Timestamp,
    args: MonitorUpdateArgs,
    referenceTimestamp: Timestamp,
    analysisStartMillis: Long
  ): Unit = {

    val monitorRouteRelation = monitorRouteStructureLoader.load(Some(referenceTimestamp), args.relationId).getOrElse(throw new RuntimeException("could not load route structure"))
    val relations = MonitorUtil.subRelationsInRouteRelation(monitorRouteRelation)

    val routeDoc = routeRepository.findRouteById(args.relationId).getOrElse(throw new RuntimeException(s"Could not find RouteDoc with id ${args.relationId}"))

    updateReporterSteps(args, relations)

    val summaries = processRelations(args, now, route._id, relations)
    // TODO redesign - do not forget to add analysis results for relationsIds in RouteDoc that are not in included in the overpass query result

    val referenceDistance = summaries.map(_.referenceDistance).sum
    val referenceBounds = if (summaries.nonEmpty) Some(Bounds.merge(summaries.map(_.referenceBounds))) else None
    val deviationDistance = summaries.map(_.deviationDistance).sum
    val deviationCount = summaries.map(_.deviationCount).sum
    val osmDistance = routeDoc.superDistance
    val analysisDuration = System.currentTimeMillis() - analysisStartMillis

    val updatedRoute = buildRoute(
      route,
      args,
      now,
      routeDoc,
      osmDistance,
      referenceBounds,
      referenceDistance,
      deviationDistance,
      deviationCount,
      analysisDuration
    )

    args.reporter.stepActive("save")
    monitorRouteRepository.saveRoute(updatedRoute)
    args.reporter.stepDone("save")
  }

  private def processRelations(args: MonitorUpdateArgs, now: Timestamp, monitorRouteId: ObjectId, relations: Seq[MonitorRouteSubRelation]): Seq[MonitorRouteDeviationAnalysisSummary] = {
    relations.flatMap { relation =>
      updateReporterActiveStep(args, relation.relationId)
      processRelation(args, now, monitorRouteId, relation)
    }
  }

  private def processRelation(args: MonitorUpdateArgs, now: Timestamp, monitorRouteId: ObjectId, relation: MonitorRouteSubRelation): Option[MonitorRouteDeviationAnalysisSummary] = {
    readReference(args, now, monitorRouteId, relation) match {
      case None => None
      case Some(reference) =>

        val segmentCoordinates = routeRepository.segmentCoordinates(Seq(relation.relationId))

        val routeLines = segmentCoordinates.map { segment =>
          val coordinates = CoordinateCodec.decode(segment.coordinates)
          geometryFactory.createLineString(coordinates)
        }

        if (routeLines.isEmpty) {
          // the relation does not exist anymore or does not contain ways, the entire reference becomes a deviation
          val deviation = MonitorRouteDeviation(
            id = 1,
            meters = reference.referenceDistance,
            distance = reference.referenceDistance,
            bounds = reference.referenceBounds,
            segmentCoordinates.map(_.coordinates)
          )
          val state = MonitorState(
            _id = ObjectId.get(),
            routeId = monitorRouteId,
            relationId = relation.relationId,
            timestamp = now,
            deviations = Seq(deviation),
            matchesDistance = 0,
            matchesLines = Seq.empty,
            segments = Seq.empty,
          )

          monitorStateStore.saveState(state)
          Some(
            MonitorRouteDeviationAnalysisSummary(
              relation.relationId,
              referenceBounds = reference.referenceBounds,
              referenceDistance = reference.referenceDistance,
              deviationDistance = deviation.distance,
              deviationCount = 1,
            )
          )
        }
        else {
          val referenceLines = reference.referenceLines.map(CoordinateUtil.coordinatesToLineString)

          val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(routeLines, referenceLines)

          val state = MonitorState(
            ObjectId.get(),
            monitorRouteId,
            relation.relationId,
            now,
            deviationAnalysis.deviations,
            deviationAnalysis.matchesDistance,
            deviationAnalysis.matchesLines,
            segmentCoordinates
          )

          monitorStateStore.saveState(state)

          Some(
            MonitorRouteDeviationAnalysisSummary(
              relation.relationId,
              referenceBounds = reference.referenceBounds,
              referenceDistance = reference.referenceDistance,
              deviationDistance = deviationAnalysis.deviations.map(_.distance).sum,
              deviationCount = deviationAnalysis.deviations.length,
            )
          )
        }
    }
  }

  private def readReference(args: MonitorUpdateArgs, now: Timestamp, monitorRouteId: ObjectId, relation: MonitorRouteSubRelation): Option[MonitorReference] = {

    log.info(s"${relation.name}")
    monitorRouteRelationRepository.loadTopLevel(Some(args.referenceTimestamp), relation.relationId) match {
      case None =>
        val error = s"Could not load relation ${relation.relationId} at ${args.referenceTimestamp.yyyymmddhhmmss}"
        args.reporter.report( // TODO redesign - should do try/catch at higher level??
          MonitorMessage(
            errors = Some(Seq(error))
          )
        )

        monitorRouteRepository.deleteReference(monitorRouteId, relation.relationId)
        monitorRouteRepository.deleteState(monitorRouteId, relation.relationId)

        None

      case Some(subRelation) =>
        val wayMembers = MonitorFilter.filterWayMembers(subRelation.wayMembers)
        if (wayMembers.nonEmpty) {
          val bounds = Bounds.from(wayMembers.flatMap(_.wayNodes))
          val analysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)

          val referenceLines = analysis.routeSegments.flatMap(_.lineStrings.map(CoordinateUtil.lineStringToCoordinates))

          val ref = monitorReferenceBuilder.build(
            MonitorReference(
              ObjectId.get(),
              monitorRouteId,
              Some(subRelation.id),
              Time.now,
              args.user,
              bounds,
              MonitorReferenceType.osm,
              args.referenceTimestamp,
              analysis.osmDistance,
              analysis.routeSegments.size,
              None,
              referenceLines,
              Seq.empty,
            )
          )

          monitorRouteRepository.saveReference(ref)
          Some(ref)
        }
        else {
          None
        }
    }
  }

  private def updateReporterActiveStep(args: MonitorUpdateArgs, routeId: Long): Unit = {
    args.reporter.report(
      MonitorMessage(
        MonitorCommand.active(routeId.toString)
      )
    )
  }

  private def updateReporterSteps(args: MonitorUpdateArgs, relations: Seq[MonitorRouteSubRelation]): Unit = {
    val subRelationSteps = relations.zipWithIndex.map { case (relation, index) =>
      val desciption = s"${index + 1}/${relations.length} ${relation.name}"
      MonitorCommand.add(relation.relationId.toString, Some(desciption))
    }

    val saveStep = MonitorCommand.add("save")

    args.reporter.report(
      MonitorMessage(subRelationSteps :+ saveStep)
    )
  }

  private def buildRoute(
    route: MonitorRoute,
    args: MonitorUpdateArgs,
    now: Timestamp,
    routeDoc: RouteDoc,
    distance: Long,
    referenceBounds: Option[Bounds],
    referenceDistance: Long,
    deviationDistance: Long,
    deviationCount: Long,
    analysisDuration: Long
  ): MonitorRoute = {

    val bounds = {
      val allBounds = routeDoc.bounds.toSeq ++ referenceBounds.toSeq
      if (allBounds.isEmpty) {
        None
      }
      else {
        Some(Bounds.merge(allBounds))
      }
    }
    val happy = distance > 0 && deviationDistance == 0 && referenceDistance > 0

    route.copy(
      timestamp = now,
      analysisTimestamp = Some(now),
      analysisDuration = Some(analysisDuration),
      referenceDistance = referenceDistance,
      deviationDistance = deviationDistance,
      deviationCount = deviationCount,
      osmSegmentCount = routeDoc.superSegments.size,
      osmDistance = distance,
      relationIds = routeDoc.routeIds,
      bounds = bounds,
      happy = happy
    )
  }
}
