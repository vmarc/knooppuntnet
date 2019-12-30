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

    val image = context.poi.tags("image") // TODO add support for 'mapillary' here ...

    context.copy(
      analysis = context.analysis.copy(image = image),
      processedTagKeys = context.processedTagKeys ++ Seq(
        "image",
        "TODO mapillary"
      )
    )
  }

}
