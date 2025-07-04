package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.common.Time
import kpn.core.tools.monitor.MonitorRouteGpxReader
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.core.util.ValidationException
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteReferenceUtil
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.XML

@Component
class MonitorMultiGpxUpload(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer
) {

  private val log = Log(classOf[MonitorMultiGpxUpload])
  private val geometryFactory = new GeometryFactory

  def execute(context: MonitorContext): Unit = {
    val args = MonitorUpdateArgs(
      context.value.user,
      context.value.reporter,
      context.value.update,
    )
    newExecute(args)
  }

  private def newExecute(args: MonitorUpdateArgs): Unit = {

    args.reporter.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-add", "upload"),
          MonitorRouteUpdateStatusCommand("step-add", "save"),
          MonitorRouteUpdateStatusCommand("step-active", "upload"),
        )
      )
    )

    val group = monitorUpdateCommon.findGroup(args)
    val route = monitorUpdateCommon.findRoute(args, group)

    val now = Time.now
    val referenceTimestamp = args.update.referenceTimestamp.getOrElse(throw new RuntimeException("reference timestamp missing in update"))
    val relationId = args.update.relationId.getOrElse(throw new RuntimeException("relationId missing in update"))

    val geometryCollection: GeometryCollection = args.update.migrationGeojson match {
      case Some(migrationGeojson) =>
        val geometryFactory = new GeometryFactory
        new GeoJsonReader(geometryFactory).read(migrationGeojson).asInstanceOf[GeometryCollection]
      case None =>
        val referenceGpx = args.update.referenceGpx.getOrElse(throw new RuntimeException("reference gpx missing in update"))
        val xml = try {
          XML.loadString(referenceGpx)
        }
        catch {
          case e: SAXParseException =>
            throw new ValidationException("invalid-reference-file")
        }

        new MonitorRouteGpxReader().read(xml)
    }

    val bounds = MonitorRouteAnalysisSupport.geometryBounds(geometryCollection)
    val geoJson = args.update.migrationGeojson match {
      case Some(migrationGeojson) => migrationGeojson
      case None => MonitorRouteAnalysisSupport.toGeoJson(geometryCollection)
    }

    val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometryCollection)
    val distance = Math.round(referenceLineStrings.map(Haversine.meters).sum)
    val segmentCount = geometryCollection.getNumGeometries

    val objectId = monitorRouteRepository.routeRelationReferenceId(route._id, Some(relationId)).getOrElse(ObjectId())

    val reference = MonitorRouteReference(
      objectId,
      routeId = route._id,
      relationId = Some(relationId),
      timestamp = now,
      user = args.user,
      referenceBounds = bounds,
      referenceType = MonitorReferenceType.gpx,
      referenceTimestamp = referenceTimestamp,
      referenceDistance = distance,
      referenceSegmentCount = segmentCount,
      referenceFilename = args.update.referenceFilename,
      referenceGeoJson = geoJson
    )

    monitorRouteRepository.saveRouteReference(reference)

    val routeCoordinateArrays = routeRepository.coordinatesArrays(Seq(relationId))
    val routeLines = routeCoordinateArrays.map { coordinateArray =>
      val flipped = coordinateArray.map(c => new Coordinate(c.y, c.x))
      geometryFactory.createLineString(flipped)
    }

    val referenceLines = {
      val referenceGeometry = new GeoJsonReader().read(reference.referenceGeoJson)
      MonitorRouteReferenceUtil.toLineStrings(referenceGeometry)
    }

    val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(routeLines, referenceLines)

    val stateId = monitorRouteRepository.routeState(route._id, relationId) match {
      case Some(routeState) => routeState._id
      case None => ObjectId()
    }

    monitorRouteRepository.saveRouteState(
      MonitorRouteState(
        stateId,
        route._id,
        relationId,
        now,
        deviationAnalysis.matchesDistance,
        deviationAnalysis.matchesGeometry,
        deviationAnalysis.deviations,
      )
    )

    val updatedRoute = monitorUpdateCommon.updateSuperRoute(route)

    args.reporter.stepActive("save")
    monitorRouteRepository.saveRoute(updatedRoute)
    args.reporter.stepDone("save")
  }
}
