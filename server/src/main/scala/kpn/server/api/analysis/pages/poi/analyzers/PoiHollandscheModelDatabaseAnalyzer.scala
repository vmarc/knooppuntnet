package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

object PoiHollandscheModelDatabaseAnalyzer extends PoiAnalyzer {
  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new PoiHollandscheModelDatabaseAnalyzer(context).analyze
  }
}

class PoiHollandscheModelDatabaseAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {
    val hollandscheMolenDatabase = context.poi.tags("dhm_id").map { id =>
      s"https://www.molens.nl/molen/zoek-een-molen/molendetail/?molenid=$id"
    }
    context.copy(
      analysis = context.analysis.copy(hollandscheMolenDatabase = hollandscheMolenDatabase),
      processedTagKeys = context.processedTagKeys :+ "dhm_id"
    )
  }

}
