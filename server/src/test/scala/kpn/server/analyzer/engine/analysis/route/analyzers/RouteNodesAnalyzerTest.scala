package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteNodesAnalysisFormatter
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinkAnalyzer

class RouteNodesAnalyzerTest extends UnitTest {

  test("no nodes") {

    val d = new RouteTestData("01-02") {
      node(1)
      node(2)
      memberWay(3, "", 1, 2)
    }

    analyze(d) should equal(
      Seq(
        "RouteWithoutNodes"
      )
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

    analyze(d).foreach(println)

    analyze(d).shouldMatchTo(
      Seq(
        "start=1(01)W",
        "end=2(02)W"
      )
    )
  }

  test("nodes defined only in route relation") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      memberNode(1)
      memberNode(2)
    }

    analyze(d).shouldMatchTo(
      Seq(
        "start=1(01)R",
        "end=2(02)R",
        "RouteNodeMissingInWays"
      )
    )
  }

  test("numeric compare") {
    pending // TODO redesign - this logic is not needed anymore in the new design?

    val d = new RouteTestData("unknown") {
      node(1, "100")
      node(2, "20")
      memberWay(11, "", 1)
      memberWay(12, "", 2)
    }

    analyze(d).shouldMatchTo(
      Seq(
        "start=2(20)W",
        "end=1(100)W)",
        "(reversed)"
      )
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

    analyze(d).shouldMatchTo(
      Seq(
        "start=3(01.a)W",
        "end=4(02.a)W",
        "start-tentacle=2(01.b)W",
        "start-tentacle=1(01.c)W",
        "end-tentacle=5(02.b)W",
        "end-tentacle=6(02.c)W",
      )
    )
  }

  test("route with extra end node") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(2, "02")
      node(3, "02")
      memberWay(11, "", 1, 2)
      memberWay(12, "", 2, 3)
    }

    analyze(d).shouldMatchTo(
      Seq(
        "start=1(01)W",
        "end=2(02.a)W",
        "end-tentacle=3(02.b)W",
      )
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

    analyze(d).shouldMatchTo(
      Seq(
        "start=1(01)W",
        "end=2(02)W",
        "redundant=3(03)R",
        "RouteRedundantNodes"
      )
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

    analyze(d).shouldMatchTo(
      Seq(
        "start=1(01)W",
        "end=3(03)W",
        "redundant=2(02)W",
        "RouteRedundantNodes",
      )
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

    analyze(d).shouldMatchTo(
      Seq(
        "start=1(01)W",
        "end=3(03)W",
        "redundant=2(02)W",
        "RouteRedundantNodes",
      )
    )
  }

  test("start and end node with the same name") {

    val d = new RouteTestData("01-01") {
      node(1, "01")
      node(3, "01")
      node(5, "01")
      node(8, "01")
      node(9, "02")
      memberWay(11, "", 1, 2) // 1: secondary start node --> 01.d
      memberWay(12, "", 2, 3)
      memberWay(13, "", 3, 4) // 3: secondary start node --> 01.c
      memberWay(14, "", 4, 5)
      memberWay(15, "", 5, 6) // 5: start --> 01.b
      memberWay(16, "", 6, 7)
      memberWay(17, "", 7, 8) // 8: start --> 01.a
      memberWay(18, "", 8, 9) // 9: end --> 02
    }
    analyze(d).foreach(a => println(s""""$a","""))

    analyze(d).shouldMatchTo(
      Seq(
        "start=8(01.a)W",
        "end=9(02)W",
        "start-tentacle=5(01.b)W",
        "start-tentacle=3(01.c)W",
        "start-tentacle=1(01.d)W",
      )
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

    analyze(d).foreach(a => println(s""""$a","""))

    analyze(d).shouldMatchTo(
      Seq(
        "Free=(1/01/01.a/W,5/01/01.b/R)",
        "Redundant=(4/02/02/W)",
        "RouteNodeMissingInWays",
        "RouteRedundantNodes"
      )
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

    analyze(d).foreach(a => println(s""""$a","""))

    analyze(d).shouldMatchTo(
      Seq(
        "start=(1/01/01/W)",
        "end=(3/02/02/W)",
        "Redundant=(5/03/03/W)",
        "RouteRedundantNodes"
      )
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

    analyze(d).foreach(a => println(s""""$a","""))

    analyze(d).shouldMatchTo(
      Seq(
        "start=(1/01/01/R)",
        "end=(3/02/02/RW)",
        "RouteNodeMissingInWays"
      )
    )
  }

  test("no start node in route name") { // TODO redesign - this test does not belong here (route name irrelevant)?

    val d = new RouteTestData("-02") {
      node(1, "01")
      node(2, "02")
      memberNode(1)
      memberNode(2)
      memberWay(11, "", 1, 2)
    }

    analyze(d).foreach(a => println(s""""$a","""))

    analyze(d).shouldMatchTo(
      Seq(
        "start=1(01)W",
        "end=2(02)W",
      )
    )
  }

  test("no end node in route name") { // TODO redesign - this test does not belong here (route name irrelevant)?

    val d = new RouteTestData("01-") {
      node(1, "01")
      node(2, "02")
      memberNode(1)
      memberNode(2)
      memberWay(11, "", 1, 2)
    }

    analyze(d).foreach(a => println(s""""$a","""))

    analyze(d).shouldMatchTo(
      Seq(
        "start=1(01)W",
        "end=2(02)W",
      )
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

    analyze(d).foreach(a => println(s""""$a","""))

    analyze(d).shouldMatchTo(
      Seq(
        "start=1(01)W",
      )
    )
  }

  test("no route name") { // TODO redesign - this test does not belong here (route name irrelevant)?

    val d = new RouteTestData("") {
      node(1, "01")
      node(2, "02")
      memberNode(1)
      memberNode(2)
      memberWay(11, "", 1, 2)
    }

    analyze(d).shouldMatchTo(
      Seq(
        "start=1(01)W",
        "end=2(02)W"
      )
    )
  }

  test("no route name - multiple nodes with same name") { // TODO redesign - this test does not belong here (route name irrelevant)?

    val d = new RouteTestData("") {
      node(1, "01")
      node(2, "01")
      node(3, "01")
      memberWay(11, "", 1, 2)
      memberWay(12, "", 2, 3)
    }

    analyze(d).foreach(a => println(s""""$a","""))

    analyze(d).shouldMatchTo(
      Seq(
        "start=3(01.a)W",
        "start-tentacle=2(01.b)W",
        "start-tentacle=1(01.c)W",
      )
    )
  }

  test("no route name - numeric node names in reverse order") { // TODO redesign - this test does not belong here (route name irrelevant)?

    val d = new RouteTestData("") {
      node(1, "02")
      node(2)
      node(3, "01")
      memberWay(11, "", 1, 2)
      memberWay(12, "", 2, 3)
    }

    analyze(d).foreach(a => println(s""""$a","""))

    analyze(d).shouldMatchTo(
      Seq(
        "start=1(02)W",
        "end=3(01)W",
      )
    )
  }

  test("no route name - pick start and and end node from numeric node names") { // TODO redesign - this test does not belong here (route name irrelevant)?

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

    analyze(d).foreach(a => println(s""""$a","""))

    analyze(d).shouldMatchTo(
      Seq(
        "start=1(02)W",
        "end=5(01)W",
        "redundant=3(03)W",
        "RouteRedundantNodes",
      )
    )
  }

  test("extra 'proposed' nodes in regular route are ignored") {
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

    analyze(d).foreach(a => println(s""""$a","""))

    analyze(d).shouldMatchTo(
      Seq(
        "start=1(01)W",
        "end=2(02)W",
      )
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

    analyze(d).foreach(a => println(s""""$a","""))

    analyze(d, proposed = true).shouldMatchTo(
      Seq(
        "start=(1/01/01/W)",
        "end=(2/02/02/W)"
      )
    )
  }

  private def analyze(d: RouteTestData, proposed: Boolean = false): Seq[String] = {
    val relation = d.data.relations(1L)
    val context = RouteNodesAnalyzer.analyze(
      RouteLinkAnalyzer.analyze(
        RouteDetailAnalysisContext(
          relation,
          None,
          nodeNetwork = true,
          _networkTypes = Some(Seq(NetworkType.hiking)),
          scopedNetworkTypeOption = Some(ScopedNetworkType.rwn),
          proposed = proposed,
        )
      )
    )
    new RouteNodesAnalysisFormatter(context.routeNodesAnalysis).nodeStrings ++ context.facts.map(_.entryName)
  }
}
