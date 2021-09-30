package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.SharedTestObjects
import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate
import kpn.core.doc.RouteDoc
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy

class RouteLocatorTest extends UnitTest with SharedTestObjects {

  private val essen = Location(Seq("be", "Flanders", "Antwerp province", "Antwerp arrondissement", "Essen BE"))
  private val kalmthout = Location(Seq("be", "Flanders", "Antwerp province", "Antwerp arrondissement", "Kalmthout"))
  private val roosendaal = Location(Seq("nl", "North Brabant", "Roosendaal"))
  private val rucphen = Location(Seq("nl", "North Brabant", "Rucphen"))
  private val woensdrecht = Location(Seq("nl", "North Brabant", "Woensdrecht"))

  test("way based locator") {

    val locator = new RouteLocatorImpl(new LocationConfigurationReader().read())

    // route 24-81
    locator.locate(route("28184").analysis.map) should matchTo(
      RouteLocationAnalysis(
        Some(essen),
        Seq(
          LocationCandidate(essen, 68),
          LocationCandidate(roosendaal, 30),
          LocationCandidate(woensdrecht, 2)
        ),
        Seq(
          "Antwerp arrondissement",
          "Antwerp province",
          "Essen BE",
          "Flanders",
          "North Brabant",
          "Roosendaal",
          "Woensdrecht",
          "be",
          "nl"
        )
      )
    )

    // route 55-95
    locator.locate(route("19227").analysis.map) should matchTo(
      RouteLocationAnalysis(
        Some(rucphen),
        Seq(
          LocationCandidate(rucphen, 61),
          LocationCandidate(roosendaal, 23),
          LocationCandidate(essen, 16)
        ),
        Seq(
          "Antwerp arrondissement",
          "Antwerp province",
          "Essen BE",
          "Flanders",
          "North Brabant",
          "Roosendaal",
          "Rucphen",
          "be",
          "nl"
        )
      )
    )

    // route 80-89
    locator.locate(route("28182").analysis.map) should matchTo(
      RouteLocationAnalysis(
        Some(kalmthout),
        Seq(
          LocationCandidate(kalmthout, 85),
          LocationCandidate(essen, 15)
        ),
        Seq(
          "Antwerp arrondissement",
          "Antwerp province",
          "Essen BE",
          "Flanders",
          "Kalmthout",
          "be"
        )
      )
    )
  }

  private def route(routeId: String): RouteDoc = {
    CaseStudy.routeAnalysis(routeId).route
  }

}
