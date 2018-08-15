package kpn.core.engine.analysis

import kpn.core.analysis.NetworkNode
import kpn.core.data.Data
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.shared.data.Node

class NetworkNodeBuilder(data: Data, countryAnalyzer: CountryAnalyzer) {

  def networkNodes: Map[Long, NetworkNode] = data.networkNodes.map(buildNode).map(n => (n.node.id, n)).toMap

  private def buildNode(node: Node): NetworkNode = {
    val name = node.tags(data.networkType.nodeTagKey).getOrElse("no-name")
    val country = countryAnalyzer.country(Seq(node))
    NetworkNode(node, name, country)
  }
}
