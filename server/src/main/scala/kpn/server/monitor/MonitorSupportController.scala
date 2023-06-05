package kpn.server.monitor

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.server.monitor.route.MonitorRouteStructureLoader
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Array("/api/monitor-support"))
class MonitorSupportController(
  monitorRouteStructureLoader: MonitorRouteStructureLoader
) {

  @GetMapping(value = Array("route-structure/{routeRelationId}"))
  def routeStructure(
    @PathVariable routeRelationId: Long
  ): Option[MonitorRouteRelation] = {
    monitorRouteStructureLoader.load(None, routeRelationId)
  }
}
