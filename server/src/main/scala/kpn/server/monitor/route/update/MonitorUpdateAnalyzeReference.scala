package kpn.server.monitor.route.update

import kpn.api.base.ObjectId
import kpn.api.common.data.WayMember
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.custom.Relation
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.core.util.Util
import kpn.server.analyzer.engine.monitor.MonitorFilter
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzer
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteAnalysis
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import org.springframework.stereotype.Component

@Component
class MonitorUpdateAnalyzeReference(
  monitorRouteRelationRepository: MonitorRouteRelationRepository,
  monitorRouteOsmSegmentAnalyzer: MonitorRouteOsmSegmentAnalyzer,
  monitorRouteDeviationAnalyzer: MonitorRouteDeviationAnalyzer,
) {

  private val log = Log(classOf[MonitorUpdateAnalyzeReference])

  def analyzeReference(context: MonitorContext, reference: MonitorRouteReference, currentRelation: Option[Relation]): Option[MonitorRouteState] = {
    reference.relationId.flatMap { relationId =>
      val relationOption = if (currentRelation.nonEmpty) {
        currentRelation
      }
      else if (context.value.isReferenceTypeGpx) {
        monitorRouteRelationRepository.load(None, relationId)
      }
      else {
        monitorRouteRelationRepository.loadTopLevel(None, relationId)
      }

      relationOption.flatMap { relation =>
        if (context.value.isReferenceTypeGpx) {
          updateSubRelationOsmInfo(context, relation)
        }

        val allWayMembers = if (context.value.isReferenceTypeGpx) {
          collectAllWayMembers(relation)
        }
        else {
          relation.wayMembers
        }
        val wayMembers = MonitorFilter.filterWayMembers(allWayMembers)
        val osmSegmentAnalysis = monitorRouteOsmSegmentAnalyzer.analyze(wayMembers)

        val deviationAnalysis = monitorRouteDeviationAnalyzer.analyze(wayMembers.map(_.way), reference.referenceGeoJson)

        val bounds = Util.mergeBounds(osmSegmentAnalysis.routeSegments.map(_.segment.bounds) ++ deviationAnalysis.deviations.map(_.bounds))
        val routeAnalysis = MonitorRouteAnalysis(
          relation,
          wayMembers.size,
          osmSegmentAnalysis.startNodeId,
          osmSegmentAnalysis.endNodeId,
          osmSegmentAnalysis.osmDistance,
          deviationAnalysis.referenceDistance,
          bounds,
          osmSegmentAnalysis.routeSegments.map(_.segment),
          Some(deviationAnalysis.referenceGeometry),
          deviationAnalysis.matchesGeometry,
          deviationAnalysis.deviations,
          relations = Seq.empty
        )

        val happy = routeAnalysis.gpxDistance > 0 &&
          routeAnalysis.deviations.isEmpty &&
          routeAnalysis.osmSegments.sizeIs == 1

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
            routeAnalysis.wayCount,
            routeAnalysis.startNodeId,
            routeAnalysis.endNodeId,
            routeAnalysis.osmDistance,
            routeAnalysis.bounds,
            routeAnalysis.osmSegments,
            routeAnalysis.matchesGeometry,
            routeAnalysis.deviations,
            happy,
          )
        )
      }
    }
  }

  private def updateSubRelationOsmInfo(context: MonitorContext, relation: Relation): Unit = {
    context.value.newRoute match {
      case None =>
      case Some(newRoute) =>
        val updatedRelation = newRoute.relation.map { monitorRouteRelation =>
          updateSubRelationOsmInfo(relation, monitorRouteRelation)
        }
        context.set(
          context.value.copy(
            newRoute = Some(
              newRoute.copy(
                relation = updatedRelation
              )
            )
          )
        )
    }
  }

  private def updateSubRelationOsmInfo(relation: Relation, monitorRouteRelation: MonitorRouteRelation): MonitorRouteRelation = {

    findSubRelation(relation, monitorRouteRelation.relationId) match {
      case None => monitorRouteRelation
      case Some(subRelation) =>

        val updatedRelations = monitorRouteRelation.relations.map { subMonitorRouteRelation =>
          updateSubRelationOsmInfo(subRelation, subMonitorRouteRelation)
        }

        val wayMembers = MonitorFilter.filterWayMembers(subRelation.wayMembers)
        val osmWayCount = wayMembers.size
        val osmDistance = wayMembers.map(_.way.length).sum
        val osmDistanceSubRelations = updatedRelations.map { monitorRouteRelation =>
          monitorRouteRelation.osmDistance + monitorRouteRelation.osmDistanceSubRelations
        }.sum

        monitorRouteRelation.copy(
          osmWayCount = osmWayCount,
          osmDistance = osmDistance,
          osmDistanceSubRelations = osmDistanceSubRelations,
          relations = updatedRelations
        )
    }
  }

  private def findSubRelation(relation: Relation, relationId: Long): Option[Relation] = {
    if (relation.id == relationId) {
      Some(relation)
    }
    else {
      relation.relationMembers.flatMap { subRelationMember =>
        findSubRelation(subRelationMember.relation, relationId)
      }.headOption
    }
  }

  private def collectAllWayMembers(relation: Relation): Seq[WayMember] = {
    val wayMembers = relation.wayMembers
    val subRelationWayMembers = relation.relationMembers.flatMap { relationMember =>
      collectAllWayMembers(relationMember.relation)
    }
    wayMembers ++ subRelationWayMembers
  }
}
