package kpn.server.analyzer.load

import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.NetworkNameAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.server.analyzer.load.data.LoadedNetwork
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.Elem
import scala.xml.XML

@Component
class NetworkLoaderImpl(cachingOverpassQueryExecutor: OverpassQueryExecutor) extends NetworkLoader {

  private val log = Log(classOf[NetworkLoaderImpl])

  def load(timestamp: Option[Timestamp], networkId: Long): Option[LoadedNetwork] = {

    val xmlString: String = log.infoElapsed {
      val xml = cachingOverpassQueryExecutor.executeQuery(timestamp, QueryRelation(networkId))
      ("Load at " + timestamp.getOrElse(Time.now).iso, xml)
    }

    new Loader(timestamp: Option[Timestamp], networkId: Long, xmlString).load()
  }

  private class Loader(timestamp: Option[Timestamp], networkId: Long, xmlString: String) {

    def load(): Option[LoadedNetwork] = {
      loadXml().flatMap(load)
    }

    private def load(xml: Elem): Option[LoadedNetwork] = {
      val rawData = new Parser().parse(xml.head).copy(timestamp = timestamp)
      if (rawData.isEmpty) {
        log.error(s"Raw data does not contain relation with id $networkId")
        None
      }
      else {
        rawData.relationWithId(networkId) match {
          case Some(rawRelation) => load(rawData, rawRelation)
          case None =>
            log.error(s"Raw data does not contain relation with id $networkId\n$xmlString")
            None
        }
      }
    }

    private def load(rawData: RawData, rawRelation: RawRelation): Option[LoadedNetwork] = {
      // put network relation first in the list of relations, so that in circular relation references detection later, priority is given to the network relation
      val improvedRawData = if (rawData.relations.isEmpty || rawData.relations.head.id == networkId) {
        rawData
      }
      else {
        val networkRelations = rawData.relations.filter(_.id == networkId)
        val nonNetworkRelations = rawData.relations.filterNot(_.id == networkId)
        val sortedRelations = networkRelations ++ nonNetworkRelations
        RawData(
          rawData.timestamp,
          rawData.nodes,
          rawData.ways,
          sortedRelations
        )
      }

      RelationAnalyzer.scopedNetworkType(rawRelation) match {
        case None =>
          log.error(s"Network type not found for network with id $networkId\n$xmlString")
          None

        case Some(scopedNetworkType) =>

          val data = new DataBuilder(improvedRawData).data
          val relation = data.relations(networkId)
          val networkName = new NetworkNameAnalyzer(relation).name
          Some(LoadedNetwork(networkId, scopedNetworkType, networkName, data, relation))
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
