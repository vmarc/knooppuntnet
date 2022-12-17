package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelationStructure
import org.springframework.stereotype.Component

import scala.xml.XML

@Component
class MonitorSupportFacadeImpl(
  overpassQueryExecutor: OverpassQueryExecutor
) extends MonitorSupportFacade {

  override def routeStructure(routeRelationId: Long): Option[MonitorRouteRelation] = {
    val query = QueryRelationStructure(routeRelationId)
    val xmlString = overpassQueryExecutor.executeQuery(None, query)
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new MonitorRelationDataBuilder(rawData).data
    data.relations.get(routeRelationId).map { relation =>
      MonitorRouteRelation.from(relation, None)
    }
  }
}
