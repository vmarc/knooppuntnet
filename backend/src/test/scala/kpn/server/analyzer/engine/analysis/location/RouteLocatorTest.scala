package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.location.Location
import kpn.api.common.location.LocationCandidate
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy.load
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLinkAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteNodesAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteSegmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTypeAnalyzer

class RouteLocatorTest extends UnitTest {

  private val essen = Location(Seq("be", "be-1-10000", "be-2-11016"))
  private val kalmthout = Location(Seq("be", "be-1-10000", "be-2-11022"))
  private val roosendaal = Location(Seq("nl", "nl-1-nb", "nl-2-1674"))
  private val rucphen = Location(Seq("nl", "nl-1-nb", "nl-2-840"))
  private val woensdrecht = Location(Seq("nl", "nl-1-nb", "nl-2-873"))

  test("route 24-81 way based locator") {
    assertEqual(
      analyze(28184),
      RouteLocationAnalysis(
        Some(essen),
        Seq(
          LocationCandidate(essen, 65),
          LocationCandidate(roosendaal, 33),
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
  }

  test("route 55-95 way based locator") {
    assertEqual(
      analyze(19227),
      RouteLocationAnalysis(
        Some(rucphen),
        Seq(
          LocationCandidate(rucphen, 62),
          LocationCandidate(roosendaal, 20),
          LocationCandidate(essen, 18)
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
  }

  test("route 80-89 way based locator") {
    assertEqual(
      analyze(28182),
      RouteLocationAnalysis(
        Some(kalmthout),
        Seq(
          LocationCandidate(kalmthout, 82),
          LocationCandidate(essen, 18)
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

  private def analyze(routeId: Long): RouteLocationAnalysis = {

    val filename = s"/case-studies/$routeId.xml"
    val routeRelation = load(filename)

    val context1 = BaseRouteAnalysisContext(routeRelation, None)
    val context2 = BaseRouteTypeAnalyzer.analyze(context1)
    val context3 = BaseRouteNodesAnalyzer.analyze(context2)
    val context4 = BaseRouteLinkAnalyzer.analyze(context3)
    val baseRouteSegmentAnalyzer = new BaseRouteSegmentAnalyzer(context4)

    val segments = baseRouteSegmentAnalyzer.analyze()

    val locator = new RouteLocator(LocationAnalyzerTest.locationAnalyzer)
    locator.locate(segments)
  }
}
