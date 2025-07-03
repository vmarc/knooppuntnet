package kpn.server.monitor.route

import kpn.api.common.data.MemberType
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.route.RouteStructureRow
import kpn.api.common.route.StructureRow
import kpn.core.doc.RouteDoc
import kpn.server.config.RequestContext
import kpn.server.monitor.domain.MonitorGroup
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
      monitorRouteRepository.routeByName(group._id, routeName).flatMap { monitorRoute =>
        monitorRoute.relationId.flatMap(routeRepository.findRouteById).map { routeDoc =>
          val references = monitorRouteRepository.routeReferences(monitorRoute._id) // TODO limit query to only the info that is needed
          val states = monitorRouteRepository.routeStates(monitorRoute._id) // TODO limit query to only the info that is needed: deviationCount, deviationDistance
          buildPage(admin, group, monitorRoute, routeDoc, references, states)
        }
      }
    }
  }

  private def buildPage(
    admin: Boolean,
    group: MonitorGroup,
    monitorRoute: MonitorRoute,
    routeDoc: RouteDoc,
    references: Seq[MonitorRouteReference],
    states: Seq[MonitorRouteState]
  ) = {

    val structureRows = migrateRows(
      monitorRoute,
      routeDoc,
      monitorRoute.relation,
      references,
      states
    )
    val relationCount = structureRows.count(_.memberType == MemberType.Relation)
    val relationLevels = structureRows.map(_.level).max

    val deviationDistance = states.flatMap(_.deviations.map(_.distance)).sum
    val deviationCount = states.map(_.deviations.length).sum
    val osmSegmentCount = routeDoc.segments.length
    val happy = false // TODO redesign

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

      deviationDistance,
      deviationCount,
      osmSegmentCount,
      happy,

      routeDoc.summary.wayCount,
      routeDoc.summary.meters,
      relationCount,
      relationLevels,
      structureRows
    )
  }

  private def migrateRows(
    route: MonitorRoute,
    routeDoc: RouteDoc,
    relation: Option[MonitorRouteRelation],
    references: Seq[MonitorRouteReference],
    states: Seq[MonitorRouteState]
  ): Seq[StructureRow] = {
    routeDoc.structureRows.zipWithIndex.map { case (row, index) => toRow(index, row, route, 1, references, states) }
  }

  private def toRow(
    rowIndex: Long,
    row: RouteStructureRow,
    route: MonitorRoute,
    level: Long,
    references: Seq[MonitorRouteReference],
    states: Seq[MonitorRouteState]
  ): StructureRow = {

    val reference = if (row.memberType == MemberType.Relation && route.referenceType == MonitorReferenceType.multiGpx) {
      references.find(_.relationId.contains(row.id))
    }
    else {
      None
    }

    val state = if (row.memberType == MemberType.Relation && route.referenceType == MonitorReferenceType.multiGpx) {
      states.find(_.relationId == row.id)
    }
    else {
      None
    }

    val physical = false // monitorRouteRelation.referenceFilename.isDefined

    val visible = if (route.referenceType == MonitorReferenceType.gpx) {
      level == 1
    }
    else {
      physical
    }

    val showMap = if (route.referenceType == MonitorReferenceType.gpx) level == 1 else physical
    val deviationDistance = 0 // TODO redesign - if (visible) Some(monitorRouteRelation.deviationDistance) else None
    val deviationCount = 0 // TODO redesign - if (visible) Some(monitorRouteRelation.deviationCount) else None

    StructureRow(
      rowIndex = rowIndex,
      level = level,
      id = row.id,
      memberType = row.memberType,
      role = row.role,
      link = row.link,
      distance = row.distance,
      name = row.name,
      poi = row.poi,
      way = row.way,
      relation = row.relation,
      segmentIds = row.segmentIds,
      pathIds = row.pathIds,
      physical = physical,
      relationId = if (row.memberType == MemberType.Relation) row.id else 0,
      subRelationIndex = None,
      survey = None, // TODO redesign - row.survey,
      symbol = None, // TODO redesign - row.symbol,
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
      happy = false // TODO redesign - monitorRouteRelation.happy
    )
  }
}
