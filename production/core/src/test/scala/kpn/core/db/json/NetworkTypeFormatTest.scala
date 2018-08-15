package kpn.core.db.json

import kpn.core.db.json.JsonFormats._
import kpn.shared.NetworkType
import kpn.shared.SharedTestObjects
import kpn.shared.route.RouteInfo
import org.scalatest.FunSuite
import org.scalatest.Matchers
import spray.json._

class NetworkTypeFormatTest extends FunSuite with Matchers with SharedTestObjects {

  test("Route NetworkType") {

    val bicycleRoute = newRoute(networkType = NetworkType.bicycle).toJson
    bicycleRoute.prettyPrint should include( """"networkType": "rcn"""")
    bicycleRoute.convertTo[RouteInfo].summary.networkType should equal(NetworkType.bicycle)

    val hikingRoute = newRoute(networkType = NetworkType.hiking).toJson
    hikingRoute.prettyPrint should include( """"networkType": "rwn"""")
    hikingRoute.convertTo[RouteInfo].summary.networkType should equal(NetworkType.hiking)
  }
}
