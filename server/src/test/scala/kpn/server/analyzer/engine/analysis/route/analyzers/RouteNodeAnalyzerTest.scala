package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysisFormatter
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RouteNodeAnalyzerTest extends AnyFunSuite with Matchers {

  // TODO ROUTE add tests for facts

  test("nodes in single way in correct order, also included in relation") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      memberNode(1)
      memberWay(5, "", 1, 2)
      memberNode(2)
    }

    analyze(d) should equal(
      "Start=(1/01/01/RW)," +
        "End=(2/02/02/RW)"
    )
  }

  test("route with extra start and end nodes") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "01")
      node(3, "01") // <-- the primary start node
      node(4, "02") // <-- the primary end node
      node(5, "02")
      node(6, "02")
      memberWay(11, "", 1)
      memberWay(12, "", 2)
      memberWay(13, "", 3)
      memberWay(14, "", 4)
      memberWay(15, "", 5)
      memberWay(16, "", 6)
    }

    analyze(d) should equal(
      "Start=(3/01/01.a/W,2/01/01.b/W,1/01/01.c/W)," +
        "End=(4/02/02.a/W,5/02/02.b/W,6/02/02.c/W)"
    )
  }

  test("route with extra start and end nodes in reverse order") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "01")
      node(3, "01")
      node(4, "02")
      node(5, "02")
      node(6, "02")
      memberWay(11, "", 6)
      memberWay(12, "", 5)
      memberWay(13, "", 4)
      memberWay(14, "", 3)
      memberWay(15, "", 2)
      memberWay(16, "", 1)
    }

    analyze(d) should equal(
      "Start=(3/01/01.a/W,2/01/01.b/W,1/01/01.c/W)," +
        "End=(4/02/02.a/W,5/02/02.b/W,6/02/02.c/W)," +
        "(reversed)"
    )
  }

  test("route with extra end nodes not in reverse order") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      node(3, "02")
      memberWay(11, "", 1, 2)
      memberWay(12, "", 2, 3)
    }

    analyze(d) should equal(
      "Start=(1/01/01/W)," +
        "End=(2/02/02.a/W,3/02/02.b/W)"
    )
  }

  test("route with extra end nodes not in reverse order 2") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      node(3, "02")
      memberWay(11, "", 2, 1)
      memberWay(12, "", 2, 3)
    }

    analyze(d) should equal(
      "Start=(1/01/01/W)," +
        "End=(2/02/02.a/W,3/02/02.b/W)"
    )
  }

  test("nodes defined only in route relation") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")

      memberNode(1)
      memberNode(2)
    }

    analyze(d) should equal(
      "Start=(1/01/01/R)," +
        "End=(2/02/02/R)"
    )
  }

  test("numeric compare") {

    val d = new RouteTestData("20-100") {
      node(1, "100")
      node(2, "20")
      memberWay(11, "", 1)
      memberWay(12, "", 2)
    }

    analyze(d) should equal(
      "Start=(2/20/20/W)," +
        "End=(1/100/100/W)," +
        "(reversed)"
    )
  }

  test("redundant node in relation") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      node(3, "03")
      memberNode(1)
      memberNode(2)
      memberNode(3)
      memberWay(5, "", 1, 2)
    }

    analyze(d) should equal(
      "Start=(1/01/01/RW)," +
        "End=(2/02/02/RW)," +
        "Redundant=(3/03/03/R)"
    )
  }

  test("redundant node in way (numeric value between start and end node in route name)") {

    val d = new RouteTestData("01-03") {
      node(1, "01")
      node(2, "02")
      node(3, "03")
      memberWay(11, "", 1, 2)
      memberWay(12, "", 2, 3)
    }

    analyze(d) should equal(
      "Start=(1/01/01/W)," +
        "End=(3/03/03/W)," +
        "Redundant=(2/02/02/W)"
    )
  }

  test("redundant node in way (numeric value higher than highest in route name)") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      node(3, "03")
      memberWay(11, "", 1, 2)
      memberWay(12, "", 2, 3)
    }

    analyze(d) should equal(
      "Start=(1/01/01/W)," +
        "End=(2/02/02/W)," +
        "Redundant=(3/03/03/W)"
    )
  }

  test("start and end node with the same name") {

    val d = new RouteTestData("01-01") {
      node(1, "01")
      node(3, "01")
      node(5, "01")
      node(8, "01")
      node(9, "02")
      memberWay(11, "", 1, 2) // 1: secundary start node
      memberWay(12, "", 2, 3)
      memberWay(13, "", 3, 4) // 3: secundary start node
      memberWay(14, "", 4, 5)
      memberWay(15, "", 5, 6) // 5: start
      memberWay(16, "", 6, 7)
      memberWay(17, "", 7, 8) // 8: end
      memberWay(18, "", 8, 9) // 9: redundant
    }

    analyze(d) should equal(
      "Start=(5/01/01.a/W,3/01/01.b/W,1/01/01.c/W)," +
        "End=(8/01/01/W)," +
        "Redundant=(9/02/02/W)"
    )
  }

  test("start and end node with the same name, but only one node in ways") {

    val d = new RouteTestData("01-01") {
      node(1, "01")
      node(4, "02")
      memberWay(11, "", 1, 2)
      memberWay(12, "", 2, 3)
      memberWay(13, "", 3, 4)
    }

    analyze(d) should equal(
      "Start=(1/01/01/W)," +
        "Redundant=(4/02/02/W)"
    )
  }

  test("node name with leading zero in route name and without leading zero in way") {

    val d = new RouteTestData("01-02") {
      node(1, "1")
      node(3, "02")
      node(5, "03")
      memberWay(11, "", 1, 2)
      memberWay(12, "", 2, 3)
      memberWay(13, "", 3, 4)
      memberWay(14, "", 4, 5)
    }

    analyze(d) should equal(
      "Start=(1/1/1/W)," +
        "End=(3/02/02/W)," +
        "Redundant=(5/03/03/W)"
    )
  }

  test("start node in route relation, but not in ways") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(3, "02")
      memberNode(1)
      memberNode(3)
      memberWay(11, "", 2, 3)
    }

    analyze(d) should equal(
      "Start=(1/01/01/R)," +
        "End=(3/02/02/RW)"
    )
  }

  private def analyze(d: RouteTestData): String = {

    val data = d.data

    val loadedRoute = LoadedRoute(
      country = None,
      networkType = NetworkType.hiking,
      "",
      data,
      data.relations(1L)
    )

    val analysisContext = new AnalysisContext()

    val context = RouteAnalysisContext(
      analysisContext,
      networkNodes = Map.empty,
      loadedRoute,
      orphan = false
    )

    val newContext = RouteNodeAnalyzer.analyze(RouteNameAnalyzer.analyze(context))
    new RouteNodeAnalysisFormatter(newContext.routeNodeAnalysis.get).string
  }
}
