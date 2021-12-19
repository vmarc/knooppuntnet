package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import org.springframework.stereotype.Component

@Component
class NodeCountryAnalyzerImpl(locationAnalyzer: LocationAnalyzer) extends NodeCountryAnalyzer {
  def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    val country = locationAnalyzer.country(Seq(analysis.node))
    analysis.copy(
      abort = country.isEmpty,
      country = country
    )
  }
}
