package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorAdminGroupPage
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorRouteAdd
import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.api.custom.ApiResponse
import kpn.server.api.CurrentUser
import kpn.server.api.monitor.domain.MonitorRoute
import org.apache.commons.io.IOUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

import java.nio.charset.StandardCharsets

@RestController
@RequestMapping(Array("/admin-api/monitor"))
class MonitorAdminController(facade: MonitorAdminFacade) {

  @GetMapping(value = Array("groups"))
  def groups(): ApiResponse[MonitorGroupsPage] = {
    facade.groups(CurrentUser.name)
  }

  @GetMapping(value = Array("groups/{groupName}"))
  def group(@PathVariable groupName: String): ApiResponse[MonitorAdminGroupPage] = {
    facade.group(CurrentUser.name, groupName)
  }

  @PostMapping(value = Array("groups"))
  def addGroup(@RequestBody group: MonitorGroup): Unit = {
    facade.addGroup(CurrentUser.name, group)
  }

  @PutMapping(value = Array("groups/{groupName}"))
  def updateGroup(@RequestBody group: MonitorGroup): Unit = {
    facade.updateGroup(CurrentUser.name, group)
  }

  @DeleteMapping(value = Array("groups/{groupName}"))
  def deleteGroup(@PathVariable groupName: String): Unit = {
    facade.deleteGroup(CurrentUser.name, groupName)
  }

  @PostMapping(value = Array("groups/{groupName}"))
  def addRoute(@PathVariable groupName: String, @RequestBody add: MonitorRouteAdd): Unit = {
    facade.addRoute(CurrentUser.name, groupName, add)
  }

  @PutMapping(value = Array("groups/{groupName}/routes/{routeName}"))
  def updateRoute(@PathVariable groupName: String, @RequestBody route: MonitorRoute): Unit = {
    facade.updateRoute(CurrentUser.name, groupName, route)
  }

  @DeleteMapping(value = Array("groups/{groupName}/routes/{routeName}"))
  def deleteRoute(@PathVariable groupName: String, @PathVariable routeName: String): Unit = {
    facade.deleteRoute(CurrentUser.name, groupName, routeName)
  }

  @GetMapping(value = Array("route-info/{routeId}"))
  def routeInfo(@PathVariable routeId: Long): ApiResponse[MonitorRouteInfoPage] = {
    facade.routeInfo(CurrentUser.name, routeId)
  }

  @PostMapping(value = Array("groups/{groupName}/routes/{routeName}/upload"))
  def uploadRoute(@PathVariable groupName: String, @PathVariable routeName: String, @RequestParam("file") file: MultipartFile): ResponseEntity[String] = {
    try {
      val text = IOUtils.toString(file.getInputStream, StandardCharsets.UTF_8.name)
      println("group=" + groupName + ", route=" + routeName)
      println(text.take(500))
      val message = "File successfully uploaded: " + file.getOriginalFilename
      ResponseEntity.status(HttpStatus.OK).body(message)
    } catch {
      case e: Exception =>
        println(e)
        val message = "Could not upload the file: " + file.getOriginalFilename + "!"
        ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message)
    }
  }
}
