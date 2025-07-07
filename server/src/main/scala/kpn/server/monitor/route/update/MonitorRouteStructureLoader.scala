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
    val xmlString = fetchRelationStructureFromOverpass(timestamp, relationId)
    val filteredXmlString = removeIrrelevantElementsFromXml(xmlString)
    val parsedRelation = parseRelationFromXml(filteredXmlString)

    convertToMonitorRouteRelation(parsedRelation, relationId)
  }

  private def fetchRelationStructureFromOverpass(timestamp: Option[Timestamp], relationId: Long): String = {
    overpassQueryExecutor.executeQuery(timestamp, QueryRelationStructure(relationId))
  }

  private def removeIrrelevantElementsFromXml(xmlString: String): String = {
    // Filter out nodes, ways, and their references as we only need relation structure
    xmlString.linesIterator.filter { line =>
      !(line.contains("<node id") ||
        line.contains("<way id") ||
        line.contains("<member type=\"node\"") ||
        line.contains("<member type=\"way\""))
    }.mkString("\n")
  }

  private def parseRelationFromXml(filteredXmlString: String): scala.xml.Node = {
    val xml = XML.loadString(filteredXmlString)
    xml.head
  }

  private def convertToMonitorRouteRelation(xmlNode: scala.xml.Node, relationId: Long): Option[MonitorRouteRelation] = {
    val rawData = new Parser().parse(xmlNode)
    val data = new DataBuilder(rawData).data

    data.relations.get(relationId).map { relation =>
      MonitorRouteRelation.from(relation, None)
    }
  }
}
