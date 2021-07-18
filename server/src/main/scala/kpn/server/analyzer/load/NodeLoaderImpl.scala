package kpn.server.analyzer.load

import kpn.api.common.data.Node
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.NetworkType
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryNodes
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedNode
import org.springframework.stereotype.Component
import org.xml.sax.SAXParseException

import scala.xml.Elem
import scala.xml.XML

@Component
class NodeLoaderImpl(
  analysisContext: AnalysisContext,
  nonCachingOverpassQueryExecutor: OverpassQueryExecutor,
  countryAnalyzer: CountryAnalyzer,
  oldNodeAnalyzer: OldNodeAnalyzer
) extends NodeLoader {

  private val log = Log(classOf[NodeLoader])

  override def oldLoadNodes(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[LoadedNode] = {
    if (nodeIds.isEmpty) {
      Seq.empty
    }
    else {
      val datas = nodeIds.sliding(50, 50).toSeq.map { nodeIdSubset =>
        oldDoLoad(timestamp, nodeIdSubset)
      }
      val rawData = RawData.merge(datas: _*)
      rawData.nodes.map { node =>
        val countries = countryAnalyzer.countries(node)
        val networkTypes = NetworkType.all.filter { networkType =>
          analysisContext.isValidNetworkNode(networkType, node)
        }
        val name = oldNodeAnalyzer.name(node.tags)
        LoadedNode(countries.headOption, networkTypes, name, Node(node))
      }
    }
  }

  override def load(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[RawNode] = {
    if (nodeIds.isEmpty) {
      Seq.empty
    }
    else {
      val datas = nodeIds.sliding(50, 50).toSeq.map { nodeIdSubset =>
        doLoad(timestamp, nodeIdSubset)
      }
      val rawData = RawData.merge(datas: _*)
      val data = new DataBuilder(rawData).data
      nodeIds.flatMap(id => data.nodes.get(id)).map(_.raw)
    }
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
        val msg = s"Could not load nodes at ${timestamp.yyyymmddhhmmss} [ids=$nodeIds]\n$xmlString"
        throw new RuntimeException(msg, e)
    }

    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data
    val nodeOptions = nodeIds.map(id => id -> data.nodes.get(id))
    logMissingNodes(timestamp, xml, nodeOptions)
    rawData
  }

  private def oldDoLoad(timestamp: Timestamp, nodeIds: Seq[Long]): RawData = {

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
      val networkTypes = NetworkType.all.filter { networkType =>
        analysisContext.isValidNetworkNode(networkType, node.raw)
      }
      val name = oldNodeAnalyzer.name(node.tags)
      LoadedNode(countries.headOption, networkTypes, name, node)
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
