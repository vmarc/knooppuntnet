package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteRelationStructureRow
import kpn.server.config.RequestContext
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.domain.MonitorRouteReference
import kpn.server.monitor.domain.MonitorRouteState
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteDetailsPageBuilder(
  routeRepository: RouteRepository,
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository
) {

  def build(groupName: String, routeName: String): Option[MonitorRouteDetailsPage] = {
    val admin = monitorRepository.isAdminUser(RequestContext.user)
    monitorGroupRepository.groupByName(groupName).flatMap { group =>
      monitorRouteRepository.routeByName(group._id, routeName).map { monitorRoute =>

        val references = monitorRouteRepository.routeReferences(monitorRoute._id) // TODO limit query to only the info that is needed
        val states = monitorRouteRepository.routeStates(monitorRoute._id) // TODO limit query to only the info that is needed: deviationCount, deviationDistance

        val routeDocOption = monitorRoute.relationId match {
          case None => None
          case Some(relationId) =>
            routeRepository.findRouteById(relationId)
        }

        val structureRows = flattenRelationTree(
          monitorRoute,
          monitorRoute.relation,
          references,
          states
        )
        val relationCount = structureRows match {
          case Some(rows) => rows.size
          case None => 0
        }
        val relationLevels = structureRows match {
          case Some(rows) => rows.map(_.level).max
          case None => 0
        }
        MonitorRouteDetailsPage(
          admin,
          group.name,
          group.description,
          monitorRoute.name,
          monitorRoute.description,
          monitorRoute.relationId,
          monitorRoute.comment,
          monitorRoute.symbol,
          monitorRoute.analysisTimestamp,
          monitorRoute.analysisDuration,
          monitorRoute.referenceType,
          monitorRoute.referenceTimestamp,
          monitorRoute.referenceFilename,
          monitorRoute.referenceDistance,
          monitorRoute.deviationDistance,
          monitorRoute.deviationCount,
          monitorRoute.osmSegmentCount,
          monitorRoute.happy,
          routeDocOption.map(_.summary.wayCount).getOrElse(0),
          routeDocOption.map(_.summary.meters).getOrElse(0),
          relationCount,
          relationLevels,
          structureRows
        )
      }
    }
  }

  private def flattenRelationTree(
    route: MonitorRoute,
    relation: Option[MonitorRouteRelation],
    references: Seq[MonitorRouteReference],
    states: Seq[MonitorRouteState]
  ): Option[Seq[MonitorRouteRelationStructureRow]] = {
    relation.flatMap { relationLevel1 =>
      val rowsLevel2 = relationLevel1.relations.flatMap { relationLevel2 =>
        val rowsLevel3 = relationLevel2.relations.flatMap { relationLevel3 =>
          val rowsLevel4 = relationLevel3.relations.flatMap { relationLevel4 =>
            val rowsLevel5 = relationLevel4.relations.map { relationLevel5 =>
              toRow(route, 5, relationLevel5, references, states)
            }
            toRow(route, 4, relationLevel4, references, states) +: rowsLevel5
          }
          toRow(route, 3, relationLevel3, references, states) +: rowsLevel4
        }
        toRow(route, 2, relationLevel2, references, states) +: rowsLevel3
      }
      Option.when(rowsLevel2.nonEmpty) {
        toRow(route, 1, relationLevel1, references, states) +: rowsLevel2
      }
    }
  }

  private def toRow(
    route: MonitorRoute,
    level: Long,
    monitorRouteRelation: MonitorRouteRelation,
    references: Seq[MonitorRouteReference],
    states: Seq[MonitorRouteState]
  ): MonitorRouteRelationStructureRow = {

    val reference = if (route.referenceType == MonitorReferenceType.multiGpx) {
      references.find(_.relationId.contains(monitorRouteRelation.relationId))
    }
    else {
      None
    }

    val state = if (route.referenceType == MonitorReferenceType.multiGpx) {
      states.find(_.relationId == monitorRouteRelation.relationId)
    }
    else {
      None
    }

    val physical = monitorRouteRelation.referenceFilename.isDefined

    val visible = if (route.referenceType == MonitorReferenceType.gpx) {
      level == 1
    }
    else {
      physical
    }

    val showMap = if (route.referenceType == MonitorReferenceType.gpx) level == 1 else physical
    val deviationDistance = if (visible) Some(monitorRouteRelation.deviationDistance) else None
    val deviationCount = if (visible) Some(monitorRouteRelation.deviationCount) else None

    MonitorRouteRelationStructureRow(
      level = level,
      physical = physical,
      name = monitorRouteRelation.name,
      relationId = monitorRouteRelation.relationId,
      subRelationIndex = None,
      role = monitorRouteRelation.role,
      survey = monitorRouteRelation.survey,
      symbol = monitorRouteRelation.symbol,
      referenceTimestamp = reference.map(_.referenceTimestamp),
      referenceFilename = reference.flatMap(_.referenceFilename),
      referenceDistance = reference.map(_.referenceDistance).getOrElse(0),
      deviationDistance = state.map(_.deviations.map(_.distance).sum),
      deviationCount = state.map(_.deviations.length),
      osmSegmentCount = Some(-1),
      osmDistance = -1,
      osmDistanceSubRelations = -1,
      gaps = None, // TODO redesign cleanup - gaps = monitorRouteRelation.gaps,
      showMap = showMap,
      happy = monitorRouteRelation.happy
    )
  }
}
