package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import org.springframework.stereotype.Component

@Component
class NodeLocationsAnalyzerImpl(
  locationAnalyzer: LocationAnalyzer
) extends NodeLocationsAnalyzer {

  override def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    val locations = locationAnalyzer.findLocations(analysis.node.latitude, analysis.node.longitude)
    analysis.copy(locations = locations)
  }
}
