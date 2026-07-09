package kpn.server.api.analysis.pages.poi

trait PoiAnalyzer {
  def analyze(context: PoiAnalysisContext): PoiAnalysisContext
}
