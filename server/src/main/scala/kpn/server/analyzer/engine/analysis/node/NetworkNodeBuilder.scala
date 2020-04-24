package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.data.Node
import kpn.api.custom.NetworkType
import kpn.core.analysis.NetworkNode
import kpn.core.data.Data
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext

class NetworkNodeBuilder(analysisContext: AnalysisContext, data: Data, networkType: NetworkType, countryAnalyzer: CountryAnalyzer) {

  def networkNodes: Map[Long, NetworkNode] = {
    val nodes = data.nodes.values.toSeq.filter(node => analysisContext.isReferencedNetworkNode(networkType, node.raw))
    nodes.map(buildNode).map(n => (n.node.id, n)).toMap
  }

  private def buildNode(node: Node): NetworkNode = {
    val name = NodeAnalyzer.name(networkType, node.tags)
    val country = countryAnalyzer.country(Seq(node))
    NetworkNode(node, name, country)
  }
}
