package kpn.server.analyzer.engine.analysis.node

import kpn.core.analysis.NetworkNode
import kpn.core.data.Data
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.shared.NetworkType
import kpn.shared.data.Node

class NetworkNodeBuilder(analysisContext: AnalysisContext, data: Data, networkType: NetworkType, countryAnalyzer: CountryAnalyzer) {

  def networkNodes: Map[Long, NetworkNode] = {
    val nodes = data.nodes.values.toSeq.filter(node => analysisContext.isNetworkNode(networkType, node.raw))
    nodes.map(buildNode).map(n => (n.node.id, n)).toMap
  }

  private def buildNode(node: Node): NetworkNode = {
    val name = NodeAnalyzer.name(networkType, node.tags)
    val country = countryAnalyzer.country(Seq(node))
    NetworkNode(node, name, country)
  }
}
