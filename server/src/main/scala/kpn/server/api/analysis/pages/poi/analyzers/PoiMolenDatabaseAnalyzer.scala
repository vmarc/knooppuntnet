package kpn.server.api.analysis.pages.poi.analyzers

import kpn.server.api.analysis.pages.poi.PoiAnalysisContext
import kpn.server.api.analysis.pages.poi.PoiAnalyzer

object PoiMolenDatabaseAnalyzer extends PoiAnalyzer {
  def analyze(context: PoiAnalysisContext): PoiAnalysisContext = {
    new PoiMolenDatabaseAnalyzer(context).analyze
  }
}

class PoiMolenDatabaseAnalyzer(context: PoiAnalysisContext) {

  def analyze: PoiAnalysisContext = {

    val molenDatabase = context.poi.tags("mdb_id").map { id =>
      s"http://www.molendatabase.nl/nederland/molen.php?nummer=$id"
    }

    context.copy(
      processedTagKeys = context.processedTagKeys :+ "mdb_id",
      analysis = context.analysis.copy(
        molenDatabase = molenDatabase
      )
    )
  }
}
