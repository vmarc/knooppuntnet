package kpn.server.analyzer.load

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryNetworkIds
import kpn.core.util.Log
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.XML

@Component
class NetworkIdsLoaderImpl(
  cachingOverpassQueryExecutor: OverpassQueryExecutor
) extends NetworkIdsLoader {

  private val log = Log(classOf[NetworkIdsLoaderImpl])

  override def load(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType): Seq[Long] = {
    val xmlString = cachingOverpassQueryExecutor.executeQuery(Some(timestamp), QueryNetworkIds(scopedNetworkType))
    val xml = try {
      XML.loadString(xmlString)
    }
    catch {
      case e: SAXParseException => throw new RuntimeException(s"Could not load ids for network type ${scopedNetworkType.key} at ${timestamp.yyyymmddhhmmss}\n$xmlString", e)
    }
    val ids = (xml \ "relation").map { n => (n \ "@id").text.toLong }
    log.info(s"${ids.size} ${scopedNetworkType.key} network ids loaded")
    ids.distinct.sorted
  }
}
