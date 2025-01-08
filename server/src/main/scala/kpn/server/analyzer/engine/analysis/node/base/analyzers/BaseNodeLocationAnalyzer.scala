package kpn.server.analyzer.engine.analysis.node.base.analyzers

import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import org.springframework.stereotype.Component

@Component
class BaseNodeLocationAnalyzer(locationAnalyzer: LocationAnalyzer) extends BaseNodeAnalyzer {
  override def analyze(context: BaseNodeAnalysisContext): BaseNodeAnalysisContext = {
    val locations = locationAnalyzer.findLocations(context.node.latitude, context.node.longitude)
    context.copy(_locations = Some(locations))
  }
}
