package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelationOnly
import org.springframework.stereotype.Component

import scala.xml.XML

@Component
class MonitorRouteInfoBuilder(overpassQueryExecutor: OverpassQueryExecutor) {

  def build(routeId: Long): MonitorRouteInfoPage = {
    val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelationOnly(routeId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    rawData.relationWithId(routeId) match {
      case None => MonitorRouteInfoPage("TODO MON", routeId)
      case Some(relation) =>
        MonitorRouteInfoPage(
          "TODO",
          routeId,
          relation.tags("name"),
          relation.tags("operator"),
          relation.tags("ref"),
          relation.nodeMembers.size,
          relation.wayMembers.size,
          relation.relationMembers.size
        )
    }
  }
}
