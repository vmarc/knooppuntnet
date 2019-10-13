package kpn.core.poi

import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.AnalysisContext
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.shared.LatLonImpl
import org.scalatest.FunSuite
import org.scalatest.Matchers

class PoiLocationFilterTest extends FunSuite with Matchers {

  val locationFilter: PoiLocationFilter = {
    val analysisContext = new AnalysisContext()
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
    new PoiLocationFilterImpl(countryAnalyzer)
  }

  test("Antwerp") {
    locationFilter.filter(LatLonImpl("51.23", "4.41")) should equal(true)
  }

  test("Koln") {
    locationFilter.filter(LatLonImpl("50.93", "6.97")) should equal(true)
  }

  test("Amsterdam") {
    locationFilter.filter(LatLonImpl("52.37", "6.89")) should equal(true)
  }

  test("Paris") {
    locationFilter.filter(LatLonImpl("48.86", "2.34")) should equal(false)
  }

  test("Frankfurt") {
    locationFilter.filter(LatLonImpl("50.11", "8.68")) should equal(false)
  }

}
