package kpn.server.analyzer.load

import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.server.analyzer.engine.analysis.NetworkNameAnalyzer
import kpn.server.analyzer.load.data.LoadedNetwork
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelation
import kpn.core.util.Log
import kpn.shared.NetworkType
import kpn.shared.Timestamp
import kpn.shared.data.raw.RawData
import kpn.shared.data.raw.RawRelation
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.Elem
import scala.xml.XML

/*
   Loads the network state at a given moment in time from the Overpass database and transforms the XML into
   object format.
*/
@Component
class NetworkLoaderImpl(executor: OverpassQueryExecutor) extends NetworkLoader {

  private val log = Log(classOf[NetworkLoaderImpl])

  def load(timestamp: Option[Timestamp], networkId: Long): Option[LoadedNetwork] = {

    val xmlString: String = log.elapsed {
      val xml = executor.executeQuery(timestamp, QueryRelation(networkId))
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
      rawData.relationWithId(networkId) match {
        case Some(rawRelation) => load(rawData, rawRelation)
        case None =>
          log.error(s"Raw data does not contain relation with id $networkId\n$xmlString")
          None
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

      networkTypeIn(rawRelation) match {
        case None =>
          log.error(s"Network type not found for network with id $networkId\n$xmlString")
          None

        case Some(networkType) =>

          val data = new DataBuilder(improvedRawData).data
          val relation = data.relations(networkId)
          val networkName = new NetworkNameAnalyzer(relation).name
          Some(LoadedNetwork(networkId, networkType, networkName, data, relation))
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

    private def networkTypeIn(relation: RawRelation): Option[NetworkType] = {
      relation.tags("network").flatMap(NetworkType.withName)
    }
  }

}
