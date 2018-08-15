package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.Interpreter
import kpn.core.engine.analysis.RouteTestData
import kpn.core.engine.analysis.route.RouteNameAnalysis
import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.core.load.data.LoadedRoute
import kpn.shared.NetworkType
import org.scalatest.FunSuite
import org.scalatest.Matchers

class RouteNameAnalyzerTest extends FunSuite with Matchers {

  test("regular route name") {

    val analysis = analyze("01-02")

    analysis.name should equal(Some("01-02"))
    analysis.startNodeName should equal(Some("01"))
    analysis.endNodeName should equal(Some("02"))
    analysis.reversed should equal(false)
  }

  test("nodes in reversed order in route name") {

    val analysis = analyze("02-01")

    analysis.name should equal(Some("02-01"))
    analysis.startNodeName should equal(Some("01"))
    analysis.endNodeName should equal(Some("02"))
    analysis.reversed should equal(true)
  }

  test("node ordering is number based (not string based)") {

    val analysis = analyze("100-20")

    analysis.name should equal(Some("100-20"))
    analysis.startNodeName should equal(Some("20"))
    analysis.endNodeName should equal(Some("100"))
    analysis.reversed should equal(true)
  }

  test("non numeric node names in route name") {

    val analysis = analyze("1A-2A")

    analysis.name should equal(Some("1A-2A"))
    analysis.startNodeName should equal(Some("1A"))
    analysis.endNodeName should equal(Some("2A"))
    analysis.reversed should equal(false)
  }

  test("non numeric node names in route name in reversed order are not reversed") {

    val analysis = analyze("2A-1A")

    analysis.name should equal(Some("2A-1A"))
    analysis.startNodeName should equal(Some("2A"))
    analysis.endNodeName should equal(Some("1A"))
    analysis.reversed should equal(false)
  }

  test("route name without node separator") {

    val analysis = analyze("UNEXPECTED")

    analysis.name should equal(Some("UNEXPECTED"))
    analysis.startNodeName should equal(None)
    analysis.endNodeName should equal(None)
    analysis.reversed should equal(false)
  }

  test("route name without start node") {

    val analysis = analyze("-01")

    analysis.name should equal(Some("-01"))
    analysis.startNodeName should equal(None)
    analysis.endNodeName should equal(Some("01"))
    analysis.reversed should equal(false)
  }

  test("route name without end node") {

    val analysis = analyze("01-")

    analysis.name should equal(Some("01-"))
    analysis.startNodeName should equal(Some("01"))
    analysis.endNodeName should equal(None)
    analysis.reversed should equal(false)
  }

  test("route name canoe") {

    val analysis = analyze("01-02; canoe")

    analysis.name should equal(Some("01-02"))
    analysis.startNodeName should equal(Some("01"))
    analysis.endNodeName should equal(Some("02"))
    analysis.reversed should equal(false)
  }

  test("route name canoe without space") {

    val analysis = analyze("01-02;canoe")

    analysis.name should equal(Some("01-02"))
    analysis.startNodeName should equal(Some("01"))
    analysis.endNodeName should equal(Some("02"))
    analysis.reversed should equal(false)
  }

  test("route name motorboat") {

    val analysis = analyze("01-02; motorboat")

    analysis.name should equal(Some("01-02"))
    analysis.startNodeName should equal(Some("01"))
    analysis.endNodeName should equal(Some("02"))
    analysis.reversed should equal(false)
  }

  test("route name motorboat without space") {

    val analysis = analyze("01-02;motorboat")

    analysis.name should equal(Some("01-02"))
    analysis.startNodeName should equal(Some("01"))
    analysis.endNodeName should equal(Some("02"))
    analysis.reversed should equal(false)
  }

  test("route name (incompleet)") {

    val analysis = analyze("01-02; (incompleet)")

    analysis.name should equal(Some("01-02"))
    analysis.startNodeName should equal(Some("01"))
    analysis.endNodeName should equal(Some("02"))
    analysis.reversed should equal(false)
  }

  test("route name (incompleet) without space") {

    val analysis = analyze("01-02;(incompleet)")

    analysis.name should equal(Some("01-02"))
    analysis.startNodeName should equal(Some("01"))
    analysis.endNodeName should equal(Some("02"))
    analysis.reversed should equal(false)
  }

  private def analyze(name: String): RouteNameAnalysis = {

    val data = new RouteTestData(name).data

    val loadedRoute = LoadedRoute(
      country = None,
      networkType = NetworkType.hiking,
      name,
      data,
      data.relations(1L)
    )

    val context = RouteAnalysisContext(
      networkNodes = Map(),
      loadedRoute,
      orphan = false,
      interpreter = new Interpreter(loadedRoute.networkType)
    )

    val newContext = RouteNameAnalyzer.analyze(context)

    newContext.routeNameAnalysis.get

  }
}
