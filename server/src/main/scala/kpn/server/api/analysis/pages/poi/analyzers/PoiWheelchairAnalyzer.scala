package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

object PoiWheelchairAnalyzer extends PoiAnalyzer {
  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new PoiWheelchairAnalyzer(context).analyze
  }
}

class PoiWheelchairAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {
    val wheelchair = context.poi.tags("wheelchair")
    context.copy(
      analysis = context.analysis.copy(wheelchair = wheelchair),
      processedTagKeys = context.processedTagKeys :+ "wheelchair"
    )
  }

}
