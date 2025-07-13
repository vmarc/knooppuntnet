package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.core.test.TestObjects.newRouteNodeAnalysis
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodesAnalysis

class RouteNameFromNodesAnalyzerTest extends UnitTest {

  test("derive route name from node names") {
    val context = BaseRouteAnalysisContext(
      null,
      None,
      nodeNetwork = true,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          startNode = Some(newRouteNodeAnalysis(1001, "01")),
          endNode = Some(newRouteNodeAnalysis(1002, "02")),
        )
      ),
    )
    val newContext = BaseRouteNameFromNodesAnalyzer.analyze(context)
    assertEqual(
      newContext.routeNameAnalysis,
      RouteNameAnalysis(
        name = Some("01-02"),
        derivedFromNodes = true
      )
    )
  }

  test("derive route name from non-digit node names (use separator with spaces)") {
    val context = BaseRouteAnalysisContext(
      null,
      None,
      nodeNetwork = true,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          startNode = Some(newRouteNodeAnalysis(1001, "a")),
          endNode = Some(newRouteNodeAnalysis(1002, "b")),
        )
      )
    )
    val newContext = BaseRouteNameFromNodesAnalyzer.analyze(context)
    assertEqual(
      newContext.routeNameAnalysis,
      RouteNameAnalysis(
        name = Some("a - b"),
        derivedFromNodes = true
      )
    )
  }

  test("do not derive route name from node names if route name already known") {
    val context = BaseRouteAnalysisContext(
      null,
      None,
      _routeNameAnalysis = Some(
        RouteNameAnalysis(name = Some("route-name"))
      ),
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          startNode = Some(newRouteNodeAnalysis(1001, "01")),
          endNode = Some(newRouteNodeAnalysis(1002, "02")),
        )
      )
    )
    val newContext = BaseRouteNameFromNodesAnalyzer.analyze(context)
    assertEqual(
      newContext.routeNameAnalysis,
      RouteNameAnalysis(
        name = Some("route-name"),
      )
    )
  }

  test("do not derive route name from node names if start node name unknown") {
    val context = BaseRouteAnalysisContext(
      null,
      None,
      _routeNameAnalysis = Some(
        RouteNameAnalysis()
      ),
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          startNode = None,
          endNode = Some(newRouteNodeAnalysis(1002, "02")),
        )
      )
    )
    val newContext = BaseRouteNameFromNodesAnalyzer.analyze(context)
    newContext.routeNameAnalysis.name should equal(None)
  }

  test("do not derive route name from node names if end node name unknown") {
    val context = BaseRouteAnalysisContext(
      null,
      None,
      _routeNameAnalysis = Some(
        RouteNameAnalysis()
      ),
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          startNode = Some(newRouteNodeAnalysis(1001, "01")),
          endNode = None,
        )
      )
    )
    val newContext = BaseRouteNameFromNodesAnalyzer.analyze(context)
    newContext.routeNameAnalysis.name should equal(None)
  }
}
