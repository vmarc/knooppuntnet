package kpn.server.analyzer.engine.analysis.location

import java.io.File

import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy
import kpn.shared.Location
import kpn.shared.LocationCandidate
import kpn.shared.RouteLocationAnalysis
import kpn.shared.SharedTestObjects
import kpn.shared.route.RouteInfo
import org.scalatest.FunSuite
import org.scalatest.Matchers

class RouteWayBasedLocatorTest extends FunSuite with Matchers with SharedTestObjects {

  val essen = Location(Seq("Belgium", "Flanders", "Antwerp", "Antwerp", "Essen"))
  val kalmthout = Location(Seq("Belgium", "Flanders", "Antwerp", "Antwerp", "Kalmthout"))
  val roosendaal = Location(Seq("Netherlands", "North Brabant", "Roosendaal"))
  val rucphen = Location(Seq("Netherlands", "North Brabant", "Rucphen"))
  val woensdrecht = Location(Seq("Netherlands", "North Brabant", "Woensdrecht"))

  test("way based locator") {

    val locator = {
      val configuration = readLocationConfiguration()
      // val locationConfiguration = new LocationConfigurationReader().read()
      new RouteWayBasedLocatorImpl(configuration)
    }

    // route 24-81
    locator.locate(route("28184")) should equal(
      Some(
        RouteLocationAnalysis(
          essen,
          Seq(
            LocationCandidate(essen, 68),
            LocationCandidate(roosendaal, 30),
            LocationCandidate(woensdrecht, 2)
          )
        )
      )
    )

    // route 55-95
    locator.locate(route("19227")) should equal(
      Some(
        RouteLocationAnalysis(
          rucphen,
          Seq(
            LocationCandidate(rucphen, 61),
            LocationCandidate(roosendaal, 23),
            LocationCandidate(essen, 16)
          )
        )
      )
    )

    // route 80-89
    locator.locate(route("28182")) should equal(
      Some(
        RouteLocationAnalysis(
          kalmthout,
          Seq(
            LocationCandidate(kalmthout, 85),
            LocationCandidate(essen, 15)
          )
        )
      )
    )
  }

  private def readLocationConfiguration(): LocationConfiguration = {

    val be = {
      val essen = location("be/Essen_964003_AL8.GeoJson")
      val kalmthout = location("be/Kalmthout_1284337_AL8.GeoJson")
      val antwerp7 = location("be/Antwerp_1902793_AL7.GeoJson", Seq(essen, kalmthout))
      val antwerp6 = location("be/Antwerp_53114_AL6.GeoJson", Seq(antwerp7))
      val flanders = location("be/Flanders_53134_AL4.GeoJson", Seq(antwerp6))
      location("be/Belgium_52411_AL2.GeoJson", Seq(flanders))
    }

    val nl = {
      val roosendaal = location("nl/Roosendaal_2078302_AL8.GeoJson")
      val rucphen = location("nl/Rucphen_2078299_AL8.GeoJson")
      val woensdrecht = location("nl/Woensdrecht_2078304_AL8.GeoJson")
      val northBrabant = location("nl/North Brabant_47696_AL4.GeoJson", Seq(roosendaal, rucphen, woensdrecht))
      location("nl/Netherlands_47796_AL3.GeoJson", Seq(northBrabant))
    }

    val de = location("de/Germany_51477_AL2.GeoJson")

    LocationConfiguration(Seq(nl, be, de))
  }

  private def location(name: String, children: Seq[LocationDefinition] = Seq.empty): LocationDefinition = {
    val filename = "/kpn/conf/locations/" + name
    val file = new File(filename)
    new LocationDefinitionReader(file).read(children)
  }

  private def route(routeId: String): RouteInfo = {
    CaseStudy.routeAnalysis(routeId).route
  }

}
