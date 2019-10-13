package kpn.server.analyzer.load.orphan.route

import kpn.core.overpass.OverpassQuery
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRouteIds
import kpn.shared.NetworkType
import kpn.shared.Timestamp
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.XML

@Component
class RouteIdsLoaderImpl(executor: OverpassQueryExecutor) extends RouteIdsLoader {

  override def load(timestamp: Timestamp, networkType: NetworkType): Set[Long] = {
    val overpassQuery = QueryRouteIds(networkType)
    ids(timestamp, "relation", overpassQuery)
  }

  private def ids(timestamp: Timestamp, elementTag: String, query: OverpassQuery): Set[Long] = {
    parseIds(elementTag, executor.executeQuery(Some(timestamp), query))
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
