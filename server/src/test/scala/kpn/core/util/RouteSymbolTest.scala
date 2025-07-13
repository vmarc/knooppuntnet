package kpn.core.util

import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newRelation

class RouteSymbolTest extends UnitTest {

  test("no symbol if only waycolor is present in symbol description") {
    symbol("route-color:") should equal(None)
  }

  test("symbol") {
    symbol("route-color:white") should equal(Some("route-color:white"))
    symbol("route-color:white:") should equal(Some("route-color:white:"))
  }

  private def symbol(symbolTagValue: String): Option[String] = {
    val relation = newRelation(tags = Tags.from("osmc:symbol" -> symbolTagValue))
    RouteSymbol.from(relation)
  }
}
