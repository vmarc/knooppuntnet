package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.api.custom.Timestamp
import kpn.core.util.CoordinateUtil
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class MonitorGpxUpdate(
  routeRepository: RouteRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateStructure: MonitorUpdateStructure,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorUpdateAnalyzeReference: MonitorUpdateAnalyzeReference,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorUpdateSave: MonitorUpdateSave,
  monitorGpxAnalyze: MonitorGpxAnalyze,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer
) {

  private val log = Log(classOf[MonitorGpxUpdate])
  private val geometryFactory = new GeometryFactory()

  def initialMessage: MonitorRouteUpdateStatusMessage = {
    MonitorRouteUpdateStatusMessage(
      commands = Seq(
        MonitorRouteUpdateStatusCommand("step-add", "prepare"),
        MonitorRouteUpdateStatusCommand("step-active", "prepare"),
      )
    )
  }

  def execute(args: MonitorUpdateArgs, route: MonitorRoute, updatedRoute: MonitorRoute, now: Timestamp): Unit = {

    if (route.relationId.isEmpty && args.update.relationId.nonEmpty && args.update.referenceGpx.isEmpty) {
      // the relationId that was previously unknown is now filled in, and no new reference is given

      monitorRouteRepository.routeReference(route._id, None) match {
        case None => throw new RuntimeException(s"Could not find reference for route ${route._id}")
        case Some(reference) =>

          val updatedReference = reference.copy(
            relationId = Some(args.relationId),
          )
          monitorRouteRepository.saveRouteReference(updatedReference)

          val referenceLines = reference.referenceLines.map(CoordinateUtil.coordinatesToLineString)

          val routeDoc = routeRepository.findRouteById(args.relationId).getOrElse(throw new RuntimeException(s"Could not find RouteDoc with id ${args.relationId}"))

          val routeCoordinateArrays = routeRepository.coordinatesArrays(routeDoc.routeIds)

          val routeLines = routeCoordinateArrays.map { coordinateArray =>
            geometryFactory.createLineString(coordinateArray)
          }

          val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(routeLines, referenceLines)

          val state = MonitorRouteState(
            ObjectId(),
            route._id,
            args.relationId,
            now,
            deviationAnalysis.deviations,
            deviationAnalysis.matchesDistance,
            deviationAnalysis.matchesLines,
          )
          monitorRouteRepository.saveRouteState(state)

          val happy = deviationAnalysis.deviations.isEmpty && routeDoc.superDistance == reference.referenceDistance && routeDoc.superDistance > 0

          val updatedRoute2 = updatedRoute.copy(
            analysisTimestamp = Some(now),
            referenceDistance = reference.referenceDistance,
            deviationDistance = deviationAnalysis.deviations.map(_.meters).sum,
            deviationCount = deviationAnalysis.deviations.length,
            osmSegmentCount = routeDoc.superSegments.size,
            osmDistance = routeDoc.superDistance,
            relation = None,
            happy = happy,
          )

          args.reporter.stepActive("save")
          monitorRouteRepository.saveRoute(updatedRoute2)
          args.reporter.stepDone("save")
      }
    }


    //    context.set(monitorUpdateStructure.update(context.value))

    //    val oldReferences = monitorRouteRepository.routeReferences(route._id)
    //    val oldReferenceIds = monitorRouteRepository.routeReferenceIds(route._id)
    //    val oldStateIds = monitorRouteRepository.routeStateIds(route._id)

    //    context.set(
    //      context.value.copy(
    //        oldReferenceIds = oldReferenceIds,
    //        oldStateIds = oldStateIds,
    //        references = oldReferences,
    //      )
    //    )

    //    context.set(monitorUpdateCommon.removeObsoleteReferences(context))
    //    monitorUpdateCommon.removeObsoleteStates(context)

    if (route.referenceType == MonitorReferenceType.osm && args.update.referenceType == MonitorReferenceType.osm) {
      if (route.referenceTimestamp != args.update.referenceTimestamp || route.relationId != args.update.relationId) {
        // perform reference update and reanalyze deviations
        //        updateSubRelationOsmReferences(context)

        if (args.update.relationId.isEmpty) {

          args.reporter.report(
            MonitorRouteUpdateStatusMessage(
              commands = Seq(
                MonitorRouteUpdateStatusCommand("step-add", "save"),
                MonitorRouteUpdateStatusCommand("step-active", "save"),
              )
            )
          )

          monitorRouteRepository.deleteRouteReferences(route._id)
          monitorRouteRepository.deleteRouteStates(route._id)

          val cleanedUpRoute = updatedRoute.copy(
            analysisTimestamp = None,
            symbol = None,
            referenceDistance = 0,
            deviationDistance = 0,
            deviationCount = 0,
            osmSegmentCount = 0,
            osmDistance = 0,
            relation = None,
            happy = false,
          )
          monitorRouteRepository.saveRoute(cleanedUpRoute)

          args.reporter.stepDone("save")
        }
        else {
          args.reporter.report(
            MonitorRouteUpdateStatusMessage(
              commands = Seq(
                MonitorRouteUpdateStatusCommand("step-add", "analyze-route-structure"),
                MonitorRouteUpdateStatusCommand("step-active", "analyze-route-structure"),
              )
            )
          )

          if (route.relationId != args.update.relationId) {
            monitorRouteRepository.deleteRouteReferences(route._id)
            monitorRouteRepository.deleteRouteStates(route._id)
          }

          //          monitorOsmAnalyze.execute(
          //            updatedRoute,
          //            now,
          //            args,
          //            analysisStartMillis
          //          )
        }
      }
      else {
        // nothing to do, but saving updatedRoute?
        args.reporter.report(
          MonitorRouteUpdateStatusMessage(
            commands = Seq(
              MonitorRouteUpdateStatusCommand("step-add", "save"),
              MonitorRouteUpdateStatusCommand("step-active", "save"),
            )
          )
        )
        monitorRouteRepository.saveRoute(updatedRoute)
        args.reporter.stepDone("save")
      }
    }
    else {
      //throw new IllegalStateException(s"reference type change from ${route.referenceType} to ${args.update.referenceType} not implemented yet")
      if (args.update.referenceGpx.nonEmpty) {
        monitorGpxAnalyze.execute(args, updatedRoute, now)
      }
    }
  }
}
