package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLonImpl
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import org.scalatest.FunSuite
import org.scalatest.Matchers

class PoiScopeAnalyzerTest extends FunSuite with Matchers {

  val poiScopeAnalyzer: PoiScopeAnalyzer = {
    val analysisContext = new AnalysisContext()
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
    new PoiScopeAnalyzerImpl(countryAnalyzer)
  }

  test("Antwerp") {
    poiScopeAnalyzer.inScope(LatLonImpl("51.23", "4.41")) should equal(true)
  }

  test("Amsterdam") {
    poiScopeAnalyzer.inScope(LatLonImpl("52.37", "6.89")) should equal(true)
  }

  test("Koln") {
    poiScopeAnalyzer.inScope(LatLonImpl("50.93", "6.97")) should equal(false) // DE currently not in scope
  }

  test("Paris") {
    poiScopeAnalyzer.inScope(LatLonImpl("48.86", "2.34")) should equal(false) // FR currently not in scope
  }

  test("Frankfurt") {
    poiScopeAnalyzer.inScope(LatLonImpl("50.11", "8.68")) should equal(false) // DE currently not in scope
  }

  test("Rome") {
    poiScopeAnalyzer.inScope(LatLonImpl("41.90", "12.50")) should equal(false)
  }
}
