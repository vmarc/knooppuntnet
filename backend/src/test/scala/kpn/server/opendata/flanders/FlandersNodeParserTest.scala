package kpn.server.opendata.flanders

import kpn.core.util.UnitTest

import scala.xml.Elem
import scala.xml.InputSource
import scala.xml.XML

class FlandersNodeParserTest extends UnitTest {

  test("node parser") {

    val xml = loadCaseStudyXml()
    val nodes = new FlandersNodeParser().parse(xml, "knoop_wandel")

    assertEqual(
      nodes.map(withCoordinateTolerance),
      buildExpectedNodes().map(withCoordinateTolerance)
    )
  }

  private def loadCaseStudyXml(): Elem = {
    val stream = getClass.getResourceAsStream("/case-studies/toerisme-vlaanderen-nodes.xml")
    val inputSource = new InputSource(stream)
    XML.load(inputSource)
  }

  private def buildExpectedNodes(): Seq[FlandersNode] = {
    Seq(
      buildNode1(),
      buildNode2(),
      buildNode3(),
      buildNode4(),
      buildNode5(),
      buildNode6()
    )
  }

  private def buildNode1(): FlandersNode = {
    FlandersNode(
      "2735158",
      "60",
      "50.99726300884831",
      "4.20858961833763",
      virtual = false,
      "Toerisme Oost-Vlaanderen",
      "Brabantse Kouters",
      "2021-04-09",
      "http://www.tov.be/nl/routedokter"
    )
  }

  private def buildNode2(): FlandersNode = {
    FlandersNode(
      "2735159",
      "61",
      "50.9975777257217",
      "4.214554784957045",
      virtual = false,
      "Toerisme Oost-Vlaanderen",
      "Brabantse Kouters",
      "2021-04-09",
      "http://www.tov.be/nl/routedokter"
    )
  }

  private def buildNode3(): FlandersNode = {
    FlandersNode(
      "2875710",
      "420",
      "51.1841283623481",
      "5.36690510439659",
      virtual = false,
      "Regionaal Landschap Lage Kempen",
      "Wandelnetwerk Bosland",
      "2021-07-26",
      "meldingen@RLLK.be"
    )
  }

  private def buildNode4(): FlandersNode = {
    FlandersNode(
      "2875711",
      "441",
      "51.182422951898864",
      "5.3751689150630035",
      virtual = true,
      "Regionaal Landschap Lage Kempen",
      "Wandelnetwerk Bosland",
      "2021-07-26",
      "meldingen@RLLK.be"
    )
  }

  private def buildNode5(): FlandersNode = {
    FlandersNode(
      "3918832",
      "74",
      "51.24270625282907",
      "4.082555070978579",
      virtual = true,
      "Toerisme Oost-Vlaanderen",
      "Moervaartvallei",
      "2021-04-09",
      "http://www.tov.be/nl/routedokter"
    )
  }

  private def buildNode6(): FlandersNode = {
    FlandersNode(
      "3918833",
      "73",
      "51.24154095634826",
      "4.086756255820981",
      virtual = false,
      "Toerisme Oost-Vlaanderen",
      "Moervaartvallei",
      "2021-04-09",
      "http://www.tov.be/nl/routedokter"
    )
  }

  // compensate for slight variations in lambertToLatLon calculations during parsing
  private def withCoordinateTolerance(node: FlandersNode): FlandersNode = {
    node.copy(
      latitude = node.latitude.take(14),
      longitude = node.longitude.take(14)
    )
  }
}
