package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import org.springframework.stereotype.Component

@Component
class NodeCountryAnalyzerImpl(countryAnalyzer: CountryAnalyzer) extends NodeCountryAnalyzer {
  def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    val country = countryAnalyzer.country(Seq(analysis.node))
    analysis.copy(
      abort = country.isEmpty,
      country = country
    )
  }
}
