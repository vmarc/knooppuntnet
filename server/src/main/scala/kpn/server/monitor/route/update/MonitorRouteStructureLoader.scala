package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelationStructure
import org.springframework.stereotype.Component

import scala.xml.XML

@Component
class MonitorRouteStructureLoader(overpassQueryExecutor: OverpassQueryExecutor) {

  def load(timestamp: Option[Timestamp], relationId: Long): Option[MonitorRouteRelation] = {
    val xmlString = overpassQueryExecutor.executeQuery(timestamp, QueryRelationStructure(relationId))
    val filteredXmlString = xmlString.linesIterator.filter { line =>
      !(line.contains("<node id") || line.contains("<way id") || line.contains("<member type=\"node\"") || line.contains("<member type=\"way\""))
    }.mkString("\n")
    val xml = XML.loadString(filteredXmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data
    data.relations.get(relationId).map { relation =>
      MonitorRouteRelation.from(relation, None)
    }
  }
}
