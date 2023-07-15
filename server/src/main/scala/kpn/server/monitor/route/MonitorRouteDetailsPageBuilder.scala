package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteRelationStructureRow
import kpn.server.config.RequestContext
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteDetailsPageBuilder(
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository
) {

  def build(groupName: String, routeName: String): Option[MonitorRouteDetailsPage] = {
    val admin = monitorRepository.isAdminUser(RequestContext.user)
    monitorGroupRepository.groupByName(groupName).flatMap { group =>
      monitorRouteRepository.routeByName(group._id, routeName).map { route =>
        val structureRows = flattenRelationTree(route.relation)
        MonitorRouteDetailsPage(
          admin,
          group.name,
          group.description,
          route.name,
          route.description,
          route.relationId,
          route.comment,
          route.symbol,
          route.referenceType,
          route.referenceTimestamp,
          route.referenceFilename,
          route.referenceDistance,
          route.deviationDistance,
          route.deviationCount,
          route.osmSegmentCount,
          route.happy,
          route.osmWayCount,
          route.osmDistance,
          structureRows
        )
      }
    }
  }

  private def flattenRelationTree(relation: Option[MonitorRouteRelation]): Option[Seq[MonitorRouteRelationStructureRow]] = {
    relation match {
      case None => None
      case Some(relationLevel1) =>
        val rowsLevel2 = relationLevel1.relations.flatMap { relationLevel2 =>
          val rowsLevel3 = relationLevel2.relations.flatMap { relationLevel3 =>
            val rowsLevel4 = relationLevel3.relations.flatMap { relationLevel4 =>
              val rowsLevel5 = relationLevel4.relations.map { relationLevel5 =>
                toRow(5, relationLevel5)
              }
              toRow(4, relationLevel4) +: rowsLevel5
            }
            toRow(3, relationLevel3) +: rowsLevel4
          }
          toRow(2, relationLevel2) +: rowsLevel3
        }
        if (rowsLevel2.nonEmpty) {
          Some(
            toRow(1, relationLevel1) +: rowsLevel2
          )
        }
        else {
          None
        }
    }
  }

  private def toRow(level: Long, monitorRouteRelation: MonitorRouteRelation): MonitorRouteRelationStructureRow = {
    val physical = monitorRouteRelation.osmDistance > 0 || monitorRouteRelation.referenceFilename.isDefined
    MonitorRouteRelationStructureRow(
      level = level,
      physical = physical,
      name = monitorRouteRelation.name,
      relationId = monitorRouteRelation.relationId,
      role = monitorRouteRelation.role,
      survey = monitorRouteRelation.survey,
      symbol = monitorRouteRelation.symbol,
      referenceTimestamp = monitorRouteRelation.referenceTimestamp,
      referenceFilename = monitorRouteRelation.referenceFilename,
      referenceDistance = monitorRouteRelation.referenceDistance,
      deviationDistance = monitorRouteRelation.deviationDistance,
      deviationCount = monitorRouteRelation.deviationCount,
      osmWayCount = monitorRouteRelation.osmWayCount,
      osmSegmentCount = monitorRouteRelation.osmSegmentCount,
      osmDistance = monitorRouteRelation.osmDistance,
      osmDistanceSubRelations = monitorRouteRelation.osmDistanceSubRelations,
      happy = monitorRouteRelation.happy
    )
  }
}
