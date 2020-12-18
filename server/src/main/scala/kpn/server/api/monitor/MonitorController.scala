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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Array("/api/monitor"))
class MonitorController(facade: MonitorFacade) {

  @GetMapping(value = Array("groups"))
  def groups(): ApiResponse[RouteGroupsPage] = {
    facade.groups(CurrentUser.name)
  }

  @GetMapping(value = Array("groups/{groupName}"))
  def group(
    @PathVariable groupName: String
  ): ApiResponse[RouteGroupDetailsPage] = {
    facade.group(CurrentUser.name, groupName)
  }

  @GetMapping(value = Array("groups/{groupName}/changes"))
  def groupChanges(
    @PathVariable groupName: String
  ): ApiResponse[RouteGroupChangesPage] = {
    facade.groupChanges(CurrentUser.name, groupName)
  }

  @GetMapping(value = Array("routes"))
  def routes(): ApiResponse[MonitorRoutesPage] = {
    facade.routes(CurrentUser.name)
  }

  @GetMapping(value = Array("routes/{routeId}"))
  def route(
    @PathVariable routeId: Long
  ): ApiResponse[MonitorRouteDetailsPage] = {
    facade.route(CurrentUser.name, routeId)
  }

  @GetMapping(value = Array("routes/{routeId}/map"))
  def routeMap(
    @PathVariable routeId: Long
  ): ApiResponse[MonitorRouteMapPage] = {
    facade.routeMap(CurrentUser.name, routeId)
  }

  @GetMapping(value = Array("routes/{routeId}/changes"))
  def routeChanges(
    @PathVariable routeId: Long
  ): ApiResponse[MonitorRouteChangesPage] = {
    facade.routeChanges(CurrentUser.name, routeId)
  }

  @GetMapping(value = Array("routes/{routeId}/changes/{changeSetId}"))
  def routeChange(
    @PathVariable routeId: Long,
    @PathVariable changeSetId: Long
  ): ApiResponse[MonitorRouteChangePage] = {
    facade.routeChange(CurrentUser.name, routeId, changeSetId)
  }
}
