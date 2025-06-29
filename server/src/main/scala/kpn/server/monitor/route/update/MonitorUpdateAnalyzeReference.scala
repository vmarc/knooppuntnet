package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.custom.Relation
import kpn.core.common.Time
import kpn.core.doc.RouteRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteReferenceUtil
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.repository.RouteRepository
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.springframework.stereotype.Component

@Component
class MonitorUpdateAnalyzeReference(
  routeRepository: RouteRepository,
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer,
) {

  private val log = Log(classOf[MonitorUpdateAnalyzeReference])
  private val geometryFactory = new GeometryFactory

  def analyzeReference(context: MonitorContext, reference: MonitorRouteReference, currentRelation: Option[Relation]): Option[MonitorRouteState] = {
    reference.relationId.flatMap { relationId =>
      compareReferenceAndRelation(context, reference, currentRelation, relationId)
    }
  }

  private def compareReferenceAndRelation(context: MonitorContext, reference: MonitorRouteReference, currentRelation: Option[Relation], relationId: Long): Option[MonitorRouteState] = {

    val routeLines = routeLinesFromBaseRouteDocs(context, relationId)
    val referenceLines = {
      val referenceGeometry = new GeoJsonReader().read(reference.referenceGeoJson)
      MonitorRouteReferenceUtil.toLineStrings(referenceGeometry)
    }

    val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(routeLines, referenceLines)

    val id = if (context.value.isActionAnalyze || context.value.isActionUpdate || context.value.isActionGpxUpload) {
      context.value.oldStateIds.find(_.relationId == relationId) match {
        case Some(oldStateId) => oldStateId._id
        case None => ObjectId()
      }
    }
    else {
      ObjectId()
    }

    Some(
      MonitorRouteState(
        id,
        context.value.routeId,
        relationId,
        Time.now,
        deviationAnalysis.matchesGeometry,
        deviationAnalysis.deviations,
      )
    )
  }

  private def routeLinesFromBaseRouteDocs(context: MonitorContext, relationId: Long) = {
    val relationIds = if (context.value.isReferenceTypeGpx) {
      routeRepository.subRelationTree(relationId) match {
        case Some(routeRelation) => Seq(relationId) ++ RouteRelation.relationIds(routeRelation)
        case None => Seq(relationId)
      }
    }
    else {
      Seq(relationId)
    }

    val routeCoordinateArrays = routeRepository.coordinatesArrays(relationIds)
    val routeLines = routeCoordinateArrays.map { coordinateArray =>
      val flipped = coordinateArray.map(c => new Coordinate(c.y, c.x))
      geometryFactory.createLineString(flipped)
    }
    routeLines
  }

  // TODO redesign cleanup
  //  private def updateSubRelationOsmInfo(context: MonitorContext, relation: Relation): Unit = {
  //    context.value.newRoute match {
  //      case None =>
  //      case Some(newRoute) =>
  //        val updatedRelation = newRoute.relation.map { monitorRouteRelation =>
  //          updateSubRelationOsmInfo(relation, monitorRouteRelation)
  //        }
  //        context.set(
  //          context.value.copy(
  //            newRoute = Some(
  //              newRoute.copy(
  //                relation = updatedRelation
  //              )
  //            )
  //          )
  //        )
  //    }
  //  }
  //
  //  private def updateSubRelationOsmInfo(relation: Relation, monitorRouteRelation: MonitorRouteRelation): MonitorRouteRelation = {
  //
  //    findSubRelation(relation, monitorRouteRelation.relationId) match {
  //      case None => monitorRouteRelation
  //      case Some(subRelation) =>
  //
  //        val updatedRelations = monitorRouteRelation.relations.map { subMonitorRouteRelation =>
  //          updateSubRelationOsmInfo(subRelation, subMonitorRouteRelation)
  //        }
  //
  //        val wayMembers = MonitorFilter.filterWayMembers(subRelation.wayMembers)
  //        val osmWayCount = wayMembers.size
  //        val osmDistance = wayMembers.map(_.way.length).sum
  //        val osmDistanceSubRelations = updatedRelations.map { monitorRouteRelation =>
  //          monitorRouteRelation.osmDistance + monitorRouteRelation.osmDistanceSubRelations
  //        }.sum
  //
  //        monitorRouteRelation.copy(
  //          osmWayCount = osmWayCount,
  //          osmDistance = osmDistance,
  //          osmDistanceSubRelations = osmDistanceSubRelations,
  //          relations = updatedRelations
  //        )
  //    }
  //  }
  //
  //  private def findSubRelation(relation: Relation, relationId: Long): Option[Relation] = {
  //    if (relation.id == relationId) {
  //      Some(relation)
  //    }
  //    else {
  //      relation.relationMembers.flatMap { subRelationMember =>
  //        findSubRelation(subRelationMember.relation, relationId)
  //      }.headOption
  //    }
  //  }
  //
  //  private def collectAllWayMembers(relation: Relation): Seq[WayMember] = {
  //    val wayMembers = relation.wayMembers
  //    val subRelationWayMembers = relation.relationMembers.flatMap { relationMember =>
  //      collectAllWayMembers(relationMember.relation)
  //    }
  //    wayMembers ++ subRelationWayMembers
  //  }
}
