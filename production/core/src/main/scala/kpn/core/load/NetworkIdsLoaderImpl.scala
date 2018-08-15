package kpn.core.load

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryNetworkIds
import kpn.core.util.Log
import kpn.shared.NetworkType
import kpn.shared.Timestamp
import org.xml.sax.SAXParseException

import scala.xml.XML

class NetworkIdsLoaderImpl(
  executor: OverpassQueryExecutor
) extends NetworkIdsLoader {

  private val log = Log(classOf[NetworkIdsLoaderImpl])

  override def load(timestamp: Timestamp, networkType: NetworkType): Seq[Long] = {
    val xmlString = executor.executeQuery(Some(timestamp), QueryNetworkIds(networkType))
    val xml = try {
      XML.loadString(xmlString)
    }
    catch {
      case e: SAXParseException => throw new RuntimeException(s"Could not load ids for network type ${networkType.name} at ${timestamp.yyyymmddhhmmss}\n$xmlString", e)
    }
    val ids = (xml \ "relation").map { n => (n \ "@id").text.toLong }
    log.info(s"${ids.size} ${networkType.name} network ids loaded")
    ids.distinct.sorted
  }
}
