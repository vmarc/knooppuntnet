package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

object PoiImageAnalyzer extends PoiAnalyzer {
  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new PoiImageAnalyzer(context).analyze
  }
}

class PoiImageAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {

    val mapillary = context.poi.tags("mapillary").map { tagValue =>
      if (tagValue.startsWith("http")) {
        tagValue
      }
      else {
        s"http://www.mapillary.com/map/im/$tagValue"
      }
    }

    val image = context.poi.tags("image")

    context.copy(
      analysis = context.analysis.copy(image = image, mapillary = mapillary),
      processedTagKeys = context.processedTagKeys ++ Seq(
        "image",
        "mapillary"
      )
    )
  }

}
