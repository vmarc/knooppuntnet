package kpn.server.api.monitor

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.EN
import kpn.api.common.monitor.MonitorChangesPage
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorGroupChangesPage
import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorGroupProperties
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorRouteAddPage
import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteUpdatePage
import kpn.api.custom.ApiResponse
import kpn.core.common.Time
import kpn.core.common.TimestampLocal
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalyzer
import kpn.server.api.Api
import kpn.server.api.monitor.domain.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.group.MonitorGroupNamesBuilder
import kpn.server.api.monitor.group.MonitorGroupPageBuilder
import kpn.server.api.monitor.group.MonitorGroupsPageBuilder
import kpn.server.api.monitor.route.MonitorRouteChangePageBuilder
import kpn.server.api.monitor.route.MonitorRouteChangesPageBuilder
import kpn.server.api.monitor.route.MonitorRouteDetailsPageBuilder
import kpn.server.api.monitor.route.MonitorRouteInfoBuilder
import kpn.server.api.monitor.route.MonitorRouteMapPageBuilder
import kpn.server.api.monitor.route.MonitorRouteUpdatePageBuilder
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component

import scala.xml.Elem

@Component
class MonitorFacadeImpl(
  api: Api,
  monitorGroupsPageBuilder: MonitorGroupsPageBuilder,
  monitorGroupNamesBuilder: MonitorGroupNamesBuilder,
  monitorGroupPageBuilder: MonitorGroupPageBuilder,
  monitorRouteUpdatePageBuilder: MonitorRouteUpdatePageBuilder,
  monitorRouteDetailsPageBuilder: MonitorRouteDetailsPageBuilder,
  monitorRouteMapPageBuilder: MonitorRouteMapPageBuilder,
  monitorRouteChangesPageBuilder: MonitorRouteChangesPageBuilder,
  monitorRouteChangePageBuilder: MonitorRouteChangePageBuilder,
  monitorRouteInfoBuilder: MonitorRouteInfoBuilder,
  monitorRepository: MonitorRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
  monitorRouteAnalyzer: MonitorRouteAnalyzer
) extends MonitorFacade {

  override def changes(user: Option[String], parameters: MonitorChangesParameters): ApiResponse[MonitorChangesPage] = {
    api.execute(user, "monitor-changes", "") {
      reply(monitorRouteChangesPageBuilder.changes(parameters))
    }
  }

  override def groups(user: Option[String]): ApiResponse[MonitorGroupsPage] = {
    api.execute(user, "monitor-groups", "") {
      reply(monitorGroupsPageBuilder.build(user))
    }
  }

  override def groupNames(user: Option[String]): ApiResponse[Seq[String]] = {
    api.execute(user, "monitor-group-names", "") {
      reply(Some(monitorGroupNamesBuilder.build()))
    }
  }

  override def group(user: Option[String], groupName: String): ApiResponse[MonitorGroupPage] = {
    api.execute(user, "monitor-group", "") {
      reply(monitorGroupPageBuilder.build(user, groupName))
    }
  }

  override def addGroup(user: Option[String], properties: MonitorGroupProperties): Unit = {
    api.execute(user, "monitor-add-group", properties.name) {
      assertAdminUser(user)
      monitorGroupRepository.saveGroup(MonitorGroup.from(properties))
    }
  }

  override def updateGroup(user: Option[String], id: ObjectId, properties: MonitorGroupProperties): Unit = {
    api.execute(user, "monitor-update-group", properties.name) {
      assertAdminUser(user)
      monitorGroupRepository.saveGroup(MonitorGroup.from(id, properties))
    }
  }

  override def deleteGroup(user: Option[String], groupId: ObjectId): Unit = {
    api.execute(user, "monitor-delete-group", groupId.oid) {
      assertAdminUser(user)
      monitorGroupRepository.deleteGroup(groupId)
    }
  }

  override def groupChanges(user: Option[String], groupName: String, parameters: MonitorChangesParameters): ApiResponse[MonitorGroupChangesPage] = {
    api.execute(user, "monitor-group-changes", "") {
      reply(monitorRouteChangesPageBuilder.groupChanges(groupName, parameters))
    }
  }

  override def route(user: Option[String], groupName: String, routeName: String): ApiResponse[MonitorRouteDetailsPage] = {
    val args = s"groupName=$groupName, routeName=$routeName"
    api.execute(user, "monitor-route", args) {
      reply(monitorRouteDetailsPageBuilder.build(groupName, routeName))
    }
  }

  override def routeMap(user: Option[String], groupName: String, routeName: String): ApiResponse[MonitorRouteMapPage] = {
    val args = s"$groupName:$routeName"
    api.execute(user, "monitor-route-map", args) {
      reply(monitorRouteMapPageBuilder.build(EN, groupName, routeName))
    }
  }

  override def routeChanges(user: Option[String], monitorRouteId: String, parameters: MonitorChangesParameters): ApiResponse[MonitorRouteChangesPage] = {
    val args = s"monitorRouteId=$monitorRouteId"
    api.execute(user, "monitor-route-changes", args) {
      reply(monitorRouteChangesPageBuilder.routeChanges(monitorRouteId, parameters))
    }
  }

  override def routeChange(user: Option[String], routeId: Long, changeSetId: Long, replicationId: Long): ApiResponse[MonitorRouteChangePage] = {
    val args = s"routeId=$routeId, changeSetId$changeSetId"
    api.execute(user, "monitor-route-change", args) {
      reply(monitorRouteChangePageBuilder.build(routeId, changeSetId, replicationId))
    }
  }

  override def routeInfo(user: Option[String], routeId: Long): ApiResponse[MonitorRouteInfoPage] = {
    api.execute(user, "monitor-route-info", routeId.toString) {
      assertAdminUser(user)
      reply(
        Some(
          monitorRouteInfoBuilder.build(routeId)
        )
      )
    }
  }

  override def groupRouteAdd(user: Option[String], groupName: String): ApiResponse[MonitorRouteAddPage] = {
    api.execute(user, "monitor-group-route-add", groupName) {
      assertAdminUser(user)
      reply(
        monitorGroupRepository.groupByName(groupName).map { monitorGroup =>
          MonitorRouteAddPage(
            monitorGroup._id.oid,
            monitorGroup.name,
            monitorGroup.description
          )
        }
      )
    }
  }

  override def routeUpdatePage(user: Option[String], groupName: String, routeName: String): ApiResponse[MonitorRouteUpdatePage] = {
    api.execute(user, "monitor-route-update-page", s"$groupName:$routeName") {
      assertAdminUser(user)
      reply(monitorRouteUpdatePageBuilder.build(groupName, routeName))
    }
  }

  override def addRoute(user: Option[String], groupName: String, properties: MonitorRouteProperties): Unit = {
    api.execute(user, "monitor-add-route", properties.name) {
      assertAdminUser(user)
      val relationId = properties.relationId.map(_.toLong)
      monitorGroupRepository.groupByName(groupName) match {
        case None => throw new IllegalArgumentException(s"""Could not find group with name "${groupName}"""")
        case Some(monitorGroup) =>
          monitorRouteRepository.routeByName(monitorGroup._id, properties.name) match {
            case Some(route) =>
              throw new IllegalArgumentException(s"""Could not add route with name "${properties.name}": already exists in group with name "${groupName}"""")
            case None =>
              val route = MonitorRoute(
                ObjectId(),
                monitorGroup._id,
                properties.name,
                properties.description,
                relationId,
              )
              monitorRouteRepository.saveRoute(route)

              if (properties.referenceType == "osm") {
                // properties.referenceTimestamp: Option[String]

                val reference = MonitorRouteReference(
                  ObjectId(),
                  routeId = route._id,
                  relationId = properties.relationId.map(_.toLong),
                  key = "", // YYYYMMDDHHMMSS derived from created Timestamp  TODO MON replace with ObjectId!?
                  created = Time.now,
                  user = user.get,
                  bounds = Bounds(), // TODO MON calculate bounds for relation id
                  referenceType = "osm",
                  referenceTimestamp = None, // TODO MON properties.referenceTimestamp,  String or Timestamp?
                  segmentCount = 0, // TODO MON
                  filename = None,
                  geometry = "TODO MON" // osm | gpx
                )
                monitorRouteRepository.saveRouteReference(reference)
                monitorRouteAnalyzer.analyze(route, reference)
              }
              else if (properties.referenceType == "gpx") {
                // the route reference will be created at the time the gpxfile is uploaded
                // the route analysis will be done in a separate call, after the gpx file has been uploaded
              }
          }
      }
    }
  }

  override def analyzeRoute(user: Option[String], groupName: String, routeName: String): Unit = {
    api.execute(user, "monitor-analyze-route", s"$groupName:$routeName") {
      assertAdminUser(user)
      monitorGroupRepository.groupByName(groupName) match {
        case None => throw new IllegalArgumentException(s"""Could not find group with name "$groupName"""")
        case Some(monitorGroup) =>
          monitorRouteRepository.routeByName(monitorGroup._id, routeName) match {
            case None =>
              throw new IllegalArgumentException(s"""Could not analyze route with name "$routeName": already exists in group with name "$groupName"""")
            case Some(route) =>
              monitorRouteRepository.currentRouteReference(route._id) match {
                case None =>
                  throw new IllegalArgumentException(s"""Could not find reference for route "$groupName:$routeName""""")
                case Some(reference) => monitorRouteAnalyzer.analyze(route, reference)
              }
          }
      }
    }
  }

  override def updateRoute(user: Option[String], route: MonitorRoute): Unit = {
    api.execute(user, "monitor-update-route", route.name) {
      assertAdminUser(user)
      monitorRouteRepository.saveRoute(route)
    }
  }

  override def deleteRoute(user: Option[String], groupName: String, routeName: String): Unit = {
    api.execute(user, "monitor-delete-route", s"$groupName:$routeName") {
      assertAdminUser(user)
      monitorGroupRepository.groupByName(groupName).foreach { group =>
        monitorRouteRepository.routeByName(group._id, routeName).foreach { route =>
          monitorRouteRepository.deleteRoute(route._id)
        }
      }
    }
  }

  override def processNewReference(user: Option[String], groupName: String, routeName: String, filename: String, xml: Elem): ApiResponse[String] = {
    api.execute(user, "monitor-route-reference", s"$groupName:$routeName") {
      assertAdminUser(user)
      reply(
        monitorGroupRepository.groupByName(groupName).flatMap { group =>
          monitorRouteRepository.routeByName(group._id, routeName).map { route =>
            monitorRouteAnalyzer.processNewReference(user.get, route, filename, xml)
          }
        }
      )
    }
  }

  override def routeNames(user: Option[String], groupName: String): ApiResponse[Seq[String]] = {
    api.execute(user, "monitor-group-route-names", groupName) {
      reply(
        monitorGroupRepository.groupByName(groupName).map { group =>
          monitorRouteRepository.routeNames(group._id)
        }
      )
    }
  }

  private def assertAdminUser(user: Option[String]): Unit = {
    if (!monitorRepository.isAdminUser(user)) {
      throw new AccessDeniedException("403 returned")
    }
  }

  private def reply[T](result: Option[T]): ApiResponse[T] = {
    val response = ApiResponse(null, 1, result)
    TimestampLocal.localize(response)
    response
  }
}
