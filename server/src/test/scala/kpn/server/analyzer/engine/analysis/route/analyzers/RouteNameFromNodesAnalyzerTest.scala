package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

class RouteNameFromNodesAnalyzerTest extends UnitTest {

  test("derive route name from node names") {
    val context = RouteAnalysisContext(
      null,
      null,
      routeNameAnalysis = Some(RouteNameAnalysis()),
      routeNodeAnalysis = Some(
        RouteNodeAnalysis(
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
    newContext.routeNameAnalysis.get.name should equal(Some("01-02"))
  }

  test("derive route name from non-digit node names (use separator with spaces)") {
    val context = RouteAnalysisContext(
      null,
      null,
      routeNameAnalysis = Some(RouteNameAnalysis()),
      routeNodeAnalysis = Some(
        RouteNodeAnalysis(
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
    newContext.routeNameAnalysis.get.name should equal(Some("a - b"))
  }

  test("do not derive route name from node names if route name already known") {
    val context = RouteAnalysisContext(
      null,
      null,
      routeNameAnalysis = Some(
        RouteNameAnalysis(name = Some("route-name"))
      ),
      routeNodeAnalysis = Some(
        RouteNodeAnalysis(
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
    newContext.routeNameAnalysis.get.name should equal(Some("route-name"))
  }

  test("do not derive route name from node names if start node name unknown") {
    val context = RouteAnalysisContext(
      null,
      null,
      routeNameAnalysis = Some(
        RouteNameAnalysis()
      ),
      routeNodeAnalysis = Some(
        RouteNodeAnalysis(
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
    newContext.routeNameAnalysis.get.name should equal(None)
  }

  test("do not derive route name from node names if end node name unknown") {
    val context = RouteAnalysisContext(
      null,
      null,
      routeNameAnalysis = Some(
        RouteNameAnalysis()
      ),
      routeNodeAnalysis = Some(
        RouteNodeAnalysis(
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
    newContext.routeNameAnalysis.get.name should equal(None)
  }

  test("RouteNameAnalysis is a prerequisite") {
    val context = RouteAnalysisContext(null, null)
    val message = intercept[IllegalStateException] {
      RouteNameFromNodesAnalyzer.analyze(context)
    }.getMessage
    message should equal("RouteNameAnalysis required before route name from nodes analysis")
  }

  test("RouteNodeAnalysis is a prerequisite") {
    val context = RouteAnalysisContext(null, null, routeNameAnalysis = Some(RouteNameAnalysis()))
    val message = intercept[IllegalStateException] {
      RouteNameFromNodesAnalyzer.analyze(context)
    }.getMessage
    message should equal("RouteNodeAnalysis required before route name from nodes analysis")
  }
}
