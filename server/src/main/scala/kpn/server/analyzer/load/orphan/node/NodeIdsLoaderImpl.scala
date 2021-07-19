package kpn.server.analyzer.load.orphan.node

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQuery
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryNodeIds
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeNameAnalyzer
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.XML

@Component
class NodeIdsLoaderImpl(cachingOverpassQueryExecutor: OverpassQueryExecutor) extends NodeIdsLoader {

  override def load(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Seq[Long] = {
    NodeNameAnalyzer.nodeTagKeys(scopedNetworkType).flatMap { nodeTagKey =>
      val query = QueryNodeIds(nodeTagKey)
      ids(timestamp, "node", query)
    }.distinct.sorted
  }

  private def ids(timestamp: Timestamp, elementTag: String, query: OverpassQuery): Seq[Long] = {
    parseIds(elementTag, cachingOverpassQueryExecutor.executeQuery(Some(timestamp), query))
  }

  private def parseIds(elementTag: String, xmlString: String): Seq[Long] = {
    val xml = try {
      XML.loadString(xmlString)
    }
    catch {
      case e: SAXParseException => throw new RuntimeException(s"Could not load xml\n$xmlString", e)
    }
    (xml \ elementTag).map { n => (n \ "@id").text.toLong }
  }
}
