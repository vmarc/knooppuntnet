package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.Country
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

class OldNodeCountryAnalyzerMock(country: Option[Country]) extends OldNodeCountryAnalyzer {
  def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    analysis.copy(country = country)
  }
}
