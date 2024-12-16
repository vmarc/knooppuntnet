package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class RouteNameFromNodesAnalyzerTest extends UnitTest {

  test("derive route name from node names") {
    pending // TODO redesign
    val context = RouteDetailAnalysisContext(
      null,
      None,
      // TODO redesign
      //      nodeNetwork = true,
      //      _routeNameAnalysis = Some(RouteNameAnalysis()),
      //      _oldRouteNodeAnalysis = Some(
      //        OldRouteNodeAnalysis(
      //          startNodes = Seq(
      //            OldRouteNode(name = "01")
      //          ),
      //          endNodes = Seq(
      //            OldRouteNode(name = "02")
      //          )
      //        )
      //      )
    )
    val newContext = RouteNameFromNodesAnalyzer.analyze(context)
    newContext.routeNameAnalysis.name should equal(Some("01-02"))
  }

  test("derive route name from non-digit node names (use separator with spaces)") {
    pending // TODO redesign
    val context = RouteDetailAnalysisContext(
      null,
      None,
      nodeNetwork = true,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      // TODO redesign
      //      _oldRouteNodeAnalysis = Some(
      //        OldRouteNodeAnalysis(
      //          startNodes = Seq(
      //            OldRouteNode(name = "a")
      //          ),
      //          endNodes = Seq(
      //            OldRouteNode(name = "b")
      //          )
      //        )
      //      )
    )
    val newContext = RouteNameFromNodesAnalyzer.analyze(context)
    newContext.routeNameAnalysis.name should equal(Some("a - b"))
  }

  test("do not derive route name from node names if route name already known") {
    pending // TODO redesign
    val context = RouteDetailAnalysisContext(
      null,
      None,
      _routeNameAnalysis = Some(
        RouteNameAnalysis(name = Some("route-name"))
      ),

      //      _oldRouteNodeAnalysis = Some(
      //        OldRouteNodeAnalysis(
      //          startNodes = Seq(
      //            OldRouteNode(name = "01")
      //          ),
      //          endNodes = Seq(
      //            OldRouteNode(name = "02")
      //          )
      //        )
      //      )
    )
    val newContext = RouteNameFromNodesAnalyzer.analyze(context)
    newContext.routeNameAnalysis.name should equal(Some("route-name"))
  }

  test("do not derive route name from node names if start node name unknown") {
    pending // TODO redesign
    val context = RouteDetailAnalysisContext(
      null,
      None,
      _routeNameAnalysis = Some(
        RouteNameAnalysis()
      ),
      //      _oldRouteNodeAnalysis = Some(
      //        OldRouteNodeAnalysis(
      //          startNodes = Seq(
      //            OldRouteNode()
      //          ),
      //          endNodes = Seq(
      //            OldRouteNode(name = "02")
      //          )
      //        )
      //      )
    )
    val newContext = RouteNameFromNodesAnalyzer.analyze(context)
    newContext.routeNameAnalysis.name should equal(None)
  }

  test("do not derive route name from node names if end node name unknown") {
    pending // TODO redesign
    val context = RouteDetailAnalysisContext(
      null,
      None,
      _routeNameAnalysis = Some(
        RouteNameAnalysis()
      ),
      //      _oldRouteNodeAnalysis = Some(
      //        OldRouteNodeAnalysis(
      //          startNodes = Seq(
      //            OldRouteNode(name = "01")
      //          ),
      //          endNodes = Seq(
      //            OldRouteNode()
      //          )
      //        )
      //      )
    )
    val newContext = RouteNameFromNodesAnalyzer.analyze(context)
    newContext.routeNameAnalysis.name should equal(None)
  }
}
