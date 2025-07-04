package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.common.Time
import kpn.core.tools.monitor.MonitorRouteGpxReader
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.core.util.Util
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

    context.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-add", "upload"),
          MonitorRouteUpdateStatusCommand("step-add", "save"),
          MonitorRouteUpdateStatusCommand("step-active", "upload"),
        )
      )
    )

    monitorUpdateCommon.findGroup(context)
    monitorUpdateCommon.findRoute(context)

    val now = Time.now
    val referenceTimestamp = context.value.update.referenceTimestamp.getOrElse(throw new RuntimeException("reference timestamp missing in update"))
    val relationId = context.value.update.relationId.getOrElse(throw new RuntimeException("relationId missing in update"))

    val geometryCollection: GeometryCollection = context.value.update.migrationGeojson match {
      case Some(migrationGeojson) =>
        val geometryFactory = new GeometryFactory
        new GeoJsonReader(geometryFactory).read(migrationGeojson).asInstanceOf[GeometryCollection]
      case None =>
        val referenceGpx = context.value.update.referenceGpx.getOrElse(throw new RuntimeException("reference gpx missing in update"))
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
    val geoJson = context.value.update.migrationGeojson match {
      case Some(migrationGeojson) => migrationGeojson
      case None => MonitorRouteAnalysisSupport.toGeoJson(geometryCollection)
    }

    val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometryCollection)
    val distance = Math.round(referenceLineStrings.map(Haversine.meters).sum)
    val segmentCount = geometryCollection.getNumGeometries

    val objectId = context.value.oldReferenceIds.filter(_.relationId.contains(relationId)).map(_._id).headOption.getOrElse(ObjectId())

    val reference = MonitorRouteReference(
      objectId,
      routeId = context.value.routeId,
      relationId = Some(relationId),
      timestamp = now,
      user = context.value.user,
      referenceBounds = bounds,
      referenceType = MonitorReferenceType.gpx,
      referenceTimestamp = referenceTimestamp,
      referenceDistance = distance,
      referenceSegmentCount = segmentCount,
      referenceFilename = context.value.update.referenceFilename,
      referenceGeoJson = geoJson
    )

    context.upsertRouteReference(reference)
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

    val stateId = monitorRouteRepository.routeState(context.value.routeId, relationId) match {
      case Some(routeState) => routeState._id
      case None => ObjectId()
    }

    monitorRouteRepository.saveRouteState(
      MonitorRouteState(
        stateId,
        context.value.routeId,
        relationId,
        now,
        deviationAnalysis.matchesDistance,
        deviationAnalysis.matchesGeometry,
        deviationAnalysis.deviations,
      )
    )

    val references = monitorRouteRepository.routeReferences(context.value.routeId)
    val referenceDistance = references.map(_.referenceDistance).sum
    val states = monitorRouteRepository.routeStates(context.value.routeId)
    val deviationCount = states.map(_.deviations.length).sum
    val deviationDistance = states.map(_.deviations.length).sum
    val matchesDistance = states.map(_.matchesDistance).sum

    val (superSegmentCount: Long, osmDistance: Long) = context.value.relationId.flatMap(routeRepository.findRouteById) match {
      case Some(routeDoc) =>
        val sc = routeDoc.superSegments.length.toLong
        val di = routeDoc.superSegments.map(_.segments.map(_.relationSegment.meters).sum).sum
        (sc, di)
      case None => (0L, 0L)
    }

    val happy = Util.isWithinTolerance(matchesDistance.toDouble, osmDistance.toDouble) && deviationCount == 0 && superSegmentCount == 1

    val updatedRoute = context.value.route.copy(
      analysisTimestamp = Some(now),
      referenceDistance = referenceDistance,
      deviationCount = deviationCount,
      deviationDistance = deviationDistance,
      osmSegmentCount = superSegmentCount,
      happy = happy
    )
    context.stepActive("save")
    monitorRouteRepository.saveRoute(updatedRoute)
    context.stepDone("save")
  }
}
