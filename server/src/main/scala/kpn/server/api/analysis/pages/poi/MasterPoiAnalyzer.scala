package kpn.server.api.analysis.pages.poi

import kpn.api.common.Poi

trait MasterPoiAnalyzer {
  def analyze(poi: Poi): PoiAnalysisContext
}
