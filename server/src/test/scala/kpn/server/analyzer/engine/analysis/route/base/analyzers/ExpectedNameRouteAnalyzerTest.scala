package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact
import kpn.api.common.SharedTestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodesAnalysis

class ExpectedNameRouteAnalyzerTest extends UnitTest with SharedTestObjects {

  test("no check for non nodenetwork route") {
    val newContext = doTest(Some("bla"), None, None, nodeNetwork = false)
    newContext.expectedName should equal(None)
    assertEqual(newContext.facts, Seq.empty)
  }

  test("happy path") {
    val newContext = doTest(Some("01-02"), Some("01"), Some("02"))
    newContext.expectedName should equal(Some("01-02"))
    assertEqual(newContext.facts, Seq.empty)
  }

  test("route name reversed") {
    val newContext = doTest(Some("02-01"), Some("01"), Some("02"))
    newContext.expectedName should equal(Some("01-02"))
    assertEqual(newContext.facts, Seq.empty)
  }

  test("unexpected route name - start node does not match") {
    val newContext = doTest(Some("04-05"), Some("04"), Some("06"))
    newContext.expectedName should equal(Some("04-06"))
    assertEqual(newContext.facts.toSet, Set(Fact.RouteNodeNameMismatch))
  }

  test("unexpected route name - end node does not match") {
    val newContext = doTest(Some("04-05"), Some("04"), Some("07"))
    newContext.expectedName should equal(Some("04-07"))
    assertEqual(newContext.facts.toSet, Set(Fact.RouteNodeNameMismatch))
  }

  test("no fact when route name unknown") {
    val newContext = doTest(None, Some("01"), Some("02"))
    newContext.expectedName should equal(Some(""))
    assertEqual(newContext.facts, Seq.empty)
  }

  test("no fact when start node unknown") {
    val newContext = doTest(Some("01-02"), None, Some("02"))
    newContext.expectedName should equal(Some(""))
    assertEqual(newContext.facts, Seq.empty)
  }

  test("no fact when end node unknown") {
    val newContext = doTest(Some("01-02"), Some("01"), None)
    newContext.expectedName should equal(Some(""))
    assertEqual(newContext.facts, Seq.empty)
  }

  test("preserve white space arround separator dash") {
    val newContext = doTest(Some("aaa - bbb-ccc"), Some("aaa"), Some("bbb-ccc"))
    newContext.expectedName should equal(Some("aaa - bbb-ccc"))
    assertEqual(newContext.facts, Seq.empty)
  }

  test("do not make check when no separator dash") {
    val newContext = doTest(Some("bla"), Some("01"), Some("02"))
    newContext.expectedName should equal(Some("01-02"))
    assertEqual(newContext.facts.toSet, Set(Fact.RouteNodeNameMismatch))
  }

  private def doTest(routeName: Option[String], startNodeName: Option[String], endNodeName: Option[String], nodeNetwork: Boolean = true): BaseRouteAnalysisContext = {
    val routeNameAnalysis = RouteNameAnalysis(
      name = routeName,
      startNodeName = startNodeName,
      endNodeName = endNodeName
    )

    val routeNodesAnalysis = RouteNodesAnalysis(
      startNode = startNodeName.map(name => RouteNodeAnalysis(newNode(), name = name, alternateName = name, isInWay = true)),
      endNode = endNodeName.map(name => RouteNodeAnalysis(newNode(), name = name, alternateName = name, isInWay = true)),
    )

    val context = buildContext(nodeNetwork).
      copy(_routeNameAnalysis = Some(routeNameAnalysis)).
      copy(_routeNodesAnalysis = Some(routeNodesAnalysis))

    BaseRouteExpectedNameAnalyzer.analyze(context)
  }

  private def buildContext(nodeNetwork: Boolean): BaseRouteAnalysisContext = {
    val data = new RouteTestData("01-02") {
      node(1001)
      node(1002)
      node(1003)
      node(1004)
      node(1005)
      node(1006)
      memberWay(101, "", 1, 2, 3)
      memberWay(102, "", 3, 4, 5)
      memberWay(103, "", 5, 6)
    }.data

    val relation = data.relations(1L)
    BaseRouteAnalysisContext(relation, None, nodeNetwork = nodeNetwork)
  }
}
