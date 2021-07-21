package kpn.server.analyzer.load.orphan.node

import kpn.api.custom.{ScopedNetworkType, Timestamp}
import kpn.core.overpass.{OverpassQuery, OverpassQueryExecutor, QueryNodeIds}
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.XML

@Component
class NodeIdsLoaderImpl(cachingOverpassQueryExecutor: OverpassQueryExecutor) extends NodeIdsLoader {

  override def load(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Set[Long] = {
    Seq(
      scopedNetworkType.nodeTagKey,
      scopedNetworkType.proposedNodeTagKey
    ).flatMap { nodeTagKey =>
      val query = QueryNodeIds(nodeTagKey)
      ids(timestamp, "node", query)
    }.toSet
  }

  private def ids(timestamp: Timestamp, elementTag: String, query: OverpassQuery): Set[Long] = {
    parseIds(elementTag, cachingOverpassQueryExecutor.executeQuery(Some(timestamp), query))
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
