package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelation
import kpn.core.util.Log
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.Elem
import scala.xml.XML

@Component
class MonitorRouteLoaderImpl(overpassQueryExecutor: OverpassQueryExecutor) extends MonitorRouteLoader {

  private val log = Log(classOf[MonitorRouteLoaderImpl])

  def loadInitial(timestamp: Timestamp, routeId: Long): Option[Relation] = {
    load(timestamp, routeId)
  }

  def loadBefore(changeSetId: Long, timestamp: Timestamp, routeId: Long): Option[Relation] = {
    load(timestamp, routeId)
  }

  def loadAfter(changeSetId: Long, timestamp: Timestamp, routeId: Long): Option[Relation] = {
    load(timestamp, routeId)
  }

  private def load(timestamp: Timestamp, routeId: Long): Option[Relation] = {

    val xmlString: String = log.infoElapsed {
      val xml = overpassQueryExecutor.executeQuery(Some(timestamp), QueryRelation(routeId))
      ("Load at " + timestamp.iso, xml)
    }

    new Loader(routeId, xmlString).load()
  }

  private class Loader(routeId: Long, xmlString: String) {

    def load(): Option[Relation] = {
      loadXml().flatMap(load)
    }

    private def load(xml: Elem): Option[Relation] = {
      val rawData = new Parser().parse(xml.head)
      if (rawData.isEmpty) {
        log.error(s"Raw data does not contain relation with id $routeId")
        None
      }
      else {
        rawData.relationWithId(routeId) match {
          case Some(rawRelation) => load(rawData, rawRelation)
          case None =>
            log.error(s"Raw data does not contain relation with id $routeId\n$xmlString")
            None
        }
      }
    }

    private def load(rawData: RawData, rawRelation: RawRelation): Option[Relation] = {
      val data = new DataBuilder(rawData).data
      Some(data.relations(routeId))
    }

    private def loadXml() = {
      val xmlOption: Option[Elem] = try {
        Some(XML.loadString(xmlString))
      }
      catch {
        case e: SAXParseException =>
          log.error(s"Could not route $routeId\n$xmlString", e)
          None
      }
      xmlOption
    }
  }

}
