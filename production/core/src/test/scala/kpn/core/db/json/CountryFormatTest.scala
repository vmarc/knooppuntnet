package kpn.core.db.json

import kpn.core.db.json.JsonFormats._
import kpn.shared.Country
import kpn.shared.SharedTestObjects
import kpn.shared.route.RouteInfo
import org.scalatest.FunSuite
import org.scalatest.Matchers
import spray.json._

class CountryFormatTest extends FunSuite with Matchers with SharedTestObjects {

  test("Route Option[Country]") {

    val beRoute = newRoute(country = Some(Country.be)).toJson
    beRoute.prettyPrint should include( """"country": "be"""")
    beRoute.convertTo[RouteInfo].summary.country should equal(Some(Country.be))

    val nlRoute = newRoute(country = Some(Country.nl)).toJson
    nlRoute.prettyPrint should include( """"country": "nl"""")
    nlRoute.convertTo[RouteInfo].summary.country should equal(Some(Country.nl))

    val unknownCountryRoute = newRoute(country = None).toJson
    unknownCountryRoute.prettyPrint should include( """"country": """)
    unknownCountryRoute.convertTo[RouteInfo].summary.country should equal(None)
  }
}
