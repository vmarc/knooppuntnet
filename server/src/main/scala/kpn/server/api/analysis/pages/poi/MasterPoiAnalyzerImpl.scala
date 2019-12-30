package kpn.server.api.analysis.pages.poi

import kpn.api.common.Poi
import kpn.api.common.PoiAnalysis
import kpn.api.custom.Tag
import kpn.server.api.analysis.pages.poi.analyzers.PoiAddressAnalyzer
import kpn.server.api.analysis.pages.poi.analyzers.PoiContactAnalyzer
import kpn.server.api.analysis.pages.poi.analyzers.PoiHollandscheModelDatabaseAnalyzer
import kpn.server.api.analysis.pages.poi.analyzers.PoiImageAnalyzer
import kpn.server.api.analysis.pages.poi.analyzers.PoiMolenDatabaseAnalyzer
import kpn.server.api.analysis.pages.poi.analyzers.PoiTitleAnalyzer
import kpn.server.api.analysis.pages.poi.analyzers.PoiWebsiteAnalyzer
import kpn.server.api.analysis.pages.poi.analyzers.PoiWheelchairAnalyzer
import kpn.server.api.analysis.pages.poi.analyzers.PoiWikidataAnalyzer
import kpn.server.api.analysis.pages.poi.analyzers.PoiWikipediaAnalyzer
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class MasterPoiAnalyzerImpl extends MasterPoiAnalyzer {

  def analyze(poi: Poi): PoiAnalysisContext = {

    val ignoredTagKeys = Seq(
      "source",
      "source:date",
      "ref:bag",
      "ref:rce",
      "start_date",
      "building:levels"
    )

    val ignoredTagKeyValues = Seq(
      Tag("building", "yes")
    )

    val analysis = PoiAnalysis()
    val context = PoiAnalysisContext(
      poi,
      Seq.empty,
      ignoredTagKeys,
      ignoredTagKeyValues,
      analysis
    )

    val analyzers: List[PoiAnalyzer] = List(
      PoiAddressAnalyzer,
      PoiContactAnalyzer,
      PoiHollandscheModelDatabaseAnalyzer,
      PoiImageAnalyzer,
      PoiMolenDatabaseAnalyzer,
      PoiTitleAnalyzer,
      PoiWebsiteAnalyzer,
      PoiWheelchairAnalyzer,
      PoiWikidataAnalyzer,
      PoiWikipediaAnalyzer
    )

    doAnalyze(analyzers, context)
  }

  @tailrec
  private def doAnalyze(analyzers: List[PoiAnalyzer], context: PoiAnalysisContext): PoiAnalysisContext = {
    if (analyzers.isEmpty) {
      context
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}
