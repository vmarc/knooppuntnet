package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

object PoiTitleAnalyzer extends PoiAnalyzer {
  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new PoiTitleAnalyzer(context).analyze
  }
}

class PoiTitleAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {

    val layers = context.poi.layers
    val name = context.poi.tags("name")
    val description = context.poi.tags("description")

    context.copy(
      analysis = context.analysis.copy(
        layers = layers,
        name = name,
        description = description
      ),
      processedTagKeys = context.processedTagKeys ++ Seq(
        "name",
        "description"
      )
    )
  }
}
