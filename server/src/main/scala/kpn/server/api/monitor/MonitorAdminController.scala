package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorAdminRouteGroupPage
import kpn.api.common.monitor.MonitorRouteGroup
import kpn.api.common.monitor.RouteGroupsPage
import kpn.api.custom.ApiResponse
import kpn.server.api.CurrentUser
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Array("/admin-api/monitor"))
class MonitorAdminController(facade: MonitorAdminFacade) {

  @GetMapping(value = Array("groups"))
  def groups(): ApiResponse[RouteGroupsPage] = {
    facade.groups(CurrentUser.name)
  }

  @GetMapping(value = Array("groups/{groupName}"))
  def group(@PathVariable groupName: String): ApiResponse[MonitorAdminRouteGroupPage] = {
    facade.group(CurrentUser.name, groupName)
  }

  @PostMapping(value = Array("groups"))
  def addGroup(@RequestBody group: MonitorRouteGroup): Unit = {
    facade.addGroup(CurrentUser.name, group)
  }

  @PutMapping(value = Array("groups/{groupName}"))
  def updateGroup(@RequestBody group: MonitorRouteGroup): Unit = {
    facade.updateGroup(CurrentUser.name, group)
  }

  @DeleteMapping(value = Array("groups/{groupName}"))
  def deleteGroup(@PathVariable groupName: String): Unit = {
    facade.deleteGroup(CurrentUser.name, groupName)
  }

}
