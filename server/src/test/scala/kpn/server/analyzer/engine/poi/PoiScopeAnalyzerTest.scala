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
    assert(poiScopeAnalyzer.inScope(LatLonImpl("51.23", "4.41")))
  }

  test("NL Amsterdam") {
    assert(poiScopeAnalyzer.inScope(LatLonImpl("52.37", "6.89")))
  }

  test("DE Koln") {
    assert(poiScopeAnalyzer.inScope(LatLonImpl("50.93", "6.97")))
  }

  test("DE Triberg (Germany South)") {
    assert(poiScopeAnalyzer.inScope(LatLonImpl("48.12", "8.23")))
  }

  test("DE Frankfurt (not in scope)") {
    assert(!poiScopeAnalyzer.inScope(LatLonImpl("50.11", "8.68")))
  }

  test("FR Lille (in bounding box)") {
    assert(poiScopeAnalyzer.inScope(LatLonImpl("50.63", "3.06")))
  }

  test("FR Paris (not in scope)") {
    assert(!poiScopeAnalyzer.inScope(LatLonImpl("48.86", "2.34")))
  }

  test("FR Sedan, within Belgium/Netherlands bounding box, but in France and not in scope") {
    assert(!poiScopeAnalyzer.inScope(LatLonImpl("49.70", "4.94")))
  }

  test("AU Judenburg") {
    assert(poiScopeAnalyzer.inScope(LatLonImpl("47.16", "14.66")))
  }

  test("ES Zaragoza") {
    assert(poiScopeAnalyzer.inScope(LatLonImpl("41.65", "-0.88")))
  }

  test("IT Rome") {
    assert(!poiScopeAnalyzer.inScope(LatLonImpl("41.90", "12.50")))
  }
}
