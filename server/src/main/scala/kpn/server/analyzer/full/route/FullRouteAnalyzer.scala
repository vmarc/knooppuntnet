package kpn.server.analyzer.full.route

import kpn.server.analyzer.full.FullAnalysisContext

trait FullRouteAnalyzer {

  def analyze(context: FullAnalysisContext): FullAnalysisContext

}
