package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

object PoiWebsiteAnalyzer extends PoiAnalyzer {
  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new PoiWebsiteAnalyzer(context).analyze
  }
}

class PoiWebsiteAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {

    val website: Option[String] = {
      Seq(
        context.poi.tags("website"),
        context.poi.tags("contact:website"),
        context.poi.tags("url")
      ).flatten.headOption.map { url =>
        if (url.startsWith("http://") || url.startsWith("https://")) {
          url
        }
        else {
          "http://" + url
        }
      }
    }

    context.copy(
      analysis = context.analysis.copy(
        website = website
      ),
      processedTagKeys = context.processedTagKeys ++ Seq(
        "website",
        "contact:website",
        "url"
      )
    )
  }

}
