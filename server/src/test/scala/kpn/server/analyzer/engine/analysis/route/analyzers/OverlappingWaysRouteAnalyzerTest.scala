package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.SharedTestObjects
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OverlappingWaysRouteAnalyzerTest extends FunSuite with Matchers with SharedTestObjects {

  test("non-overlapping ways") {

    val context = analyze(
      new RouteTestData("01-02") {
        node(1001)
        node(1002)
        node(1003)
        node(1004)
        node(1005)
        node(1006)
        memberWay(101, "", 1, 2, 3)
        memberWay(102, "", 3, 4, 5)
        memberWay(103, "", 5, 6)
      }
    )

    context.hasFact(Fact.RouteOverlappingWays) should equal(false)
    context.overlappingWays should equal(Some(Seq()))
  }

  test("overlapping ways") {

    val context = analyze(
      new RouteTestData("01-02") {
        node(1001)
        node(1002)
        node(1003)
        node(1004)
        node(1005)
        node(1006)
        memberWay(101, "", 1, 2, 3)
        memberWay(102, "", 2, 3, 4)
        memberWay(103, "", 4, 5, 6)
      }
    )

    context.hasFact(Fact.RouteOverlappingWays) should equal(true)
    context.overlappingWays should equal(Some(Seq(Overlap(101, 102))))
  }

  test("multiple overlapping ways") {

    val context = analyze(
      new RouteTestData("01-02") {
        node(1001)
        node(1002)
        node(1003)
        node(1004)
        node(1005)
        node(1006)
        memberWay(101, "", 1, 2, 3)
        memberWay(102, "", 3, 2, 1)
        memberWay(103, "", 2, 3, 4, 5, 6)
      }
    )

    context.hasFact(Fact.RouteOverlappingWays) should equal(true)
    context.overlappingWays should equal(Some(Seq(Overlap(101, 102), Overlap(101, 103), Overlap(102, 103))))
  }

  test("ways with just a single node do not cause a problem in the overlap analysis logic") {

    val context = analyze(
      new RouteTestData("01-02") {
        node(1001)
        memberWay(101, "", 1)
      }
    )

    context.hasFact(Fact.RouteOverlappingWays) should equal(false)
    context.overlappingWays should equal(Some(Seq()))
  }

  test("route without ways does not cause a problem in the overlap analysis logic") {
    val context = analyze(new RouteTestData("01-02"))
    context.hasFact(Fact.RouteOverlappingWays) should equal(false)
    context.overlappingWays should equal(Some(Seq()))
  }

  def analyze(routeTestData: RouteTestData): RouteAnalysisContext = {

    val data = routeTestData.data

    val loadedRoute = LoadedRoute(
      country = None,
      networkType = NetworkType.hiking,
      "01-02",
      data,
      data.relations(1L)
    )

    val analysisContext = new AnalysisContext(oldTagging = false)

    val context = RouteAnalysisContext(
      analysisContext,
      networkNodes = Map.empty,
      loadedRoute,
      orphan = false
    )

    OverlappingWaysRouteAnalyzer.analyze(context)
  }

}
