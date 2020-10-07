package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLonImpl
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext

class PoiScopeAnalyzerTest extends UnitTest {

  val poiScopeAnalyzer: PoiScopeAnalyzer = {
    val analysisContext = new AnalysisContext()
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
    new PoiScopeAnalyzerImpl(countryAnalyzer)
  }

  test("BE Antwerp") {
    poiScopeAnalyzer.inScope(LatLonImpl("51.23", "4.41")) should equal(true)
  }

  test("NL Amsterdam") {
    poiScopeAnalyzer.inScope(LatLonImpl("52.37", "6.89")) should equal(true)
  }

  test("DE Koln") {
    poiScopeAnalyzer.inScope(LatLonImpl("50.93", "6.97")) should equal(true)
  }

  test("DE Triberg (Germany South)") {
    poiScopeAnalyzer.inScope(LatLonImpl("48.12", "8.23")) should equal(true)
  }

  test("DE Frankfurt (not in scope)") {
    poiScopeAnalyzer.inScope(LatLonImpl("50.11", "8.68")) should equal(false)
  }

  test("FR Lille (in bounding box)") {
    poiScopeAnalyzer.inScope(LatLonImpl("50.63", "3.06")) should equal(true)
  }

  test("FR Paris (not in scope)") {
    poiScopeAnalyzer.inScope(LatLonImpl("48.86", "2.34")) should equal(false)
  }

  test("FR Sedan, within Belgium/Netherlands bounding box, but in France and not in scope") {
    poiScopeAnalyzer.inScope(LatLonImpl("49.70", "4.94")) should equal(false)
  }

  test("AU Judenburg") {
    poiScopeAnalyzer.inScope(LatLonImpl("47.16", "14.66")) should equal(true)
  }

  test("ES Zaragoza") {
    poiScopeAnalyzer.inScope(LatLonImpl("41.65", "-0.88")) should equal(true)
  }

  test("IT Rome") {
    poiScopeAnalyzer.inScope(LatLonImpl("41.90", "12.50")) should equal(false)
  }
}
