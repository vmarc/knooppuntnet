package kpn.core.tools.monitor.support

import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelationTopLevel
import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import kpn.server.monitor.MonitorUtil.subRelationsIn
import kpn.server.monitor.domain.MonitorGroup

import scala.xml.XML

object MonitorRouteReportRoleUsageTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-monitor") { database =>
      val groupRepository = new MonitorGroupRepositoryImpl(database)
      val routeRepository = new MonitorRouteRepositoryImpl(database)
      val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl
      val tool = new MonitorRouteReportRoleUsageTool(groupRepository, routeRepository, overpassQueryExecutor)
      tool.report()
    }
  }
}

class MonitorRouteReportRoleUsageTool(
  groupRepository: MonitorGroupRepository,
  routeRepository: MonitorRouteRepository,
  overpassQueryExecutor: OverpassQueryExecutor
) {
  def report(): Unit = {
    groupRepository.groups().sortBy(_.name).foreach { group =>
      println(s"group=${group.name}")
      groupRepository.groupRoutes(group._id).sortBy(_.name).foreach { route =>
        println(s"group=${group.name}, route=${route.name}")
        route.relationId match {
          case None =>
          case Some(relationId) =>
            reportRoles(group, route, relationId)
            subRelationsIn(route).foreach { monitorRouteSubRelation =>
              reportRoles(group, route, monitorRouteSubRelation.relationId)
            }
        }
      }
    }
  }

  private def reportRoles(group: MonitorGroup, route: MonitorRoute, relationId: Long): Unit = {
    val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelationTopLevel(relationId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    rawData.relationWithId(relationId) match {
      case None =>
      case Some(relation) =>
        val nodeRoles = relation.members.filter(_.isNode).flatMap(_.role).distinct
        if (nodeRoles.nonEmpty) {
          println(s"group=${group.name}, route=${route.name}, relation=$relationId, nodeRoles=$nodeRoles")
        }
        val wayRoles = relation.members.filter(_.isWay).flatMap(_.role).distinct
        if (wayRoles.nonEmpty) {
          println(s"group=${group.name}, route=${route.name}, relation=$relationId, wayRoles=$wayRoles")
        }
        val relationRoles = relation.members.filter(_.isRelation).flatMap(_.role).distinct
        if (relationRoles.nonEmpty) {
          println(s"group=${group.name}, route=${route.name}, relation=$relationId, relationRoles=$relationRoles")
        }
    }
  }
}
