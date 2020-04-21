package kpn.server.analyzer.load.orphan.node

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQuery
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryNodeIds
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.XML

@Component
class NodeIdsLoaderImpl(overpassQueryExecutor: OverpassQueryExecutor) extends NodeIdsLoader {

  override def load(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Set[Long] = {
    val overpassQuery = QueryNodeIds(scopedNetworkType)
    ids(timestamp, "node", overpassQuery)
  }

  private def ids(timestamp: Timestamp, elementTag: String, query: OverpassQuery): Set[Long] = {
    parseIds(elementTag, overpassQueryExecutor.executeQuery(Some(timestamp), query))
  }

  private def parseIds(elementTag: String, xmlString: String): Set[Long] = {
    val xml = try {
      XML.loadString(xmlString)
    }
    catch {
      case e: SAXParseException => throw new RuntimeException(s"Could not load xml\n$xmlString", e)
    }
    (xml \ elementTag).map { n => (n \ "@id").text.toLong }.toSet
  }
}
