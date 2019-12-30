package kpn.server.api.analysis.pages.poi

@Component
class MasterPoiAnalyzerImpl extends MasterPoiAnalyzer {

  def analyze(poi: Poi): PoiAnalysisContext = {

    val ignoredTagKeys = Seq(
      "source",
      "source:date",
      "ref:bag",
      "ref:rce",
      "start_date"
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
