package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelationTopLevel
import kpn.core.util.RouteSymbol
import kpn.server.monitor.route.update.RelationTopLevelDataBuilder
import org.springframework.stereotype.Component

import scala.xml.XML

@Component
class MonitorRouteInfoBuilder(overpassQueryExecutor: OverpassQueryExecutor) {

  def build(routeRelationId: Long): MonitorRouteInfoPage = {
    val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelationTopLevel(routeRelationId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new RelationTopLevelDataBuilder(rawData, Seq(routeRelationId)).data
    data.relations.get(routeRelationId) match {
      case None => MonitorRouteInfoPage(routeRelationId)
      case Some(relation) =>
        val route = relation.tagValue("route")
        val name = relation.tagValue("name")
        val ref = relation.tagValue("ref")
        val from = relation.tagValue("from")
        val to = relation.tagValue("to")
        val operator = relation.tagValue("operator")
        val website = relation.tagValue("website")
        val symbol = RouteSymbol.from(relation)
        val hasRouteTags = route.nonEmpty && Seq(name, ref, from, to, operator, symbol).flatten.nonEmpty
        MonitorRouteInfoPage(
          routeRelationId,
          active = true,
          hasRouteTags,
          name,
          ref,
          from,
          to,
          operator,
          website,
          symbol
        )
    }
  }
}
