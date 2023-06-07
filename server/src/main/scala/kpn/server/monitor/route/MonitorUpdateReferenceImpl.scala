package kpn.server.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.tools.monitor.MonitorRouteGpxReader
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport.toMeters
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteReferenceUtil
import kpn.server.monitor.MonitorUtil
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonWriter
import org.springframework.stereotype.Component

import scala.xml.XML

@Component
class MonitorUpdateReferenceImpl(
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer
) extends MonitorUpdateReference {

  private val log = Log(classOf[MonitorUpdateReferenceImpl])

  def update(context: MonitorUpdateContext): MonitorUpdateContext = {

    context.oldRoute match {
      case None =>
        context.newRoute match {
          case None =>
            // no old or new route: no references to update
            context
          case Some(newRoute) =>
            // a new route is added, if reference type "osm": update reference
            if (newRoute.referenceType == "osm") {
              updateReferences(context, newRoute)
            }
            else if (newRoute.referenceType == "gpx") {
              context.referenceGpx match {
                case None => context
                case Some(referenceGpx) =>

                  val referenceTimestamp = newRoute.referenceTimestamp.getOrElse(throw new RuntimeException("reference timestamp not found"))

                  val now = Time.now
                  val xml = XML.loadString(referenceGpx)
                  val geometryCollection = new MonitorRouteGpxReader().read(xml)
                  val bounds = MonitorRouteAnalysisSupport.geometryBounds(geometryCollection)
                  val geoJson = MonitorRouteAnalysisSupport.toGeoJson(geometryCollection)

                  // TODO should delete already existing reference here?

                  val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometryCollection)
                  val distance = Math.round(toMeters(referenceLineStrings.map(_.getLength).sum))
                  val segmentCount = geometryCollection.getNumGeometries

                  val reference = MonitorRouteReference(
                    ObjectId(),
                    routeId = context.routeId,
                    relationId = context.relationId,
                    timestamp = now,
                    user = context.user,
                    bounds = bounds,
                    referenceType = "gpx",
                    referenceTimestamp = referenceTimestamp,
                    distance = distance,
                    segmentCount = segmentCount,
                    filename = context.referenceFilename,
                    geoJson = geoJson
                  )

                  val updatedNewRoute = newRoute.copy(
                    referenceDistance = distance
                  )

                  context.copy(
                    newReferences = context.newReferences :+ reference,
                    newRoute = Some(updatedNewRoute)
                  )
              }
            }
            else {
              // nothing to do if not reference type "osm"
              context
            }
        }
      case Some(oldRoute) =>
        context.newRoute match {
          case None =>
            // no changes to route definition: no need to update reference
            context
          case Some(newRoute) =>
            if (isOsmReferenceChanged(oldRoute, newRoute)) {
              updateReferences(context, newRoute)
            }
            else {
              context
            }
        }
    }
  }

  private def isOsmReferenceChanged(oldRoute: MonitorRoute, newRoute: MonitorRoute): Boolean = {
    newRoute.referenceType == "osm" && (
      oldRoute.referenceType != newRoute.referenceType ||
        oldRoute.relationId != newRoute.relationId ||
        oldRoute.referenceTimestamp != newRoute.referenceTimestamp
      )
  }

  private def updateReferences(context: MonitorUpdateContext, newRoute: MonitorRoute): MonitorUpdateContext = {

    val updatedContext = context.copy(removeOldReferences = true)

    newRoute.relationId match {
      case None =>
        // relationId is not defined yet (or not defined anymore), no need to update reference
        updatedContext

      case Some(relationId) =>
        val referenceTimestamp = newRoute.referenceTimestamp.getOrElse(throw new RuntimeException("reference timestamp not found"))
        val monitorRouteRelation = newRoute.relation.getOrElse(throw new RuntimeException("route structure not found"))
        if (monitorRouteRelation.relations.isEmpty) {
          updateSingleReference(updatedContext, newRoute, referenceTimestamp, relationId)
        }
        else {
          updateSuperRouteReferences(updatedContext, newRoute, referenceTimestamp)
        }
    }
  }

  private def updateSingleReference(
    context: MonitorUpdateContext,
    newRoute: MonitorRoute,
    referenceTimestamp: Timestamp,
    relationId: Long
  ): MonitorUpdateContext = {

    monitorRouteRelationRepository.loadTopLevel(Some(referenceTimestamp), relationId) match {
      case None =>
        val newRoute = context.newRoute match {
          case Some(route) => resetAnalysis(route)
          case None => resetAnalysis(context.oldRoute.get)
        }

        context.copy(
          newRoute = Some(newRoute),
          saveResult = context.saveResult.copy(
            errors = context.saveResult.errors :+ s"Could not load relation $relationId at ${referenceTimestamp.yyyymmddhhmmss}"
          )
        )

      case Some(relation) =>
        val reference = buildReference(context, newRoute, relation)
        context.copy(
          newReferences = context.newReferences :+ reference
        )
    }
  }

  private def updateSuperRouteReferences(
    originalContext: MonitorUpdateContext,
    newRoute: MonitorRoute,
    referenceTimestamp: Timestamp
  ): MonitorUpdateContext = {

    var context = originalContext

    val references = MonitorUtil.subRelationsIn(newRoute).flatMap { monitorRouteSubRelation =>
      log.info(s"build reference ${monitorRouteSubRelation.relationId} ${monitorRouteSubRelation.name}")
      monitorRouteRelationRepository.loadTopLevel(Some(referenceTimestamp), monitorRouteSubRelation.relationId) match {
        case Some(subRelation) => Some(buildReference(context, newRoute, subRelation))
        case None =>
          context = context.copy(
            saveResult = context.saveResult.copy(
              errors = context.saveResult.errors :+ s"Could not load relation ${monitorRouteSubRelation.relationId} at ${referenceTimestamp.yyyymmddhhmmss}"
            )
          )
          None
      }
    }

    context.copy(
      newReferences = context.newReferences ++ references
    )
  }

  private def buildReference(context: MonitorUpdateContext, newRoute: MonitorRoute, relation: Relation): MonitorRouteReference = {

    val wayMembers = MonitorFilter.filterWayMembers(relation.wayMembers)
    val bounds = Bounds.from(wayMembers.flatMap(_.way.nodes))
    val analysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)

    val geomFactory = new GeometryFactory
    val geometryCollection = new GeometryCollection(analysis.routeSegments.map(_.lineString).toArray, geomFactory)
    val geoJsonWriter = new GeoJsonWriter()
    geoJsonWriter.setEncodeCRS(false)
    val geometry = geoJsonWriter.write(geometryCollection)

    MonitorRouteReference(
      ObjectId(),
      newRoute._id,
      Some(relation.id),
      Time.now,
      context.user,
      bounds,
      "osm",
      newRoute.referenceTimestamp.get,
      analysis.osmDistance,
      analysis.routeSegments.size,
      None,
      geometry
    )
  }

  private def resetAnalysis(route: MonitorRoute): MonitorRoute = {
    val updatedRelation = route.relation.map { relation =>
      relation.copy(
        referenceDistance = 0, // only filled in for referenceType = "multi-gpx"
        deviationDistance = 0,
        deviationCount = 0,
        osmWayCount = 0,
        osmDistance = 0,
        osmSegmentCount = 0,
        happy = false,
      )
    }

    route.copy(
      referenceDistance = 0,
      deviationCount = 0,
      osmWayCount = 0,
      osmDistance = 0,
      osmSegmentCount = 0,
      osmSegments = Seq.empty,
      relation = updatedRelation,
      happy = false
    )
  }
}
