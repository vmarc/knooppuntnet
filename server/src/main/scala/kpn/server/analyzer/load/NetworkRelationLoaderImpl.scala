package kpn.server.analyzer.load

import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelationOnly
import kpn.core.util.Log
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.Elem
import scala.xml.XML

@Component
class NetworkRelationLoaderImpl(cachingOverpassQueryExecutor: OverpassQueryExecutor) extends NetworkRelationLoader {

  private val log = Log(classOf[NetworkRelationLoaderImpl])

  def load(timestamp: Option[Timestamp], networkId: Long): Option[RawRelation] = {

    val xmlString: String = log.infoElapsed {
      val xml = cachingOverpassQueryExecutor.executeQuery(timestamp, QueryRelationOnly(networkId))
      ("Load at " + timestamp.getOrElse(Time.now).iso, xml)
    }

    new Loader(timestamp: Option[Timestamp], networkId: Long, xmlString).load()
  }

  private class Loader(timestamp: Option[Timestamp], networkId: Long, xmlString: String) {

    def load(): Option[RawRelation] = {
      loadXml().flatMap(load)
    }

    private def load(xml: Elem): Option[RawRelation] = {
      val rawData = new Parser().parse(xml.head).copy(timestamp = timestamp)
      if (rawData.isEmpty) {
        log.error(s"Raw data does not contain relation with id $networkId")
        None
      }
      else {
        rawData.relationWithId(networkId) match {
          case Some(rawRelation) => Some(rawRelation)
          case None =>
            log.error(s"Raw data does not contain relation with id $networkId\n$xmlString")
            None
        }
      }
    }

    private def loadXml() = {
      val xmlOption: Option[Elem] = try {
        Some(XML.loadString(xmlString))
      }
      catch {
        case e: SAXParseException =>
          log.error(s"Could not network $networkId\n$xmlString", e)
          None
      }
      xmlOption
    }
  }

}
