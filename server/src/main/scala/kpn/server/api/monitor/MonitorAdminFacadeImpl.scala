package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorAdminGroupPage
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.monitor.MonitorGroupDetail
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorRouteAdd
import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.api.custom.ApiResponse
import kpn.core.common.TimestampLocal
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelationOnly
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalyzer
import kpn.server.api.Api
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component

import scala.xml.Elem
import scala.xml.XML

@Component
class MonitorAdminFacadeImpl(
  api: Api,
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  overpassQueryExecutor: OverpassQueryExecutor,
  monitorRouteAnalyzer: MonitorRouteAnalyzer
) extends MonitorAdminFacade {

  override def groups(user: Option[String]): ApiResponse[MonitorGroupsPage] = {
    api.execute(user, "admin-groups", "") {
      val admin = monitorRepository.isAdminUser(user)
      val groups = monitorGroupRepository.groups()
      reply(
        Some(
          MonitorGroupsPage(
            admin,
            groups.map { group =>
              MonitorGroupDetail(
                group.name,
                group.description
              )
            }
          )
        )
      )
    }
  }

  override def group(user: Option[String], groupName: String): ApiResponse[MonitorAdminGroupPage] = {
    api.execute(user, "admin-group", "") {
      val admin = monitorRepository.isAdminUser(user)
      reply(
        monitorGroupRepository.group(groupName).map { group =>
          MonitorAdminGroupPage(
            admin,
            group.name,
            group.description
          )
        }
      )
    }
  }

  override def addGroup(user: Option[String], group: MonitorGroup): Unit = {
    api.execute(user, "admin-add-group", group.name) {
      assertAdminUser(user)
      monitorGroupRepository.saveGroup(group)
    }
  }

  override def updateGroup(user: Option[String], group: MonitorGroup): Unit = {
    api.execute(user, "admin-update-group", group.name) {
      assertAdminUser(user)
      monitorGroupRepository.saveGroup(group)
    }
  }

  override def deleteGroup(user: Option[String], groupName: String): Unit = {
    api.execute(user, "admin-delete-group", groupName) {
      assertAdminUser(user)
      monitorGroupRepository.deleteGroup(groupName)
    }
  }

  override def routeInfo(user: Option[String], routeId: Long): ApiResponse[MonitorRouteInfoPage] = {
    api.execute(user, "admin-route-info", routeId.toString) {
      assertAdminUser(user)

      val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelationOnly(routeId))
      val xml = XML.loadString(xmlString)
      val rawData = new Parser().parse(xml.head)

      val result = rawData.relationWithId(routeId) match {
        case None =>
          MonitorRouteInfoPage(
            routeId,
            None,
            None,
            None,
            0,
            0,
            0
          )

        case Some(relation) =>
          val routeName: Option[String] = relation.tags("name")
          val operator: Option[String] = relation.tags("operator")
          val ref: Option[String] = relation.tags("ref")
          val nodeCount: Long = relation.nodeMembers.size
          val wayCount: Long = relation.wayMembers.size
          val relationCount: Long = relation.relationMembers.size
          MonitorRouteInfoPage(
            routeId,
            routeName,
            operator,
            ref,
            nodeCount,
            wayCount,
            relationCount,
          )
      }

      reply(Some(result))
    }
  }

  override def addRoute(user: Option[String], groupName: String, add: MonitorRouteAdd): Unit = {
    api.execute(user, "admin-add-route", add.name) {
      assertAdminUser(user)
      val route = MonitorRoute(
        _id = groupName + ":" + add.name,
        groupName = groupName,
        name = add.name,
        description = add.description,
        routeId = add.routeId,
      )
      monitorRouteRepository.saveRoute(route)
    }
  }

  override def updateRoute(user: Option[String], groupName: String, route: MonitorRoute): Unit = {
    api.execute(user, "admin-update-route", route.name) {
      assertAdminUser(user)
      monitorRouteRepository.saveRoute(route)
    }
  }

  override def deleteRoute(user: Option[String], groupName: String, routeName: String): Unit = {
    val routeDocId = groupName + ":" + routeName
    api.execute(user, "admin-delete-route", routeDocId) {
      assertAdminUser(user)
      monitorRouteRepository.deleteRoute(routeDocId)
    }
  }

  override def processNewReference(user: Option[String], groupName: String, routeName: String, filename: String, xml: Elem): Unit = {
    val routeDocId = groupName + ":" + routeName
    api.execute(user, "admin-route-reference", routeDocId) {
      assertAdminUser(user)
      monitorRouteAnalyzer.processNewReference(user.get, routeDocId, filename, xml)
    }
  }

  private def assertAdminUser(user: Option[String]): Unit = {
    if (!monitorRepository.isAdminUser(user)) {
      throw new AccessDeniedException("403 returned");
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(null, 1, result)
    TimestampLocal.localize(response)
    response
  }
}
