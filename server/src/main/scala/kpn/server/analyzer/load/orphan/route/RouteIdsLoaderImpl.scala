package kpn.server.analyzer.load.orphan.route

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQuery
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRouteIds
import kpn.core.overpass.QueryRouteIdsByType
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.XML

@Component
class RouteIdsLoaderImpl(cachingOverpassQueryExecutor: OverpassQueryExecutor) extends RouteIdsLoader {

  override def loadByType(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Set[Long] = {
    val overpassQuery = QueryRouteIdsByType(scopedNetworkType)
    ids(timestamp, "relation", overpassQuery)
  }

  override def load(timestamp: Timestamp): Set[Long] = {
    val overpassQuery = QueryRouteIds()
    ids(timestamp, "relation", overpassQuery)
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
