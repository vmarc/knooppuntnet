package kpn.core.poi

import kpn.core.engine.analysis.country.CountryAnalyzerImpl
import kpn.shared.LatLonImpl
import org.scalatest.FunSuite
import org.scalatest.Matchers

class PoiLocationFilterTest extends FunSuite with Matchers {

  val locationFilter = new PoiLocationFilterImpl(new CountryAnalyzerImpl())

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
