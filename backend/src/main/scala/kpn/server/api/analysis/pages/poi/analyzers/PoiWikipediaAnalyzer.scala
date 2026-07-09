package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

object PoiWikipediaAnalyzer extends PoiAnalyzer {
  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new PoiWikipediaAnalyzer(context).analyze
  }
}

class PoiWikipediaAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {

    val wikipedia = context.poi.tagValue("wikipedia").map { tagValue =>
      if (tagValue.startsWith("nl:")) {
        val id = tagValue.substring(3)
        s"https://nl.wikipedia.org/wiki/${id.replaceAll(" ", "_")}"
      }
      else if (tagValue.startsWith("de:")) {
        val id = tagValue.substring(3)
        s"https://de.wikipedia.org/wiki/${id.replaceAll(" ", "_")}"
      }
      else if (tagValue.startsWith("fr:")) {
        val id = tagValue.substring(3)
        s"https://fr.wikipedia.org/wiki/${id.replaceAll(" ", "_")}"
      }
      else if (tagValue.startsWith("en:")) {
        val id = tagValue.substring(3)
        s"https://en.wikipedia.org/wiki/${id.replaceAll(" ", "_")}"
      }
      else {
        tagValue
      }
    }

    context.copy(
      processedTagKeys = context.processedTagKeys :+ "wikipedia",
      analysis = context.analysis.copy(
        wikipedia = wikipedia
      )
    )
  }
}
