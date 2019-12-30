package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

object PoiWikidataAnalyzer extends PoiAnalyzer {
  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new PoiWikidataAnalyzer(context).analyze
  }
}

class PoiWikidataAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {

    val wikidata = context.poi.tags("wikidata").map { id =>
      "https://www.wikidata.org/wiki/" + id
    }

    context.copy(
      processedTagKeys = context.processedTagKeys :+ "wikidata",
      analysis = context.analysis.copy(
        wikidata = wikidata
      )
    )
  }

}
