package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysisFormatter
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute

class RouteNodeAnalyzerTest extends UnitTest {

  test("no nodes") {

    val d = new RouteTestData("01-02") {
      node(1)
      node(2)
      memberWay(3, "", 1, 2)
    }

    analyze(d) should equal(
      ";RouteWithoutNodes"
    )
  }

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

  test("nodes defined only in route relation") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")

      memberNode(1)
      memberNode(2)
    }

    analyze(d) should equal(
      "Start=(1/01/01/R)," +
        "End=(2/02/02/R);" +
        "RouteNodeMissingInWays"
    )
  }

  test("numeric compare") {

    val d = new RouteTestData("unknown") {
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
        "Redundant=(3/03/03/R);" +
        "RouteRedundantNodes"
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
        "Redundant=(2/02/02/W);" +
        "RouteRedundantNodes"
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
        "Redundant=(3/03/03/W);" +
        "RouteRedundantNodes"
    )
  }

  test("start and end node with the same name") {

    val d = new RouteTestData("01-01") {
      node(1, "01")
      node(3, "01")
      node(5, "01")
      node(8, "01")
      node(9, "02")
      memberWay(11, "", 1, 2) // 1: secondary start node --> 01.c
      memberWay(12, "", 2, 3)
      memberWay(13, "", 3, 4) // 3: secondary start node --> 01.b
      memberWay(14, "", 4, 5)
      memberWay(15, "", 5, 6) // 5: start --> 01.a
      memberWay(16, "", 6, 7)
      memberWay(17, "", 7, 8) // 8: end --> 01
      memberWay(18, "", 8, 9) // 9: redundant --> 02
    }

    analyze(d) should equal(
      "Free=(1/01/01.a/W,3/01/01.b/W,5/01/01.c/W,8/01/01.d/W)," +
        "Redundant=(9/02/02/W);" +
        "RouteRedundantNodes"
    )
  }

  test("start and end node with the same name, but only one node in ways") {

    val d = new RouteTestData("01-01") {
      node(1, "01")
      node(4, "02")
      node(5, "01")
      memberWay(11, "", 1, 2)
      memberWay(12, "", 2, 3)
      memberWay(13, "", 3, 4)
      memberNode(5)
    }

    analyze(d) should equal(
      "Free=(1/01/01.a/W,5/01/01.b/R)," +
        "Redundant=(4/02/02/W);" +
        "RouteNodeMissingInWays,RouteRedundantNodes"
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
      "Start=(1/01/01/W)," +
        "End=(3/02/02/W)," +
        "Redundant=(5/03/03/W);" +
        "RouteRedundantNodes"
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
        "End=(3/02/02/RW);" +
        "RouteNodeMissingInWays"
    )
  }

  test("no start node in route name") {

    val d = new RouteTestData("-02") {
      node(1, "01")
      node(2, "02")
      memberNode(1)
      memberNode(2)
      memberWay(11, "", 1, 2)
    }

    analyze(d) should equal(
      "End=(2/02/02/RW)," +
        "Redundant=(1/01/01/RW);" +
        "RouteRedundantNodes"
    )
  }

  test("no end node in route name") {

    val d = new RouteTestData("01-") {
      node(1, "01")
      node(2, "02")
      memberNode(1)
      memberNode(2)
      memberWay(11, "", 1, 2)
    }

    analyze(d) should equal(
      "Start=(1/01/01/RW)," +
        "Redundant=(2/02/02/RW);" +
        "RouteRedundantNodes"
    )
  }

  test("circular route with one node only") {

    val d = new RouteTestData("01-01") {
      node(1, "01")
      node(2)
      node(3)
      node(4)
      memberWay(11, "", 1, 2, 3, 4, 1)
    }

    analyze(d) should equal("Free=(1/01/01/W)")
  }

  test("no route name") {

    val d = new RouteTestData("") {
      node(1, "01")
      node(2, "02")
      memberNode(1)
      memberNode(2)
      memberWay(11, "", 1, 2)
    }

    analyze(d) should equal(
      "Start=(1/01/01/RW)," +
        "End=(2/02/02/RW);" +
        "RouteNameMissing"
    )
  }

  test("no route name - multiple nodes with same name") {

    val d = new RouteTestData("") {
      node(1, "01")
      node(2, "01")
      node(3, "01")
      memberWay(11, "", 1, 2)
      memberWay(12, "", 2, 3)
    }

    analyze(d) should equal(
      "Start=(1/01/01.a/W,2/01/01.b/W,3/01/01.c/W);" +
        "RouteNameMissing"
    )
  }

  test("no route name - numeric node names in reverse order") {

    val d = new RouteTestData("") {
      node(1, "02")
      node(2)
      node(3, "01")
      memberWay(11, "", 1, 2)
      memberWay(12, "", 2, 3)
    }

    analyze(d) should equal(
      "Start=(3/01/01/W)," +
        "End=(1/02/02/W)," +
        "(reversed);" +
        "RouteNameMissing"
    )
  }

  test("no route name - pick start and and end node from numeric node names") {

    val d = new RouteTestData("") {
      node(1, "02")
      node(2)
      node(3, "03")
      node(4)
      node(5, "01")

      memberWay(11, "", 1, 2)
      memberWay(12, "", 2, 3)
      memberWay(13, "", 3, 4)
      memberWay(14, "", 4, 5)
    }

    analyze(d) should equal(
      "Start=(5/01/01/W)," +
        "End=(3/03/03/W)," +
        "Redundant=(1/02/02/W)," +
        "(reversed);" +
        "RouteNameMissing,RouteRedundantNodes"
    )
  }

  test("extra 'proposed' nodes in regular route") {
    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      rawNode(
        newRawNode(
          3,
          tags = Tags.from(
            "network:type" -> "node_network",
            "rwn_ref" -> "03",
            "state" -> "proposed"
          )
        )
      )
      rawNode(
        newRawNode(
          4,
          tags = Tags.from(
            "network:type" -> "node_network",
            "proposed:rwn_ref" -> "04",
          )
        )
      )
      memberWay(11, "", 1, 3, 4, 2)
    }

    analyze(d) should equal(
      "Start=(1/01/01/W),End=(2/02/02/W)"
    )
  }

  test("extra regular nodes in proposed route") {
    val d = new RouteTestData("01-02", routeTags = Tags.from("state" -> "proposed")) {
      rawNode(
        newRawNode(
          1,
          tags = Tags.from(
            "network:type" -> "node_network",
            "rwn_ref" -> "01",
            "state" -> "proposed"
          )
        )
      )
      rawNode(
        newRawNode(
          2,
          tags = Tags.from(
            "network:type" -> "node_network",
            "proposed:rwn_ref" -> "02",
          )
        )
      )
      node(3, "03")
      node(4, "04")
      memberWay(11, "", 1, 3, 4, 2)
    }

    analyze(d, proposed = true) should equal(
      "Start=(1/01/01/W),End=(2/02/02/W)"
    )
  }

  private def analyze(d: RouteTestData, proposed: Boolean = false): String = {

    val data = d.data

    val loadedRoute = LoadedRoute(
      country = None,
      scopedNetworkType = ScopedNetworkType.rwn,
      data,
      data.relations(1L)
    )

    val analysisContext = new AnalysisContext()
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val routeNodeInfoAnalyzer = new RouteNodeInfoAnalyzerImpl(analysisContext, oldNodeAnalyzer)
    val routeNodeInfos = routeNodeInfoAnalyzer.analyze(loadedRoute)

    val context = RouteAnalysisContext(
      analysisContext,
      loadedRoute,
      orphan = false,
      routeNodeInfos,
      proposed = proposed,
    )

    val newContext = RouteNodeAnalyzer.analyze(RouteNameAnalyzer.analyze(context))
    val nodeString = new RouteNodeAnalysisFormatter(newContext.routeNodeAnalysis.get).string
    val factsString = newContext.facts.map(_.name).mkString(",")
    if (factsString.nonEmpty) {
      nodeString + ";" + factsString
    }
    else {
      nodeString
    }
  }
}
