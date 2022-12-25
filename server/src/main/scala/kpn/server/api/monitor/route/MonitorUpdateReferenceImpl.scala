package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.monitor.MonitorRouteFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteRelationReference
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
          case None => context
          case Some(newRoute) =>
            if (newRoute.referenceType.contains("osm")) {
              updateOsmReferences(context, newRoute)
            }
            else {
              context
            }
        }

      case Some(oldRoute) =>
        context.newRoute match {
          case None => context
          case Some(newRoute) =>

            if (isOsmReferenceChanged(oldRoute, newRoute)) {
              updateOsmReferences(context, newRoute)
            }
            else if (isGpxReferenceChanged(oldRoute, newRoute)) {
              context
            }
            else {
              context
            }

        }
    }
  }

  private def isOsmReferenceChanged(oldRoute: MonitorRoute, newRoute: MonitorRoute): Boolean = {
    newRoute.referenceType.contains("osm") && (
      oldRoute.referenceType != newRoute.referenceType ||
        oldRoute.relationId != newRoute.relationId ||
        oldRoute.referenceDay != newRoute.referenceDay
      )
  }

  private def isGpxReferenceChanged(oldRoute: MonitorRoute, newRoute: MonitorRoute): Boolean = {
    throw new RuntimeException("TODO")
  }

  private def updateOsmReferences(context: MonitorUpdateContext, newRoute: MonitorRoute): MonitorUpdateContext = {
    newRoute.relationId match {
      case None => context // TODO add error in MonitorRouteSaveResult ???
      case Some(relationId) =>
        newRoute.referenceDay match {
          case None => context // TODO add error in MonitorRouteSaveResult ???
          case Some(referenceDay) =>
            monitorRouteRelationRepository.load(Some(Timestamp(referenceDay)), relationId) match {
              case None => context // TODO add error in MonitorRouteSaveResult !!
              case Some(relation) =>
                val relations: Seq[Relation] = MonitorRouteFilter.relationsInRelation(relation)
                val references = relations.map { routeRelation =>

                  val wayMembers = MonitorRouteFilter.filterWayMembers(routeRelation.wayMembers)
                  val bounds = Bounds.from(wayMembers.flatMap(_.way.nodes))
                  val analysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)

                  val geomFactory = new GeometryFactory
                  val geometryCollection = new GeometryCollection(analysis.routeSegments.map(_.lineString).toArray, geomFactory)
                  val geoJsonWriter = new GeoJsonWriter()
                  geoJsonWriter.setEncodeCRS(false)
                  val geometry = geoJsonWriter.write(geometryCollection)

                  MonitorRouteRelationReference(
                    ObjectId(),
                    newRoute._id,
                    routeRelation.id,
                    analysis.osmDistance,
                    bounds,
                    analysis.routeSegments.size,
                    geometry
                  )
                }

                val referenceDistance = references.map(_.distance).sum
                val updatedNewRoute = context.newRoute.map { route =>
                  route.copy(
                    referenceDistance = referenceDistance
                  )
                }

                context.copy(
                  newRoute = updatedNewRoute,
                  newReferences = references,
                )
            }
        }
    }
  }
}
