package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteSubRelation
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.doc.RouteDoc
import kpn.core.util.CoordinateUtil
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.monitor.MonitorUtil
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class MonitorOsmAnalyze(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorRouteStructureLoader: MonitorRouteStructureLoader,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer,
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

        val routeCoordinateArrays = routeRepository.coordinatesArrays(Seq(relation.relationId))
        val lines = routeCoordinateArrays.map(CoordinateUtil.coordinatesToString)
        if (routeCoordinateArrays.isEmpty) {
          // the relation does not exist anymore or does not contain ways, the entire reference becomes a deviation
          val deviation = MonitorRouteDeviation(
            id = 1,
            meters = reference.referenceDistance,
            distance = reference.referenceDistance,
            bounds = reference.referenceBounds,
            lines
          )
          val state = MonitorRouteState(
            _id = ObjectId(),
            routeId = monitorRouteId,
            relationId = relation.relationId,
            timestamp = now,
            deviations = Seq(deviation),
            matchesDistance = 0,
            matchesLines = Seq.empty,
          )
          monitorRouteRepository.saveRouteState(state)
          Some(
            MonitorRouteDeviationAnalysisSummary(
              relation.relationId,
              referenceDistance = reference.referenceDistance,
              deviationDistance = deviation.distance,
              deviationCount = 1,
            )
          )
        }
        else {
          val routeLines = routeCoordinateArrays.map { coordinateArray =>
            geometryFactory.createLineString(coordinateArray)
          }

          val referenceLines = reference.referenceLines.map(CoordinateUtil.coordinatesToLineString)

          val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(routeLines, referenceLines)

          val state = MonitorRouteState(
            ObjectId(),
            monitorRouteId,
            relation.relationId,
            now,
            deviationAnalysis.deviations,
            deviationAnalysis.matchesDistance,
            deviationAnalysis.matchesLines,
          )
          monitorRouteRepository.saveRouteState(state)

          Some(
            MonitorRouteDeviationAnalysisSummary(
              relation.relationId,
              referenceDistance = reference.referenceDistance,
              deviationDistance = deviationAnalysis.deviations.map(_.distance).sum,
              deviationCount = deviationAnalysis.deviations.length,
            )
          )
        }
    }
  }

  private def readReference(args: MonitorUpdateArgs, now: Timestamp, monitorRouteId: ObjectId, relation: MonitorRouteSubRelation): Option[MonitorRouteReference] = {

    log.info(s"${relation.name}")
    monitorRouteRelationRepository.loadTopLevel(Some(args.referenceTimestamp), relation.relationId) match {
      case None =>
        val error = s"Could not load relation ${relation.relationId} at ${args.referenceTimestamp.yyyymmddhhmmss}"
        args.reporter.report( // TODO redesign - should do try/catch at higher level??
          MonitorRouteUpdateStatusMessage(
            errors = Some(Seq(error))
          )
        )

        monitorRouteRepository.deleteRouteReference(monitorRouteId, relation.relationId)
        monitorRouteRepository.deleteRouteState(monitorRouteId, relation.relationId)

        None

      case Some(subRelation) =>
        val wayMembers = MonitorFilter.filterWayMembers(subRelation.wayMembers)
        if (wayMembers.nonEmpty) {
          val bounds = Bounds.from(wayMembers.flatMap(_.way.nodes))
          val analysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)

          val referenceLines = analysis.routeSegments.flatMap(_.lineStrings.map(CoordinateUtil.lineStringToCoordinates))

          val ref = MonitorRouteReference(
            ObjectId(),
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
            referenceLines
          )

          monitorRouteRepository.saveRouteReference(ref)
          Some(ref)
        }
        else {
          None
        }
    }
  }

  private def updateReporterActiveStep(args: MonitorUpdateArgs, routeId: Long): Unit = {
    args.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand(
            "step-active",
            routeId.toString
          )
        )
      )
    )
  }

  private def updateReporterSteps(args: MonitorUpdateArgs, relations: Seq[MonitorRouteSubRelation]): Unit = {
    val subRelationSteps = relations.zipWithIndex.map { case (relation, index) =>
      val desciption = s"${index + 1}/${relations.length} ${relation.name}"
      MonitorRouteUpdateStatusCommand(
        "step-add",
        relation.relationId.toString,
        Some(desciption)
      )
    }

    val saveStep = MonitorRouteUpdateStatusCommand(
      "step-add",
      "save"
    )

    args.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = subRelationSteps :+ saveStep
      )
    )
  }

  private def buildRoute(
    route: MonitorRoute,
    args: MonitorUpdateArgs,
    now: Timestamp,
    routeDoc: RouteDoc,
    distance: Long,
    referenceDistance: Long,
    deviationDistance: Long,
    deviationCount: Long,
    analysisDuration: Long
  ): MonitorRoute = {

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
      relation = None,
      happy = happy
    )
  }
}
