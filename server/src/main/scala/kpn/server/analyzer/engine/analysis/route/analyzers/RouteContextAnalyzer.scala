package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.core.util.Redesign
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteContextAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteContextAnalyzer(context).analyze
    context
  }
}

class RouteContextAnalyzer(context: RouteAnalysisContext) {

  def analyze: Unit = {
    if (Redesign.enableDebugPrinting) {
      RouteAnalysisContextReport.report(context)
    }
  }
}
