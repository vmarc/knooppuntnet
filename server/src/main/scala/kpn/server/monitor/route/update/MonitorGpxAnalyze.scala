package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.custom.Timestamp
import kpn.core.tools.monitor.MonitorRouteGpxReader
import kpn.core.util.CoordinateUtil
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.core.util.Util.mergeBounds
import kpn.server.analyzer.engine.monitor.analysis.MonitorReferenceUtil
import kpn.server.analyzer.engine.monitor.analysis.MonitorRouteAnalysisSupport
import kpn.server.analyzer.engine.monitor.analysis.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.state.MonitorStateStore
import kpn.server.analyzer.engine.tiles.domain.CoordinateCodec
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.bson.types.ObjectId
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

import scala.xml.XML

@Component
class MonitorGpxAnalyze(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorOsmAnalyze: MonitorOsmAnalyze,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer,
  monitorReferenceBuilder: MonitorReferenceBuilder,
  monitorStateStore: MonitorStateStore,
) {

  private val log = Log(classOf[MonitorGpxAnalyze])
  val geometryFactory = new GeometryFactory

  def execute(args: MonitorUpdateArgs, route: MonitorRoute, now: Timestamp): Unit = {

    args.reporter.stepActive("analyze-route-structure")

    //    if (args.update.relationId.isEmpty) {
    //
    //      args.reporter.stepActive("save")
    //      monitorRouteRepository.saveRoute(route)
    //      args.reporter.stepDone("save")
    //
    //      return
    //    }

    val xml = XML.loadString(args.referenceGpx)
    val geometryCollection = new MonitorRouteGpxReader().read(xml)
    val referenceBounds = MonitorRouteAnalysisSupport.geometryBounds(geometryCollection)

    val referenceLineStrings = MonitorReferenceUtil.toLineStrings(geometryCollection)
    val referenceDistance = Math.round(referenceLineStrings.map(Haversine.meters).sum)
    val referenceSegmentCount = geometryCollection.getNumGeometries
    val referenceLines = referenceLineStrings.map(CoordinateUtil.lineStringToCoordinates)

    monitorRouteRepository.saveReference(
      monitorReferenceBuilder.build(
        MonitorReference(
          ObjectId.get(),
          routeId = route._id,
          relationId = args.update.relationId,
          timestamp = now,
          user = args.user,
          referenceBounds = referenceBounds,
          referenceType = MonitorReferenceType.gpx,
          referenceTimestamp = args.referenceTimestamp,
          referenceDistance = referenceDistance,
          referenceSegmentCount = referenceSegmentCount,
          referenceFilename = args.update.referenceFilename,
          referenceLines = referenceLines,
          Seq.empty
        )
      )
    )

    args.update.relationId match {
      case Some(relationId) =>

        val routeDoc = routeRepository.findRouteById(args.relationId).getOrElse(throw new RuntimeException(s"Could not find RouteDoc with id ${args.relationId}"))

        val segmentCoordinates = routeRepository.segmentCoordinates(routeDoc.routeIds)

        val routeLines = segmentCoordinates.map { segment =>
          val coordinates = CoordinateCodec.decode(segment.coordinates)
          geometryFactory.createLineString(coordinates)
        }

        val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(routeLines, referenceLineStrings)

        monitorStateStore.saveState(
          MonitorState(
            ObjectId.get(),
            route._id,
            args.relationId,
            now,
            deviationAnalysis.deviations,
            deviationAnalysis.matchesDistance,
            deviationAnalysis.matchesLines,
            segmentCoordinates,
          )
        )

        val bounds = mergeBounds(routeDoc.bounds.toSeq :+ referenceBounds)

        val happy = deviationAnalysis.deviations.isEmpty && routeDoc.superDistance == referenceDistance && routeDoc.superDistance > 0

        val updatedRoute = route.copy(
          analysisTimestamp = Some(now),
          referenceDistance = referenceDistance,
          deviationDistance = deviationAnalysis.deviations.map(_.meters).sum,
          deviationCount = deviationAnalysis.deviations.length,
          osmSegmentCount = routeDoc.superSegments.size,
          osmDistance = routeDoc.superDistance,
          relationIds = routeDoc.routeIds,
          bounds = Some(bounds),
          happy = happy,
        )

        //    updateReporterSteps(args, relations)
        //
        //    val summaries = processRelations(args, now, route._id, relations)
        //    // TODO redesign - do not forget to add analysis results for relationsIds in RouteDoc that are not in included in the overpass query result
        //
        //    //    val referenceDistance = summaries.map(_.referenceDistance).sum
        //    val deviationDistance = summaries.map(_.deviationDistance).sum
        //    val deviationCount = summaries.map(_.deviationCount).sum
        //
        //    val osmDistance = routeDoc.superDistance
        //
        //    val analysisDuration = System.currentTimeMillis() - analysisStartMillis
        //
        //    val updatedRoute = buildRoute(
        //      route,
        //      args,
        //      now,
        //      routeDoc,
        //      osmDistance,
        //      referenceDistance,
        //      deviationDistance,
        //      deviationCount,
        //      analysisDuration
        //    )

        args.reporter.stepActive("save")
        monitorRouteRepository.saveRoute(updatedRoute)
        args.reporter.stepDone("save")

      case None =>

        val updatedRoute = route.copy(
          analysisTimestamp = Some(now),
          referenceDistance = referenceDistance,
          deviationDistance = 0,
          deviationCount = 0,
          osmSegmentCount = 0,
          osmDistance = 0,
          bounds = Some(referenceBounds),
          happy = false,
        )

        args.reporter.stepActive("save")
        monitorRouteRepository.saveRoute(updatedRoute)
        args.reporter.stepDone("save")
    }
  }
}
