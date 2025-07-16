package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorCommand
import kpn.api.common.monitor.MonitorMessage
import kpn.api.common.monitor.MonitorReferenceType
import kpn.core.common.Time
import kpn.core.tools.monitor.MonitorRouteGpxReader
import kpn.core.util.CoordinateUtil
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.core.util.ValidationException
import kpn.server.analyzer.engine.monitor.MonitorReferenceUtil
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.state.MonitorStateStore
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorState
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.XML

@Component
class MonitorGpxUpload(
  routeRepository: RouteRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer,
  monitorReferenceBuilder: MonitorReferenceBuilder,
  monitorStateStore: MonitorStateStore
) {

  private val log = Log(classOf[MonitorGpxUpload])
  private val geometryFactory = new GeometryFactory

  def execute(args: MonitorUpdateArgs): Unit = {

    if (args.update.referenceType != MonitorReferenceType.multiGpx) {
      throw new RuntimeException(s"invalid reference type ${args.update.referenceType} for gpx upload")
    }

    args.reporter.report(
      MonitorMessage(
        MonitorCommand.add("upload"),
        MonitorCommand.add("save"),
        MonitorCommand.active("upload"),
      )
    )

    val group = monitorUpdateCommon.findGroup(args)
    val route = monitorUpdateCommon.findRoute(group, args)

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

    val referenceLineStrings = MonitorReferenceUtil.toLineStrings(geometryCollection)
    val distance = Math.round(referenceLineStrings.map(Haversine.meters).sum)
    val segmentCount = geometryCollection.getNumGeometries

    val objectId = monitorRouteRepository.routeRelationReferenceId(route._id, Some(relationId)).getOrElse(ObjectId())
    val referenceLines1 = referenceLineStrings.map(CoordinateUtil.lineStringToCoordinates)

    val reference = monitorReferenceBuilder.build(
      MonitorReference(
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
        referenceLines = referenceLines1,
        Seq.empty
      )
    )

    monitorRouteRepository.saveReference(reference)

    val routeCoordinateArrays = routeRepository.coordinatesArrays(Seq(relationId))
    val routeLines = routeCoordinateArrays.map { coordinateArray =>
      geometryFactory.createLineString(coordinateArray)
    }

    val referenceLines = reference.referenceLines.map(CoordinateUtil.coordinatesToLineString)

    val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(routeLines, referenceLines)

    val stateId = monitorRouteRepository.state(route._id, relationId) match {
      case Some(routeState) => routeState._id
      case None => ObjectId()
    }

    monitorStateStore.saveState(
      MonitorState(
        stateId,
        route._id,
        relationId,
        now,
        deviationAnalysis.deviations,
        deviationAnalysis.matchesDistance,
        deviationAnalysis.matchesLines
      )
    )

    val updatedRoute = monitorUpdateCommon.updateSuperRoute(route)

    args.reporter.stepActive("save")
    monitorRouteRepository.saveRoute(updatedRoute)
    args.reporter.stepDone("save")
  }
}
