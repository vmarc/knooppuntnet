package kpn.server.opendata.netherlands

import kpn.api.common.LatLonImpl
import kpn.core.util.UnitTest

class RoutedatabankRouteParserTest extends UnitTest {

  test("parse routedatabank routes") {

    val filename = s"/case-studies/routedatabank-routes.json"
    val inputStream = getClass.getResourceAsStream(filename)
    val routes = new RoutedatabankRouteParser().parse(inputStream)

    routes shouldMatchTo Seq(
      RoutedatabankRoute(
        "211357",
        Some("2019-11-21"),
        "Leiden",
        "Zuid-Holland",
        Seq(
          LatLonImpl("52.1716429", "4.46856203"),
          LatLonImpl("52.17166265", "4.46857649"),
        )
      )
    )
  }
}
