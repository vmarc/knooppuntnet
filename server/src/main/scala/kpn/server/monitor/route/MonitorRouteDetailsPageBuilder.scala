package kpn.server.monitor.route

import kpn.api.common.Language
import kpn.api.common.common.Reference
import kpn.api.common.data.MemberType
import kpn.api.common.location.LocationCandidateInfo
import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteSummary
import kpn.api.common.route.RouteDetails
import kpn.api.common.route.RouteStructureRow
import kpn.api.common.route.StructureRow
import kpn.core.doc.RouteDoc
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.config.RequestContext
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorReference
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorStateDeviationInfo
import kpn.server.monitor.repository.MonitorUserRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteDetailsPageBuilder(
  routeRepository: RouteRepository,
  monitorUserRepository: MonitorUserRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  locationService: LocationService
) {

  def build(language: Language, groupName: String, routeName: String): Option[MonitorRouteDetailsPage] = {
    val adminUser = monitorUserRepository.isAdminUser(RequestContext.user)
    monitorGroupRepository.groupByName(groupName).flatMap { group =>
      monitorRouteRepository.routeByName(group._id, routeName).flatMap { monitorRoute =>
        monitorRoute.relationId.flatMap(routeRepository.findRouteById).map { routeDoc =>
          val references = monitorRouteRepository.references(monitorRoute._id) // TODO limit query to only the info that is needed
          val stateDeviationInfos = monitorRouteRepository.stateDeviationInfos(monitorRoute._id)
          buildPage(
            language,
            adminUser,
            group,
            monitorRoute,
            routeDoc,
            references,
            stateDeviationInfos
          )
        }
      }
    }
  }

  private def buildPage(
    language: Language,
    adminUser: Boolean,
    group: MonitorGroup,
    monitorRoute: MonitorRoute,
    routeDoc: RouteDoc,
    references: Seq[MonitorReference],
    stateDeviationInfos: Seq[MonitorStateDeviationInfo]
  ): MonitorRouteDetailsPage = {

    val structureRows = convertRows(
      monitorRoute,
      routeDoc,
      references,
      stateDeviationInfos
    )
    val relationCount = structureRows.count(_.memberType == MemberType.Relation)
    val relationLevels = structureRows.map(_.level).max

    val deviationDistance = stateDeviationInfos.map(_.deviationDistance).sum
    val deviationCount = stateDeviationInfos.map(_.deviationCount).sum
    val osmSegmentCount = routeDoc.segments.length
    val happy = false // TODO redesign

    val networkReferences: Seq[Reference] = monitorRoute.relationId.toSeq.flatMap(relationId => routeRepository.networkReferences(relationId))
    val locationCandidateInfos = {
      routeDoc.locationAnalysis.candidates.map { candidate =>
        val locationNames = candidate.location.names
        val locationInfos = locationService.toInfos(language, locationNames, locationNames)
        LocationCandidateInfo(locationInfos, candidate.percentage)
      }
    }

    val summary = MonitorRouteSummary(
      adminUser,
      group.name,
      monitorRoute.name,
      monitorRoute.description,
      monitorRoute._id.oid,
      monitorRoute.relationId,
      monitorRoute.relationIds,
      memberCount = structureRows.length,
      segmentCount = monitorRoute.osmSegmentCount,
      deviationCount = monitorRoute.deviationCount,
      monitorRoute.bounds,
    )

    val details = RouteDetails(
      routeDoc._id,
      routeDoc.active,
      routeDoc.summary,
      routeDoc.proposed,
      routeDoc.version,
      routeDoc.changeSetId,
      routeDoc.lastUpdated,
      routeDoc.lastSurvey,
      routeDoc.facts,
      locationCandidateInfos,
      routeDoc.unexpectedNodeIds,
      routeDoc.unexpectedRelationIds,
      routeDoc.segments,
      routeDoc.paths,
      routeDoc.nameDerivedFromNodes,
      routeDoc.nodes,
      routeDoc.bounds,
      routeDoc.routeIds,
      routeDoc.parentRoutes,
      networkReferences,
    )

    MonitorRouteDetailsPage(
      summary,
      monitorRoute.comment,
      monitorRoute.symbol,
      monitorRoute.analysisTimestamp,
      monitorRoute.analysisDuration,
      monitorRoute.referenceType,
      monitorRoute.referenceTimestamp,
      monitorRoute.referenceFilename,
      monitorRoute.referenceDistance,
      deviationDistance,
      happy,
      routeDoc.summary.wayCount,
      routeDoc.summary.meters,
      relationCount,
      relationLevels,
      details,
    )
  }

  private def convertRows(
    route: MonitorRoute,
    routeDoc: RouteDoc,
    references: Seq[MonitorReference],
    stateDeviationInfos: Seq[MonitorStateDeviationInfo]
  ): Seq[StructureRow] = {
    routeDoc.structureRows.map { row => toRow(row, route, 1, references, stateDeviationInfos) }
  }

  private def toRow(
    row: RouteStructureRow,
    route: MonitorRoute,
    level: Long,
    references: Seq[MonitorReference],
    stateDeviationInfos: Seq[MonitorStateDeviationInfo]
  ): StructureRow = {

    val reference = if (row.memberType == MemberType.Relation && route.referenceType == MonitorReferenceType.multiGpx) {
      references.find(_.relationId.contains(row.id))
    }
    else {
      None
    }

    val stateDeviationInfo = if (row.memberType == MemberType.Relation && route.referenceType == MonitorReferenceType.multiGpx) {
      stateDeviationInfos.find(_.relationId == row.id)
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
      rowNumber = row.rowNumber,
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
      deviationDistance = stateDeviationInfo.map(_.deviationDistance),
      deviationCount = stateDeviationInfo.map(_.deviationCount),
      osmSegmentCount = row.relation.map(_.segments.length),
      osmDistance = -1,
      osmDistanceSubRelations = -1,
      gaps = row.relation.flatMap(_.gaps),
      showMap = showMap,
      happy = false // TODO redesign - monitorRouteRelation.happy
    )
  }
}
