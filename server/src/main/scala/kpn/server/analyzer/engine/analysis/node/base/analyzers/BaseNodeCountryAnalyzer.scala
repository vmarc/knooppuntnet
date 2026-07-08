package kpn.server.analyzer.engine.analysis.node.base.analyzers

import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class BaseNodeCountryAnalyzer(locationAnalyzer: LocationAnalyzer) extends BaseNodeAnalyzer {
  override def analyze(context: BaseNodeAnalysisContext): BaseNodeAnalysisContext = {
    val country = locationAnalyzer.country(Seq(context.node))
    context.copy(
      _country = Some(country)
    )
  }
}
