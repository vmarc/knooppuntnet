package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.api.custom.Timestamp
import kpn.core.util.CoordinateUtil
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.analysis.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.state.MonitorStateStore
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.json.Json
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class MonitorGpxUpdate(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorGpxAnalyze: MonitorGpxAnalyze,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer,
  monitorReferenceBuilder: MonitorReferenceBuilder,
  monitorStateStore: MonitorStateStore
) {

  private val log = Log(classOf[MonitorGpxUpdate])
  private val geometryFactory = new GeometryFactory()

  def initialMessage: MonitorMessage = {
    MonitorMessage(
      MonitorCommand.add("prepare"),
      MonitorCommand.active("prepare"),
    )
  }

  def execute(args: MonitorUpdateArgs, route: MonitorRoute, updatedRoute: MonitorRoute, now: Timestamp): Unit = {

    if (route.relationId.isEmpty && args.update.relationId.nonEmpty && args.update.referenceGpx.isEmpty) {
      // the relationId that was previously unknown is now filled in, and no new reference is given

      monitorRouteRepository.reference(route._id, None) match {
        case None => throw new RuntimeException(s"Could not find reference for route ${route._id}")
        case Some(reference) =>

          monitorRouteRepository.saveReference(
            monitorReferenceBuilder.build(
              reference.copy(
                relationId = Some(args.relationId),
              )
            )
          )

          val referenceLines = reference.referenceLines.map(CoordinateUtil.coordinatesToLineString)

          val routeDoc = routeRepository.findRouteById(args.relationId).getOrElse(throw new RuntimeException(s"Could not find RouteDoc with id ${args.relationId}"))

          val segmentCoordinates = routeRepository.segmentCoordinates(routeDoc.routeIds)

          val routeLines = segmentCoordinates.map { segment =>
            val coordinates = Json.value(segment.coordinates, classOf[CoordinateArray]).coordinates
            geometryFactory.createLineString(coordinates)
          }

          val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(routeLines, referenceLines)

          monitorStateStore.saveState(
            MonitorState(
              ObjectId(),
              route._id,
              args.relationId,
              now,
              deviationAnalysis.deviations,
              deviationAnalysis.matchesDistance,
              deviationAnalysis.matchesLines,
              segmentCoordinates
            )
          )

          val happy = deviationAnalysis.deviations.isEmpty && routeDoc.superDistance == reference.referenceDistance && routeDoc.superDistance > 0

          val updatedRoute2 = updatedRoute.copy(
            analysisTimestamp = Some(now),
            referenceDistance = reference.referenceDistance,
            deviationDistance = deviationAnalysis.deviations.map(_.meters).sum,
            deviationCount = deviationAnalysis.deviations.length,
            osmSegmentCount = routeDoc.superSegments.size,
            osmDistance = routeDoc.superDistance,
            happy = happy,
          )

          args.reporter.stepActive("save")
          monitorRouteRepository.saveRoute(updatedRoute2)
          args.reporter.stepDone("save")
      }
    }

    if (args.update.referenceGpx.nonEmpty) {
      monitorGpxAnalyze.execute(args, updatedRoute, now)
    }
  }
}
