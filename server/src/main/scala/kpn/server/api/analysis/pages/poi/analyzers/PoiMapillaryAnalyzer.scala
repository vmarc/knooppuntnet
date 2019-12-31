package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

object PoiMapillaryAnalyzer extends PoiAnalyzer {

  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new PoiMapillaryAnalyzer(context).analyze
  }
}

class PoiMapillaryAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {

    context.poi.tags("mapillary") match {
      case None => context
      case Some(tagValue) =>

        val mapillary = if (tagValue.startsWith("http") || tagValue.startsWith("https")) {
          tagValue
        }
        else {
          s"http://www.mapillary.com/map/im/$tagValue"
        }
        context.copy(
          analysis = context.analysis.copy(mapillary = Some(mapillary)),
          processedTagKeys = context.processedTagKeys :+ "mapillary"
        )
    }
  }
}
