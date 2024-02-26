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
        val route = relation.tags("route")
        val name = relation.tags("name")
        val ref = relation.tags("ref")
        val from = relation.tags("from")
        val to = relation.tags("to")
        val operator = relation.tags("operator")
        val website = relation.tags("website")
        val symbol = RouteSymbol.from(relation.tags)
        val hasRouteRelationTags = route.nonEmpty && Seq(name, ref, from, to, operator, symbol).flatten.nonEmpty
        MonitorRouteInfoPage(
          routeRelationId,
          active = true,
          hasRouteTags = hasRouteRelationTags,
          name = relation.tags("name"),
          ref = relation.tags("ref"),
          from = relation.tags("from"),
          to = relation.tags("to"),
          operator = relation.tags("operator"),
          website = relation.tags("website"),
          symbol = RouteSymbol.from(relation.tags),
        )
    }
  }
}
