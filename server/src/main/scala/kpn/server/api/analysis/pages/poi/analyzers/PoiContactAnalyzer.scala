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

    val fax = Seq(
      context.poi.tags("contact:fax"),
      context.poi.tags("fax")
    ).flatten.headOption

    val facebook = Seq(
      context.poi.tags("contact:facebook"),
        context.poi.tags("facebook")
    ).flatten.headOption

    val twitter = Seq(
      context.poi.tags("contact:twitter"),
      context.poi.tags("twitter")
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
