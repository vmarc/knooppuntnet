package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.core.util.Redesign

object BaseRouteContextAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteContextAnalyzer(context).analyze()
    context
  }
}

class BaseRouteContextAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze(): Unit = {
    if (Redesign.enableDebugPrinting) {
      RouteAnalysisContextReport.report(context)
    }
  }
}
