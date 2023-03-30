package kpn.server.monitor

import kpn.api.base.ObjectId
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
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.common.monitor.MonitorRouteUpdatePage
import kpn.api.custom.ApiResponse
import kpn.core.common.Time
import kpn.server.api.CurrentUser
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

import javax.servlet.http.HttpServletRequest
import scala.xml.XML

@RestController
@RequestMapping(Array("/api/monitor"))
class MonitorController(
  facade: MonitorFacade
) {

  @PostMapping(value = Array("changes"))
  def changes(
    request: HttpServletRequest,
    @RequestBody parameters: MonitorChangesParameters
  ): ApiResponse[MonitorChangesPage] = {
    facade.changes(request, CurrentUser.name, parameters)
  }

  @GetMapping(value = Array("groups"))
  def groups(request: HttpServletRequest): ApiResponse[MonitorGroupsPage] = {
    facade.groups(request, CurrentUser.name)
  }

  @GetMapping(value = Array("group-names"))
  def groupNames(request: HttpServletRequest): ApiResponse[Seq[String]] = {
    facade.groupNames(request, CurrentUser.name)
  }

  @GetMapping(value = Array("groups/{groupName}"))
  def group(
    request: HttpServletRequest,
    @PathVariable groupName: String
  ): ApiResponse[MonitorGroupPage] = {
    facade.group(request, CurrentUser.name, groupName)
  }

  @PostMapping(value = Array("groups"))
  def groupAdd(
    request: HttpServletRequest,
    @RequestBody properties: MonitorGroupProperties
  ): Unit = {
    facade.groupAdd(request, CurrentUser.name, properties)
  }

  @PutMapping(value = Array("groups/{groupId}"))
  def groupUpdate(
    request: HttpServletRequest,
    @PathVariable groupId: String,
    @RequestBody properties: MonitorGroupProperties
  ): Unit = {
    facade.groupUpdate(request, CurrentUser.name, ObjectId(groupId), properties)
  }

  @DeleteMapping(value = Array("groups/{groupId}"))
  def groupDelete(
    request: HttpServletRequest,
    @PathVariable groupId: String
  ): Unit = {
    facade.groupDelete(request, CurrentUser.name, ObjectId(groupId))
  }

  @PostMapping(value = Array("groups/{groupName}/changes"))
  def groupChanges(
    request: HttpServletRequest,
    @PathVariable groupName: String,
    @RequestBody parameters: MonitorChangesParameters
  ): ApiResponse[MonitorGroupChangesPage] = {
    facade.groupChanges(request, CurrentUser.name, groupName, parameters)
  }

  @GetMapping(value = Array("groups/{groupName}/routes/{routeName}"))
  def route(
    request: HttpServletRequest,
    @PathVariable groupName: String,
    @PathVariable routeName: String
  ): ApiResponse[MonitorRouteDetailsPage] = {
    facade.route(request, CurrentUser.name, groupName, routeName)
  }

  @PostMapping(value = Array("groups/{groupName}"))
  def routeAdd(
    request: HttpServletRequest,
    @PathVariable groupName: String,
    @RequestBody properties: MonitorRouteProperties
  ): ApiResponse[MonitorRouteSaveResult] = {
    facade.routeAdd(request, CurrentUser.name, groupName, properties)
  }

  @PutMapping(value = Array("groups/{groupName}/routes/{routeName}"))
  def routeUpdate(
    request: HttpServletRequest,
    @PathVariable groupName: String,
    @PathVariable routeName: String,
    @RequestBody properties: MonitorRouteProperties
  ): ApiResponse[MonitorRouteSaveResult] = {
    facade.routeUpdate(request, CurrentUser.name, groupName, routeName, properties)
  }

  @DeleteMapping(value = Array("groups/{groupName}/routes/{routeName}"))
  def routeDelete(
    request: HttpServletRequest,
    @PathVariable groupName: String,
    @PathVariable routeName: String
  ): Unit = {
    facade.routeDelete(request, CurrentUser.name, groupName, routeName)
  }

  @GetMapping(value = Array("groups/{groupName}/routes/{routeName}/map"))
  def routeMap(
    request: HttpServletRequest,
    @PathVariable groupName: String,
    @PathVariable routeName: String,
  ): ApiResponse[MonitorRouteMapPage] = {
    facade.routeMap(request, CurrentUser.name, groupName, routeName, None)
  }

  @GetMapping(value = Array("groups/{groupName}/routes/{routeName}/map/{relationId}"))
  def routeRelationMap(
    request: HttpServletRequest,
    @PathVariable groupName: String,
    @PathVariable routeName: String,
    @PathVariable relationId: Long,
  ): ApiResponse[MonitorRouteMapPage] = {
    facade.routeMap(request, CurrentUser.name, groupName, routeName, Some(relationId))
  }

  @PostMapping(value = Array("groups/{groupName}/routes/{monitorRouteId}/changes"))
  def routeChanges(
    request: HttpServletRequest,
    @PathVariable groupName: String,
    @PathVariable routeName: String,
    @RequestBody parameters: MonitorChangesParameters
  ): ApiResponse[MonitorRouteChangesPage] = {
    facade.routeChanges(request, CurrentUser.name, groupName + ":" + routeName, parameters)
  }

  @GetMapping(value = Array("routes/{monitorRouteId}/changes/{changeSetId}/{replicationNumber}"))
  def routeChange(
    request: HttpServletRequest,
    @PathVariable monitorRouteId: Long,
    @PathVariable changeSetId: Long,
    @PathVariable replicationNumber: Long
  ): ApiResponse[MonitorRouteChangePage] = {
    facade.routeChange(request, CurrentUser.name, monitorRouteId, changeSetId, replicationNumber)
  }

  @GetMapping(value = Array("route-add/{groupName}"))
  def routeAddPage(
    request: HttpServletRequest,
    @PathVariable groupName: String
  ): ApiResponse[MonitorRouteAddPage] = {
    facade.groupRouteAdd(request, CurrentUser.name, groupName)
  }

  @GetMapping(value = Array("groups/{groupName}/routes/{routeName}/update-info"))
  def routeUpdatePage(
    request: HttpServletRequest,
    @PathVariable groupName: String,
    @PathVariable routeName: String,
  ): ApiResponse[MonitorRouteUpdatePage] = {
    facade.routeUpdatePage(request, CurrentUser.name, groupName, routeName)
  }

  @GetMapping(value = Array("route-info/{routeId}"))
  def routeInfo(
    request: HttpServletRequest,
    @PathVariable routeId: Long
  ): ApiResponse[MonitorRouteInfoPage] = {
    facade.routeInfo(request, CurrentUser.name, routeId)
  }

  @PostMapping(value = Array(s"groups/{groupName}/routes/{routeName}/upload/{relationId}"))
  def routeReferenceGpxFileUpload(
    request: HttpServletRequest,
    @PathVariable groupName: String,
    @PathVariable routeName: String,
    @PathVariable relationId: Long,
    @RequestParam("file") file: MultipartFile
  ): ApiResponse[MonitorRouteSaveResult] = {

    val xml = XML.load(file.getInputStream)

    facade.upload(
      request,
      CurrentUser.name,
      groupName,
      routeName,
      relationId,
      Time.now.toDay, // TODO should get reference day from client !!!
      file.getOriginalFilename,
      xml
    )
  }

  @GetMapping(value = Array("groups/{groupName}/route-names"))
  def routeNames(
    request: HttpServletRequest,
    @PathVariable groupName: String,
  ): ApiResponse[Seq[String]] = {
    facade.routeNames(request, CurrentUser.name, groupName)
  }

}
