package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteUpdateStatusCommand
import kpn.api.common.monitor.MonitorRouteUpdateStatusMessage
import kpn.core.common.Time
import kpn.core.tools.monitor.MonitorRouteGpxReader
import kpn.core.util.CoordinateUtil
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.core.util.ValidationException
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.analyzer.engine.monitor.MonitorRouteReferenceUtil
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteReferenceSummary
import kpn.server.monitor.repository.MonitorRouteRepository
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.XML

@Component
class MonitorGpxUpload(
  monitorRouteRepository: MonitorRouteRepository,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorUpdateAnalyzeReference: MonitorUpdateAnalyzeReference,
  monitorUpdateCommon: MonitorUpdateCommon,
  monitorUpdateSave: MonitorUpdateSave,
  monitorMultiGpxUpload: MonitorMultiGpxUpload
) {

  private val log = Log(classOf[MonitorGpxUpload])

  def execute(context: MonitorContext): Unit = {

    if (context.value.isReferenceTypeMultiGpx) {
      monitorMultiGpxUpload.execute(context)
      return
    }

    context.report(
      MonitorRouteUpdateStatusMessage(
        commands = Seq(
          MonitorRouteUpdateStatusCommand("step-add", "upload"),
          MonitorRouteUpdateStatusCommand("step-add", "save"),
          MonitorRouteUpdateStatusCommand("step-active", "upload"),
        )
      )
    )

    monitorUpdateCommon.oldFindGroup(context)
    monitorUpdateCommon.oldFindRoute(context)

    val oldReferenceIds = monitorRouteRepository.routeReferenceIds(context.value.routeId)
    val oldStateIds = monitorRouteRepository.routeStateIds(context.value.routeId)
    context.set(
      context.value.copy(
        oldReferenceIds = oldReferenceIds,
        oldStateIds = oldStateIds
      )
    )

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

    val referenceLines = referenceLineStrings.map(CoordinateUtil.lineStringToCoordinates)

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
      referenceLines = referenceLines,
    )

    context.upsertRouteReference(reference)
    monitorRouteRepository.saveRouteReference(reference)

    context.set(
      context.value.copy(
        newReferenceSummaries = context.value.newReferenceSummaries :+ MonitorRouteReferenceSummary.from(reference),
      )
    )

    if (context.value.isReferenceTypeMultiGpx) { // TODO referenceType will always be "multi-gpx" ?
      context.set(
        context.value.copy(
          newRoute = context.value.oldRoute
        )
      )
    }
    else {
      context.set(
        context.value.copy(
          newRoute = Some(
            context.value.oldRoute.get.copy(
              referenceDistance = reference.referenceDistance
            )
          )
        )
      )
    }

    monitorRouteRelationRepository.loadTopLevel(None, relationId) match {
      case None =>
      case Some(relation) =>
        monitorUpdateAnalyzeReference.analyzeReference(context, reference, Some(relation)) match {
          case None =>
          case Some(state) =>
            monitorRouteRepository.saveRouteState(state)
            context.set(
              context.value.copy(
                stateChanged = true
              )
            )
            context.stepActive("save")
            monitorUpdateSave.save(context)
            context.stepDone("save")
        }
    }
  }
}
