package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.custom.Day
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.api.monitor.MonitorUtil
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonWriter
import org.springframework.stereotype.Component

@Component
class MonitorUpdateReferenceImpl(
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer
) extends MonitorUpdateReference {

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
        oldRoute.referenceDay != newRoute.referenceDay
      )
  }

  private def updateReferences(context: MonitorUpdateContext, newRoute: MonitorRoute): MonitorUpdateContext = {

    val updatedContext = context.copy(removeOldReferences = true)

    newRoute.relationId match {
      case None =>
        // relationId is not defined yet (or not defined anymore), no need to update reference
        updatedContext

      case Some(relationId) =>
        val referenceDay = newRoute.referenceDay.getOrElse(throw new RuntimeException("reference day not found"))
        val monitorRouteRelation = newRoute.relation.getOrElse(throw new RuntimeException("route structure not found"))
        if (monitorRouteRelation.relations.isEmpty) {
          updateSingleReference(updatedContext, newRoute, referenceDay, relationId)
        }
        else {
          updateSuperRouteReferences(updatedContext, newRoute, referenceDay)
        }
    }
  }

  private def updateSingleReference(
    context: MonitorUpdateContext,
    newRoute: MonitorRoute,
    referenceDay: Day,
    relationId: Long
  ): MonitorUpdateContext = {

    monitorRouteRelationRepository.loadTopLevel(Some(Timestamp(referenceDay)), relationId) match {
      case None =>
        context.copy(
          saveResult = context.saveResult.copy(
            errors = context.saveResult.errors :+ s"Could not load relation $relationId at ${referenceDay.yyyymmdd}"
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
    referenceDay: Day
  ): MonitorUpdateContext = {

    var context = originalContext

    val references = MonitorUtil.subRelationsIn(newRoute).flatMap { monitorRouteSubRelation =>
      monitorRouteRelationRepository.loadTopLevel(Some(Timestamp(referenceDay)), monitorRouteSubRelation.relationId) match {
        case Some(subRelation) => Some(buildReference(context, newRoute, subRelation))
        case None =>
          context = context.copy(
            saveResult = context.saveResult.copy(
              errors = context.saveResult.errors :+ s"Could not load relation ${monitorRouteSubRelation.relationId} at ${referenceDay.yyyymmdd}"
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
      newRoute.referenceDay.get,
      analysis.osmDistance,
      analysis.routeSegments.size,
      None,
      geometry
    )
  }
}
