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
import kpn.api.common.monitor.MonitorRouteGpxPage
import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.common.monitor.MonitorRouteUpdatePage
import kpn.api.custom.ApiResponse
import kpn.core.common.Time
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
class MonitorController(facade: MonitorFacade) {

  @PostMapping(value = Array("changes"))
  def changes(@RequestBody parameters: MonitorChangesParameters): ApiResponse[MonitorChangesPage] = {
    facade.changes(parameters)
  }

  @GetMapping(value = Array("groups"))
  def groups(): ApiResponse[MonitorGroupsPage] = {
    facade.groups()
  }

  @GetMapping(value = Array("group-names"))
  def groupNames(): ApiResponse[Seq[String]] = {
    facade.groupNames()
  }

  @GetMapping(value = Array("groups/{groupName}"))
  def group(@PathVariable groupName: String): ApiResponse[MonitorGroupPage] = {
    facade.group(groupName)
  }

  @PostMapping(value = Array("groups"))
  def groupAdd(@RequestBody properties: MonitorGroupProperties): Unit = {
    facade.groupAdd(properties)
  }

  @PutMapping(value = Array("groups/{groupId}"))
  def groupUpdate(
    @PathVariable groupId: String,
    @RequestBody properties: MonitorGroupProperties
  ): Unit = {
    facade.groupUpdate(ObjectId(groupId), properties)
  }

  @DeleteMapping(value = Array("groups/{groupId}"))
  def groupDelete(@PathVariable groupId: String): Unit = {
    facade.groupDelete(ObjectId(groupId))
  }

  @PostMapping(value = Array("groups/{groupName}/changes"))
  def groupChanges(
    @PathVariable groupName: String,
    @RequestBody parameters: MonitorChangesParameters
  ): ApiResponse[MonitorGroupChangesPage] = {
    facade.groupChanges(groupName, parameters)
  }

  @GetMapping(value = Array("groups/{groupName}/routes/{routeName}"))
  def route(
    @PathVariable groupName: String,
    @PathVariable routeName: String
  ): ApiResponse[MonitorRouteDetailsPage] = {
    facade.route(groupName, routeName)
  }

  @PostMapping(value = Array("groups/{groupName}"))
  def routeAdd(
    @PathVariable groupName: String,
    @RequestBody properties: MonitorRouteProperties
  ): ApiResponse[MonitorRouteSaveResult] = {
    facade.routeAdd(groupName, properties)
  }

  @PutMapping(value = Array("groups/{groupName}/routes/{routeName}"))
  def routeUpdate(
    @PathVariable groupName: String,
    @PathVariable routeName: String,
    @RequestBody properties: MonitorRouteProperties
  ): ApiResponse[MonitorRouteSaveResult] = {
    facade.routeUpdate(groupName, routeName, properties)
  }

  @DeleteMapping(value = Array("groups/{groupName}/routes/{routeName}"))
  def routeDelete(
    @PathVariable groupName: String,
    @PathVariable routeName: String
  ): Unit = {
    facade.routeDelete(groupName, routeName)
  }

  @GetMapping(value = Array("groups/{groupName}/routes/{routeName}/map"))
  def routeMap(
    @PathVariable groupName: String,
    @PathVariable routeName: String,
  ): ApiResponse[MonitorRouteMapPage] = {
    facade.routeMap(groupName, routeName, None)
  }

  @GetMapping(value = Array("groups/{groupName}/routes/{routeName}/map/{relationId}"))
  def routeRelationMap(
    @PathVariable groupName: String,
    @PathVariable routeName: String,
    @PathVariable relationId: Long,
  ): ApiResponse[MonitorRouteMapPage] = {
    facade.routeMap(groupName, routeName, Some(relationId))
  }

  @GetMapping(value = Array("groups/{groupName}/routes/{routeName}/gpx/{subRelationId}"))
  def routeSubRelationGpx(
    @PathVariable groupName: String,
    @PathVariable routeName: String,
    @PathVariable subRelationId: Long
  ): ApiResponse[MonitorRouteGpxPage] = {
    facade.routeGpx(groupName, routeName, subRelationId)
  }

  @PostMapping(value = Array("groups/{groupName}/routes/{monitorRouteId}/changes"))
  def routeChanges(
    @PathVariable groupName: String,
    @PathVariable routeName: String,
    @RequestBody parameters: MonitorChangesParameters
  ): ApiResponse[MonitorRouteChangesPage] = {
    facade.routeChanges(groupName + ":" + routeName, parameters)
  }

  @GetMapping(value = Array("routes/{monitorRouteId}/changes/{changeSetId}/{replicationNumber}"))
  def routeChange(
    @PathVariable monitorRouteId: Long,
    @PathVariable changeSetId: Long,
    @PathVariable replicationNumber: Long
  ): ApiResponse[MonitorRouteChangePage] = {
    facade.routeChange(monitorRouteId, changeSetId, replicationNumber)
  }

  @GetMapping(value = Array("route-add/{groupName}"))
  def routeAddPage(@PathVariable groupName: String): ApiResponse[MonitorRouteAddPage] = {
    facade.groupRouteAdd(groupName)
  }

  @GetMapping(value = Array("groups/{groupName}/routes/{routeName}/update-info"))
  def routeUpdatePage(
    @PathVariable groupName: String,
    @PathVariable routeName: String,
  ): ApiResponse[MonitorRouteUpdatePage] = {
    facade.routeUpdatePage(groupName, routeName)
  }

  @GetMapping(value = Array("route-info/{routeId}"))
  def routeInfo(@PathVariable routeId: Long): ApiResponse[MonitorRouteInfoPage] = {
    facade.routeInfo(routeId)
  }

  @PostMapping(value = Array(s"groups/{groupName}/routes/{routeName}/upload/{relationId}"))
  def routeReferenceGpxFileUpload(
    @PathVariable groupName: String,
    @PathVariable routeName: String,
    @PathVariable relationId: Long,
    @RequestParam("file") file: MultipartFile
  ): ApiResponse[MonitorRouteSaveResult] = {

    val xml = XML.load(file.getInputStream)

    facade.upload(
      groupName,
      routeName,
      relationId,
      Time.now.toDay, // TODO should get reference day from client !!!
      file.getOriginalFilename,
      xml
    )
  }

  @GetMapping(value = Array("groups/{groupName}/route-names"))
  def routeNames(@PathVariable groupName: String): ApiResponse[Seq[String]] = {
    facade.routeNames(groupName)
  }
}
