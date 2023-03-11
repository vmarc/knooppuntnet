package kpn.server.opendata.netherlands

import kpn.core.util.UnitTest

class RoutedatabankNodeParserTest extends UnitTest {

  test("parse routedatabank nodes") {

    val filename = s"/case-studies/routedatabank-nodes.json"
    val inputStream = getClass.getResourceAsStream(filename)
    val nodes = new RoutedatabankNodeParser().parse(inputStream)

    nodes shouldMatchTo Seq(
      RoutedatabankNode(
        "162757",
        "57",
        "52.07450141",
        "5.64449968",
        "Gelderland",
        Some("2019-11-19"),
        "1",
        "keuzepunt",
        "Lunteren"
      )
    )
  }
}
