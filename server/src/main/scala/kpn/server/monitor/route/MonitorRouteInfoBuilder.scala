package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelationTopLevel
import org.springframework.stereotype.Component

import scala.xml.XML

@Component
class MonitorRouteInfoBuilder(overpassQueryExecutor: OverpassQueryExecutor) {

  def build(routeRelationId: Long): MonitorRouteInfoPage = {
    val xmlString = overpassQueryExecutor.executeQuery(None, QueryRelationTopLevel(routeRelationId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data
    data.relations.get(routeRelationId) match {
      case None => MonitorRouteInfoPage(routeRelationId)
      case Some(relation) =>
        MonitorRouteInfoPage(
          relationId = routeRelationId,
          name = relation.tags("name"),
          ref = relation.tags("ref"),
          from = relation.tags("from"),
          to = relation.tags("to"),
          operator = relation.tags("operator"),
          website = relation.tags("website"),
          symbol = relation.tags("osmc:symbol"),
        )
    }
  }
}
