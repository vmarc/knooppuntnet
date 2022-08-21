package kpn.server.api.monitor

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorChangesPage
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorGroupChangesPage
import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorGroupProperties
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorRouteAdd
import kpn.api.common.monitor.MonitorRouteAddPage
import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.custom.ApiResponse
import kpn.server.api.CurrentUser
import kpn.server.api.monitor.domain.MonitorRoute
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

import scala.xml.XML

@RestController
@RequestMapping(Array("/api/monitor"))
class MonitorController(
  facade: MonitorFacade
) {

  @PostMapping(value = Array("changes"))
  def changes(
    @RequestBody parameters: MonitorChangesParameters
  ): ApiResponse[MonitorChangesPage] = {
    facade.changes(CurrentUser.name, parameters)
  }

  @GetMapping(value = Array("groups"))
  def groups(): ApiResponse[MonitorGroupsPage] = {
    facade.groups(CurrentUser.name)
  }

  @GetMapping(value = Array("group-names"))
  def groupNames(): ApiResponse[Seq[String]] = {
    facade.groupNames(CurrentUser.name)
  }

  @GetMapping(value = Array("groups/{groupName}"))
  def group(
    @PathVariable groupName: String
  ): ApiResponse[MonitorGroupPage] = {
    facade.group(CurrentUser.name, groupName)
  }

  @PostMapping(value = Array("groups"))
  def addGroup(
    @RequestBody properties: MonitorGroupProperties
  ): Unit = {
    facade.addGroup(CurrentUser.name, properties)
  }

  @PutMapping(value = Array("groups/{groupId}"))
  def updateGroup(
    @PathVariable groupId: String,
    @RequestBody properties: MonitorGroupProperties
  ): Unit = {
    facade.updateGroup(CurrentUser.name, ObjectId(groupId), properties)
  }

  @DeleteMapping(value = Array("groups/{groupId}"))
  def deleteGroup(
    @PathVariable groupId: String
  ): Unit = {
    facade.deleteGroup(CurrentUser.name, ObjectId(groupId))
  }

  @PostMapping(value = Array("groups/{groupName}/changes"))
  def groupChanges(
    @PathVariable groupName: String,
    @RequestBody parameters: MonitorChangesParameters
  ): ApiResponse[MonitorGroupChangesPage] = {
    facade.groupChanges(CurrentUser.name, groupName, parameters)
  }

  @GetMapping(value = Array("groups/{groupName}/routes/{routeName}"))
  def route(
    @PathVariable groupName: String,
    @PathVariable routeName: String
  ): ApiResponse[MonitorRouteDetailsPage] = {
    facade.route(CurrentUser.name, groupName, routeName)
  }

  @PostMapping(value = Array("groups/{groupName}"))
  def addRoute(
    @PathVariable groupName: String,
    @RequestBody add: MonitorRouteAdd
  ): Unit = {
    facade.addRoute(CurrentUser.name, groupName, add)
  }

  @PutMapping(value = Array("groups/{groupName}/routes/{routeName}"))
  def updateRoute(
    @PathVariable groupName: String, // TODO MON not used anymore, groupId included in MonitorRouteAdd (change url?)
    @RequestBody route: MonitorRoute
  ): Unit = {
    facade.updateRoute(CurrentUser.name, route)
  }

  @DeleteMapping(value = Array("groups/{groupName}/routes/{routeName}"))
  def deleteRoute(
    @PathVariable groupName: String,
    @PathVariable routeName: String
  ): Unit = {
    facade.deleteRoute(CurrentUser.name, groupName, routeName)
  }

  @GetMapping(value = Array("groups/{groupName}/routes/{routeName}/map"))
  def routeMap(
    @PathVariable groupName: String,
    @PathVariable routeName: String
  ): ApiResponse[MonitorRouteMapPage] = {
    facade.routeMap(CurrentUser.name, groupName, routeName)
  }

  @PostMapping(value = Array("groups/{groupName}/routes/{monitorRouteId}/changes"))
  def routeChanges(
    @PathVariable groupName: String,
    @PathVariable routeName: String,
    @RequestBody parameters: MonitorChangesParameters
  ): ApiResponse[MonitorRouteChangesPage] = {
    facade.routeChanges(CurrentUser.name, groupName + ":" + routeName, parameters)
  }

  @GetMapping(value = Array("routes/{monitorRouteId}/changes/{changeSetId}/{replicationNumber}"))
  def routeChange(
    @PathVariable monitorRouteId: Long,
    @PathVariable changeSetId: Long,
    @PathVariable replicationNumber: Long
  ): ApiResponse[MonitorRouteChangePage] = {
    facade.routeChange(CurrentUser.name, monitorRouteId, changeSetId, replicationNumber)
  }

  @GetMapping(value = Array("route-add/{groupName}"))
  def groupRouteAdd(
    @PathVariable groupName: String
  ): ApiResponse[MonitorRouteAddPage] = {
    facade.groupRouteAdd(CurrentUser.name, groupName)
  }

  @GetMapping(value = Array("route-info/{routeId}"))
  def routeInfo(
    @PathVariable routeId: Long
  ): ApiResponse[MonitorRouteInfoPage] = {
    facade.routeInfo(CurrentUser.name, routeId)
  }

  @PostMapping(value = Array("groups/{groupName}/routes/{routeName}/upload"))
  def uploadRoute(
    @PathVariable groupName: String,
    @PathVariable routeName: String,
    @RequestParam("file") file: MultipartFile
  ): ResponseEntity[String] = {
    try {
      val xml = XML.load(file.getInputStream)
      facade.processNewReference(CurrentUser.name, groupName, routeName, file.getOriginalFilename, xml)
      val message = "File successfully uploaded: " + file.getOriginalFilename
      ResponseEntity.status(HttpStatus.OK).body(message)
    } catch {
      case e: Exception =>

        e.printStackTrace()

        val message = "Could not upload the file: " + file.getOriginalFilename + "!"
        ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message)
    }
  }

  @GetMapping(value = Array("groups/{groupName}/route-names"))
  def routeNames(
    @PathVariable groupName: String,
  ): ApiResponse[Seq[String]] = {
    facade.routeNames(CurrentUser.name, groupName)
  }

}
