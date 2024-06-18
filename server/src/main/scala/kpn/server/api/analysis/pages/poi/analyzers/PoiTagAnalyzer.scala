package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

object PoiTagAnalyzer extends PoiAnalyzer {
  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new PoiTagAnalyzer(context).analyze
  }
}

class PoiTagAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {

    context.copy(
      analysis = context.analysis.copy(
        openingHours = context.poi.tagValue("opening_hours"),
        serviceTimes = context.poi.tagValue("service_times"),
        cuisine = context.poi.tagValue("cuisine"),
        denomination = context.poi.tagValue("denomination"),
      ),
      processedTagKeys = context.processedTagKeys ++ Seq(
        "opening_hours",
        "service_times",
        "cuisine",
        "denomination"
      )
    )
  }
}
