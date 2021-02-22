package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.data.Node
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.location.NodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.node.NodeAnalysis
import org.springframework.stereotype.Component

@Component
class MainNodeAnalyzerImpl(
  countryAnalyzer: CountryAnalyzer,
  nodeLocationAnalyzer: NodeLocationAnalyzer
) extends MainNodeAnalyzer {

  override def analyze(node: Node): NodeAnalysis = {
    val country = countryAnalyzer.country(Seq(node))
    val location = nodeLocationAnalyzer.locate(node.latitude, node.longitude)
    NodeAnalysis(node, country, location)
  }

}
