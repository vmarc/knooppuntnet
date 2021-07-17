package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.data.Node
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.location.OldNodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalysis
import org.springframework.stereotype.Component

@Component
class OldMainNodeAnalyzerImpl(
  countryAnalyzer: CountryAnalyzer,
  oldNodeLocationAnalyzer: OldNodeLocationAnalyzer
) extends OldMainNodeAnalyzer {

  override def analyze(node: Node): OldNodeAnalysis = {
    val country = countryAnalyzer.country(Seq(node))
    val locations = oldNodeLocationAnalyzer.locations(node.latitude, node.longitude)
    OldNodeAnalysis(node, country, locations)
  }
}
