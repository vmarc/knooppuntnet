package kpn.core.engine.analysis.country

import kpn.shared.Country
import kpn.shared.LatLon
import kpn.shared.SharedTestObjects
import org.scalatest.FunSuite
import org.scalatest.Matchers

class CountryAnalyzerTest extends FunSuite with Matchers with SharedTestObjects {

  private val analyzer = new CountryAnalyzerImpl()

  private val be1 = node("51.47464736069959", "4.478302001953125")
  private val be2 = node("51.43563788497879", "4.941433668136596")
  private val nl1 = node("51.48170361107213", "4.4769287109375")
  private val nl2 = node("51.43948683099483", "4.931525588035583")
  private val de1 = node("50.36999258287717", "6.7291259765625")
  private val unknown1 = node("1", "1")
  private val unknown2 = node("2", "2")

  test("regular nodes") {
    analyzer.countries(be1) should equal(Seq(Country.be))
    analyzer.countries(nl1) should equal(Seq(Country.nl))
    analyzer.countries(de1) should equal(Seq(Country.de))
  }

  test("outer 1 Baarle Nassau") {
    analyzer.countries(node("51.43581846832453", "4.926767349243164")) should equal(Seq(Country.be))
  }

  test("outer 2 Baarle Nassau") {
    analyzer.countries(node("51.43563788497879", "4.941433668136596")) should equal(Seq(Country.be))
  }

  test("inner in be outer Baarle Nassau") {
    analyzer.countries(node("51.43948683099483", "4.931525588035583")) should equal(Seq(Country.nl))
  }

  test("country be1") {
    analyzer.country(Seq(be1)) should equal(Some(Country.be))
  }

  test("country nl1") {
    analyzer.country(Seq(nl1)) should equal(Some(Country.nl))
  }

  test("country be1, be2, nl1") {
    analyzer.country(Seq(be1, be2, nl1)) should equal(Some(Country.be))
  }

  test("country be1, nl1, nl2") {
    analyzer.country(Seq(be1, nl1, nl2)) should equal(Some(Country.nl))
  }

  test("country be1, be2, nl1, nl2") {
    analyzer.country(Seq(be1, be2, nl1, nl2)) should equal(Some(Country.be))
  }

  test("country nl1, nl2, be1, be2") {
    analyzer.country(Seq(nl1, nl2, be1, be2)) should equal(Some(Country.be))
  }

  test("country unknown1") {
    analyzer.country(Seq(unknown1)) should equal(None)
  }

  test("country be1, unknown1n unknwon2") {
    analyzer.country(Seq(be1, unknown1, unknown2)) should equal(Some(Country.be))
  }

  private def node(latitude: String, longitude: String): LatLon = {
    newRawNode(latitude = latitude, longitude = longitude)
  }
}
