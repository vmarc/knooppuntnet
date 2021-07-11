package kpn.server.analyzer.load

import kpn.api.common.data.Node
import kpn.api.common.data.raw.RawData
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryNodes
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.load.data.LoadedNode
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.Elem
import scala.xml.XML

@Component
class NodeLoaderImpl(
  nonCachingOverpassQueryExecutor: OverpassQueryExecutor,
  countryAnalyzer: CountryAnalyzer
) extends NodeLoader {

  private val log = Log(classOf[NodeLoader])

  override def loadNodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[LoadedNode] = {
    if (nodeIds.isEmpty) {
      Seq.empty
    }
    else {
      val datas = nodeIds.sliding(50, 50).toSeq.map { nodeIdSubset =>
        doLoad(timestamp, nodeIdSubset)
      }
      val rawData = RawData.merge(datas: _*)
      rawData.nodes.map { node =>
        val countries = countryAnalyzer.countries(node)
        LoadedNode.from(countries.headOption, node)
      }
    }
  }

  override def load(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType, nodeIds: Seq[Long]): Seq[LoadedNode] = {
    if (nodeIds.isEmpty) {
      Seq.empty
    }
    else {
      val datas = nodeIds.sliding(50, 50).toSeq.map { nodeIdSubset =>
        doLoad(timestamp, scopedNetworkType, nodeIdSubset)
      }
      val rawData = RawData.merge(datas: _*)
      val data = new DataBuilder(rawData).data
      val nodeOptions = nodeIds.map(id => id -> data.nodes.get(id))
      loadedNodes(nodeOptions)
    }
  }

  private def doLoad(timestamp: Timestamp, scopedNetworkType: ScopedNetworkType, nodeIds: Seq[Long]): RawData = {

    val xmlString: String = log.elapsed {
      val xml = nonCachingOverpassQueryExecutor.executeQuery(Some(timestamp), QueryNodes("nodes", nodeIds))
      (s"${timestamp.iso} Load ${scopedNetworkType.key} ${nodeIds.size}", xml)
    }

    val xml = try {
      XML.loadString(xmlString)
    }
    catch {
      case e: SAXParseException =>
        val msg = s"Could not load nodes for networkType ${scopedNetworkType.key} at ${timestamp.yyyymmddhhmmss} [ids=$nodeIds]\n$xmlString"
        throw new RuntimeException(msg, e)
    }

    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data
    val nodeOptions = nodeIds.map(id => id -> data.nodes.get(id))
    logMissingNodes(timestamp, xml, nodeOptions)
    rawData
  }

  private def doLoad(timestamp: Timestamp, nodeIds: Seq[Long]): RawData = {

    val xmlString: String = log.elapsed {
      val xml = nonCachingOverpassQueryExecutor.executeQuery(Some(timestamp), QueryNodes("nodes", nodeIds))
      (s"${timestamp.iso} Load ${nodeIds.size} nodes", xml)
    }

    val xml = try {
      XML.loadString(xmlString)
    }
    catch {
      case e: SAXParseException =>
        val msg = s"Could not load node ids at ${timestamp.yyyymmddhhmmss} [ids=$nodeIds]\n$xmlString"
        throw new RuntimeException(msg, e)
    }

    new Parser().parse(xml.head)
  }

  private def loadedNodes(nodeOptions: Seq[(Long, Option[Node])]): Seq[LoadedNode] = {
    val nodes = nodeOptions.flatMap(_._2)
    nodes.map { node =>
      val countries = countryAnalyzer.countries(node)
      LoadedNode.from(countries.headOption, node.raw)
    }
  }

  private def logMissingNodes(timestamp: Timestamp, xml: Elem, nodeOptions: Seq[(Long, Option[Node])]): Unit = {
    val missingNodeIds = nodeOptions.flatMap { case (nodeId, option) =>
      if (option.isEmpty) Some(nodeId) else None
    }

    if (missingNodeIds.nonEmpty) {
      val string = missingNodeIds.mkString(",")
      log.error(s"${timestamp.iso} Nodes with id $string not found in query response\n$xml")
    }
  }
}
