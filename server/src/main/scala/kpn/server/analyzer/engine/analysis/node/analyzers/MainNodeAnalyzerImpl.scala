package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.data.Node
import kpn.core.analysis.NetworkNode
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.location.NodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalysis
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import org.springframework.stereotype.Component

@Component
class MainNodeAnalyzerImpl(
  countryAnalyzer: CountryAnalyzer,
  nodeLocationAnalyzer: NodeLocationAnalyzer
) extends MainNodeAnalyzer {

  override def analyze(node: Node): NodeAnalysis = {
    // val name = NodeAnalyzer.name(networkType, node.tags)
    val country = countryAnalyzer.country(Seq(node))
    val location = nodeLocationAnalyzer.locate(node.latitude, node.longitude)
    NodeAnalysis(node, country, location)

  }

}
