package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

case class IgnoredTag(layer: String, key: String, value: String)

object IgnoredTagAnalyzer extends PoiAnalyzer {

  private val ignoredTagDefinitions = Seq(
    IgnoredTag("restaurant", "restaurant", "yes"),
    IgnoredTag("restaurant", "building", "commercial"),
    IgnoredTag("church", "building", "church")
  )

  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new IgnoredTagAnalyzer(context).analyze
  }
}

class IgnoredTagAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {
    val ignoredTagKeys = IgnoredTagAnalyzer.ignoredTagDefinitions.flatMap { ignoredTag =>
      if (context.poi.layers.head == ignoredTag.layer &&
        context.poi.tags.has(ignoredTag.key, ignoredTag.value)) {
        Some(ignoredTag.key)
      }
      else {
        None
      }
    }
    if (ignoredTagKeys.nonEmpty) {
      context.copy(ignoredTagKeys = context.ignoredTagKeys ++ ignoredTagKeys)
    }
    else {
      context
    }
  }
}
