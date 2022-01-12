package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.SharedTestObjects
import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate
import kpn.core.doc.RouteDoc
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy

class RouteLocatorTest extends UnitTest with SharedTestObjects {

  private val essen = Location(Seq("be", "be-1-10000", "be-2-11016"))
  private val kalmthout = Location(Seq("be", "be-1-10000", "be-2-11022"))
  private val roosendaal = Location(Seq("nl", "nl-1-nb", "nl-2-1674"))
  private val rucphen = Location(Seq("nl", "nl-1-nb", "nl-2-840"))
  private val woensdrecht = Location(Seq("nl", "nl-1-nb", "nl-2-873"))

  test("way based locator") {

    val locator = new RouteLocatorImpl(LocationAnalyzerTest.locationAnalyzer)

    // route 24-81
    locator.locate(route("28184").analysis.map).shouldMatchTo(
      RouteLocationAnalysis(
        Some(essen),
        Seq(
          LocationCandidate(essen, 68),
          LocationCandidate(roosendaal, 30),
          LocationCandidate(woensdrecht, 2)
        ),
        Seq(
          "be",
          "be-1-10000", // Antwerp province
          "be-2-11016", // Essen
          "nl",
          "nl-1-nb", // North Brabant
          "nl-2-1674", // Roosendaal
          "nl-2-873" // Woensdrecht
        )
      )
    )

    // route 55-95
    locator.locate(route("19227").analysis.map).shouldMatchTo(
      RouteLocationAnalysis(
        Some(rucphen),
        Seq(
          LocationCandidate(rucphen, 61),
          LocationCandidate(roosendaal, 23),
          LocationCandidate(essen, 16)
        ),
        Seq(
          "be",
          "be-1-10000", // Antwerp province
          "be-2-11016", // Essen
          "nl",
          "nl-1-nb", // North Brabant
          "nl-2-1674", // Roosendaal
          "nl-2-840" // Rucphen
        )
      )
    )

    // route 80-89
    locator.locate(route("28182").analysis.map).shouldMatchTo(
      RouteLocationAnalysis(
        Some(kalmthout),
        Seq(
          LocationCandidate(kalmthout, 85),
          LocationCandidate(essen, 15)
        ),
        Seq(
          "be",
          "be-1-10000", // Antwerp province
          "be-2-11016", // Essen
          "be-2-11022" // Kalmthout
        )
      )
    )
  }

  private def route(routeId: String): RouteDoc = {
    CaseStudy.routeAnalysis(routeId).route
  }
}
