package kpn.core.engine.analysis

import kpn.core.analysis.NetworkNode
import kpn.core.data.Data
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.tools.analyzer.AnalysisContext
import kpn.shared.NetworkType
import kpn.shared.data.Node

class NetworkNodeBuilder(analysisContext: AnalysisContext, data: Data, networkType: NetworkType, countryAnalyzer: CountryAnalyzer) {

  def networkNodes: Map[Long, NetworkNode] = {
    val nodes = data.nodes.values.toSeq.filter(node => analysisContext.isNetworkNode(networkType, node.raw))
    nodes.map(buildNode).map(n => (n.node.id, n)).toMap
  }

  private def buildNode(node: Node): NetworkNode = {
    val name = node.tags(networkType.nodeTagKey).getOrElse("no-name")
    val country = countryAnalyzer.country(Seq(node))
    NetworkNode(node, name, country)
  }
}
