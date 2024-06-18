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
      context.poi.tagValue("contact:email"),
      context.poi.tagValue("email")
    ).flatten.headOption

    val phone = Seq(
      context.poi.tagValue("contact:phone"),
      context.poi.tagValue("phone")
    ).flatten.headOption

    val fax = Seq(
      context.poi.tagValue("contact:fax"),
      context.poi.tagValue("fax")
    ).flatten.headOption

    val facebook = Seq(
      context.poi.tagValue("contact:facebook"),
      context.poi.tagValue("facebook")
    ).flatten.headOption

    val twitter = Seq(
      context.poi.tagValue("contact:twitter"),
      context.poi.tagValue("twitter")
    ).flatten.headOption

    context.copy(
      analysis = context.analysis.copy(
        email = email,
        phone = phone,
        fax = fax,
        facebook = facebook,
        twitter = twitter
      ),
      processedTagKeys = context.processedTagKeys ++ Seq(
        "contact:facebook",
        "facebook",
        "contact:twitter",
        "twitter",
        "contact:email",
        "email",
        "contact:phone",
        "phone",
        "contact:fax",
        "fax"
      )
    )
  }
}
