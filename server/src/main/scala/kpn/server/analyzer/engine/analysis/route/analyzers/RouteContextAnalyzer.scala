package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.core.util.Redesign
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouteContextAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteContextAnalyzer(context).analyze()
    context
  }
}

class RouteContextAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze(): Unit = {
    if (Redesign.enableDebugPrinting) {
      RouteAnalysisContextReport.report(context)
    }
  }
}
