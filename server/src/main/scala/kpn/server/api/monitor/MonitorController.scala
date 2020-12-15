package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRoutesPage
import kpn.api.common.monitor.RouteGroupChangesPage
import kpn.api.common.monitor.RouteGroupDetailsPage
import kpn.api.common.monitor.RouteGroupsPage
import kpn.api.custom.ApiResponse
import kpn.server.api.CurrentUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MonitorController(facade: MonitorFacade) {

  @GetMapping(value = Array("/api/monitor/groups"))
  def groups(): ApiResponse[RouteGroupsPage] = {
    facade.groups(CurrentUser.name)
  }

  @GetMapping(value = Array("/api/monitor/groups/{groupName}"))
  def group(
    @PathVariable groupName: String
  ): ApiResponse[RouteGroupDetailsPage] = {
    facade.group(CurrentUser.name, groupName)
  }

  @GetMapping(value = Array("/api/monitor/groups/{groupName}/changes"))
  def groupChanges(
    @PathVariable groupName: String
  ): ApiResponse[RouteGroupChangesPage] = {
    facade.groupChanges(CurrentUser.name, groupName)
  }

  @GetMapping(value = Array("/api/monitor/routes"))
  def routes(): ApiResponse[MonitorRoutesPage] = {
    facade.routes(CurrentUser.name)
  }

  @GetMapping(value = Array("/api/monitor/routes/{routeId}"))
  def route(
    @PathVariable routeId: Long
  ): ApiResponse[MonitorRouteDetailsPage] = {
    facade.route(CurrentUser.name, routeId)
  }

  @GetMapping(value = Array("/api/monitor/routes/{routeId}/map"))
  def routeMap(
    @PathVariable routeId: Long
  ): ApiResponse[MonitorRouteMapPage] = {
    facade.routeMap(CurrentUser.name, routeId)
  }

  @GetMapping(value = Array("/api/monitor/routes/{routeId}/changes"))
  def routeChanges(
    @PathVariable routeId: Long
  ): ApiResponse[MonitorRouteChangesPage] = {
    facade.routeChanges(CurrentUser.name, routeId)
  }

  @GetMapping(value = Array("/api/monitor/routes/{routeId}/changes/{changeSetId}"))
  def routeChange(
    @PathVariable routeId: Long,
    @PathVariable changeSetId: Long
  ): ApiResponse[MonitorRouteChangePage] = {
    facade.routeChange(CurrentUser.name, routeId, changeSetId)
  }
}
