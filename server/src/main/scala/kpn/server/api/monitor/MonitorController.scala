package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorChangesPage
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorGroupChangesPage
import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.custom.ApiResponse
import kpn.server.api.CurrentUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Array("/api/monitor"))
class MonitorController(
  facade: MonitorFacade
) {

  @GetMapping(value = Array("groups"))
  def groups(): ApiResponse[MonitorGroupsPage] = {
    facade.groups(CurrentUser.name)
  }

  @GetMapping(value = Array("groups/{groupName}"))
  def group(
    @PathVariable groupName: String
  ): ApiResponse[MonitorGroupPage] = {
    facade.group(CurrentUser.name, groupName)
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

  @PostMapping(value = Array("changes"))
  def changes(
    @RequestBody parameters: MonitorChangesParameters
  ): ApiResponse[MonitorChangesPage] = {
    facade.changes(CurrentUser.name, parameters)
  }

  @PostMapping(value = Array("groups/{groupName}/changes"))
  def groupChanges(
    @PathVariable groupName: String,
    @RequestBody parameters: MonitorChangesParameters
  ): ApiResponse[MonitorGroupChangesPage] = {
    facade.groupChanges(CurrentUser.name, groupName, parameters)
  }

  @PostMapping(value = Array("routes/{routeId}/changes"))
  def routeChanges(
    @PathVariable routeId: Long,
    @RequestBody parameters: MonitorChangesParameters
  ): ApiResponse[MonitorRouteChangesPage] = {
    facade.routeChanges(CurrentUser.name, routeId, parameters)
  }

  @GetMapping(value = Array("routes/{routeId}/changes/{changeSetId}/{replicationNumber}"))
  def routeChange(
    @PathVariable routeId: Long,
    @PathVariable changeSetId: Long,
    @PathVariable replicationNumber: Long
  ): ApiResponse[MonitorRouteChangePage] = {
    facade.routeChange(CurrentUser.name, routeId, changeSetId, replicationNumber)
  }
}
