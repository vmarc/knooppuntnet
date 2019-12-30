package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

object OnroerendErfgoedAnalyzer extends PoiAnalyzer {
  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new OnroerendErfgoedAnalyzer(context).analyze
  }
}

class OnroerendErfgoedAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {
    context.poi.tags("ref:OnroerendErfgoed") match {
      case Some(ref) => processRef(ref)
      case _ => context
    }
  }

  private def processRef(ref: String): PoiAnalysisContext = {

    val onroerendErfgoed = s"https://inventaris.onroerenderfgoed.be/dibe/relict/$ref"
    context.copy(
      analysis = context.analysis.copy(onroerendErfgoed = Some(onroerendErfgoed)),
      processedTagKeys = context.processedTagKeys ++ Seq("ref:OnroerendErfgoed"),
      ignoredTagKeys = context.ignoredTagKeys ++ Seq(
        "OnroerendErfgoed:criteria",
        "heritage",
        "heritage:operator",
        "heritage:website"
      )
    )
  }
}
