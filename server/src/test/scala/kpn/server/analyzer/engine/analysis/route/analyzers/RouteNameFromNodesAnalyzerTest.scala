package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.OldRouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class RouteNameFromNodesAnalyzerTest extends UnitTest {

  test("derive route name from node names") {
    val context = RouteDetailAnalysisContext(
      null,
      None,
      nodeNetwork = true,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _oldRouteNodeAnalysis = Some(
        OldRouteNodeAnalysis(
          startNodes = Seq(
            RouteNode(name = "01")
          ),
          endNodes = Seq(
            RouteNode(name = "02")
          )
        )
      )
    )
    val newContext = RouteNameFromNodesAnalyzer.analyze(context)
    newContext.routeNameAnalysis.name should equal(Some("01-02"))
  }

  test("derive route name from non-digit node names (use separator with spaces)") {
    val context = RouteDetailAnalysisContext(
      null,
      None,
      nodeNetwork = true,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _oldRouteNodeAnalysis = Some(
        OldRouteNodeAnalysis(
          startNodes = Seq(
            RouteNode(name = "a")
          ),
          endNodes = Seq(
            RouteNode(name = "b")
          )
        )
      )
    )
    val newContext = RouteNameFromNodesAnalyzer.analyze(context)
    newContext.routeNameAnalysis.name should equal(Some("a - b"))
  }

  test("do not derive route name from node names if route name already known") {
    val context = RouteDetailAnalysisContext(
      null,
      None,
      _routeNameAnalysis = Some(
        RouteNameAnalysis(name = Some("route-name"))
      ),
      _oldRouteNodeAnalysis = Some(
        OldRouteNodeAnalysis(
          startNodes = Seq(
            RouteNode(name = "01")
          ),
          endNodes = Seq(
            RouteNode(name = "02")
          )
        )
      )
    )
    val newContext = RouteNameFromNodesAnalyzer.analyze(context)
    newContext.routeNameAnalysis.name should equal(Some("route-name"))
  }

  test("do not derive route name from node names if start node name unknown") {
    val context = RouteDetailAnalysisContext(
      null,
      None,
      _routeNameAnalysis = Some(
        RouteNameAnalysis()
      ),
      _oldRouteNodeAnalysis = Some(
        OldRouteNodeAnalysis(
          startNodes = Seq(
            RouteNode()
          ),
          endNodes = Seq(
            RouteNode(name = "02")
          )
        )
      )
    )
    val newContext = RouteNameFromNodesAnalyzer.analyze(context)
    newContext.routeNameAnalysis.name should equal(None)
  }

  test("do not derive route name from node names if end node name unknown") {
    val context = RouteDetailAnalysisContext(
      null,
      None,
      _routeNameAnalysis = Some(
        RouteNameAnalysis()
      ),
      _oldRouteNodeAnalysis = Some(
        OldRouteNodeAnalysis(
          startNodes = Seq(
            RouteNode(name = "01")
          ),
          endNodes = Seq(
            RouteNode()
          )
        )
      )
    )
    val newContext = RouteNameFromNodesAnalyzer.analyze(context)
    newContext.routeNameAnalysis.name should equal(None)
  }
}
