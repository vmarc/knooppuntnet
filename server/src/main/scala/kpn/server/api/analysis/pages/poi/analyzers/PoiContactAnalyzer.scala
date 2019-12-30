package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

object PoiContactAnalyzer extends PoiAnalyzer {
  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new PoiContactAnalyzer(context).analyze
  }
}

class PoiContactAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {

    val email = Seq(
      context.poi.tags("contact:email"),
      context.poi.tags("email")
    ).flatten.headOption

    val phone = Seq(
      context.poi.tags("contact:phone"),
      context.poi.tags("phone")
    ).flatten.headOption

    context.copy(
      analysis = context.analysis.copy(
        email = email,
        phone = phone
      ),
      processedTagKeys = context.processedTagKeys ++ Seq(
        "contact:email",
        "email",
        "contact:phone",
        "phone"
      )
    )
  }

}
