package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.custom.Country
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

class NodeCountryAnalyzerMock(country: Option[Country]) extends NodeCountryAnalyzer {
  def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    analysis.copy(country = country)
  }
}
