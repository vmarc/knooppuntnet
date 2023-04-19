package kpn.server.opendata.flanders

import kpn.core.util.UnitTest

import scala.xml.InputSource
import scala.xml.XML

class FlandersNodeParserTest extends UnitTest {

  test("node parser") {
    val filename = "/case-studies/toerisme-vlaanderen-nodes.xml"
    val stream = getClass.getResourceAsStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    val nodes = new FlandersNodeParser().parse(xml, "knoop_wandel")

    val node1 = FlandersNode(
      "2735158",
      "60",
      "50.99726300884831",
      "4.20858961833763",
      "Toerisme Oost-Vlaanderen",
      "Brabantse Kouters",
      "2021-04-09",
      "http://www.tov.be/nl/routedokter"
    )

    val node2 = FlandersNode(
      "2735159",
      "61",
      "50.9975777257217",
      "4.214554784957045",
      "Toerisme Oost-Vlaanderen",
      "Brabantse Kouters",
      "2021-04-09",
      "http://www.tov.be/nl/routedokter"
    )

    val node3 = FlandersNode(
      "2875710",
      "420",
      "51.1841283623481",
      "5.36690510439659",
      "Regionaal Landschap Lage Kempen",
      "Wandelnetwerk Bosland",
      "2021-07-26",
      "meldingen@RLLK.be"
    )

    val node4 = FlandersNode(
      "2875711",
      "441",
      "51.18242295189887",
      "5.3751689150630035",
      "Regionaal Landschap Lage Kempen",
      "Wandelnetwerk Bosland",
      "2021-07-26",
      "meldingen@RLLK.be"
    )

    val node5 = FlandersNode(
      "3918832",
      "74",
      "51.24270625282907",
      "4.082555070978579",
      "Toerisme Oost-Vlaanderen",
      "Moervaartvallei",
      "2021-04-09",
      "http://www.tov.be/nl/routedokter"
    )

    val node6 = FlandersNode(
      "3918833",
      "73",
      "51.24154095634826",
      "4.086756255820981",
      "Toerisme Oost-Vlaanderen",
      "Moervaartvallei",
      "2021-04-09",
      "http://www.tov.be/nl/routedokter"
    )

    nodes shouldMatchTo Seq(
      node1,
      node2,
      node3,
      node4,
      node5,
      node6
    )
  }
}
