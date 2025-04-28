package kpn.server.analyzer.full.analyzers

import org.springframework.stereotype.Component

@Component
class FullAnalysisPipeline(
  fullBaseNodeAnalyzer: FullBaseNodeAnalyzer,
  fullBaseRouteAnalyzer: FullBaseRouteAnalyzer,
  fullBaseNetworkAnalyzer: FullBaseNetworkAnalyzer,
  fullNodeAnalyzer: FullNodeAnalyzer,
  fullRouteAnalyzer: FullRouteAnalyzer,
  fullNetworkAnalyzer: FullNetworkAnalyzer,
) {

  private val analyzers: List[FullAnalyzer] = List(
    fullBaseNodeAnalyzer,
    fullBaseRouteAnalyzer,
    fullBaseNetworkAnalyzer,
    fullNodeAnalyzer,
    fullRouteAnalyzer,
    fullNetworkAnalyzer
  )

  def execute(initialContext: FullAnalysisContext): FullAnalysisContext = {
    analyzers.foldLeft(initialContext) { (context, analyzer) =>
      analyzer.analyze(context)
    }
  }
}
