package kpn.server.opendata.flanders

import kpn.api.common.LatLonImpl
import kpn.core.util.UnitTest

import scala.xml.Elem
import scala.xml.InputSource
import scala.xml.XML

class FlandersRouteParserTest extends UnitTest {

  test("route parser") {

    val xml = loadCaseStudyXml()
    val routes = new FlandersRouteParser().parse(xml, "traject_wandel")

    assertEqual(
      routes.map(withCoordinateTolerance),
      buildExpectedRoutes().map(withCoordinateTolerance)
    )
  }

  private def loadCaseStudyXml(): Elem = {
    val stream = getClass.getResourceAsStream("/case-studies/toerisme-vlaanderen-routes.xml")
    val inputSource = new InputSource(stream)
    XML.load(inputSource)
  }

  private def buildExpectedRoutes(): Seq[FlandersRoute] = {
    Seq(
      buildRoute1(),
      buildRoute2(),
      buildRoute3(),
    )
  }

  private def buildRoute1() = {
    FlandersRoute(
      "2735158-2735159",
      "2735158",
      "2735159",
      virtual = false,
      buildCoordinates(List(
        ("50.99726300884831", "4.20858961833763"),
        ("50.99743014977333", "4.209364062541376"),
        ("50.99746637491434", "4.209563370628709"),
        ("50.99762863845031", "4.2099047042556705"),
        ("50.99773692826578", "4.2102874346433845"),
        ("50.997782972381046", "4.210475948086235"),
        ("50.99781606374201", "4.210665788898985"),
        ("50.99761617092074", "4.210751930982354"),
        ("50.997641109797684", "4.210915665999323"),
        ("50.9972053673487", "4.211088067115046"),
        ("50.99746439175949", "4.211942827102273"),
        ("50.997483131107444", "4.2122620491603815"),
        ("50.997547870825564", "4.2127407620060175"),
        ("50.9976036795636", "4.213393661951592"),
        ("50.99763187831998", "4.213945062385732"),
        ("50.9975777257217", "4.214554784957045")
      )),
      "Toerisme Oost-Vlaanderen",
      "Brabantse Kouters",
      "2021-05-25",
      "http://www.tov.be/nl/routedokter"
    )
  }

  private def buildRoute2() = {
    FlandersRoute(
      "2875710-2875711",
      "2875710",
      "2875711",
      virtual = false,
      buildCoordinates(List(
        ("51.1841283623481", "5.36690510439659"),
        ("51.1838229476952", "5.36835420514106"),
        ("51.18363345968425", "5.369253672981231"),
        ("51.183440712243375", "5.370168738425829"),
        ("51.18324423832358", "5.37110682863599"),
        ("51.18304776426817", "5.372037490720702"),
        ("51.182861997817675", "5.372923588977698"),
        ("51.18269718144325", "5.373701989267854"),
        ("51.18252631235325", "5.374620025103075"),
        ("51.18242295189887", "5.3751689150630035")
      )),
      "Regionaal Landschap Lage Kempen",
      "Wandelnetwerk Bosland",
      "2021-07-25",
      "meldingen@RLLK.be"
    )
  }

  private def buildRoute3() = {
    FlandersRoute(
      "3918832-3918833",
      "3918832",
      "3918833",
      virtual = true,
      buildCoordinates(List(
        ("51.24270625282907", "4.082555070978579"),
        ("51.242297740067414", "4.083974523189664"),
        ("51.24208707075206", "4.084902113815581"),
        ("51.24190199417362", "4.085501091306413"),
        ("51.24154095634826", "4.086756255820981")
      )),
      "Toerisme Oost-Vlaanderen",
      "Moervaartvallei",
      "2021-05-25",
      "http://www.tov.be/nl/routedokter"
    )
  }

  private def buildCoordinates(coords: List[(String, String)]): List[LatLonImpl] = {
    coords.map { case (lat, lon) => LatLonImpl(lat, lon) }
  }

  // compensate for slight variations in lambertToLatLon calculations during parsing
  private def withCoordinateTolerance(route: FlandersRoute): FlandersRoute = {
    route.copy(
      coordinates = route.coordinates.map(c => LatLonImpl(c.latitude.take(14), c.longitude.take(14)))
    )
  }
}
