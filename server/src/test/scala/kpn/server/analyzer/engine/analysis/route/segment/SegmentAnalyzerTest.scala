package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.RouteNodeFormatter
import kpn.server.analyzer.engine.analysis.route.RouteStructure
import kpn.server.analyzer.engine.analysis.route.RouteStructureFormatter
import kpn.server.analyzer.engine.analysis.route.RouteTestData
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNameAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeInfo
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute

class SegmentAnalyzerTest extends UnitTest {

  test("single way route") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(4, "02")
      memberWay(10, "", 1, 2, 3, 4)
    }

    assertSegments(d,
      "forward=(01-02 via +<01-02 10>)," +
        "backward=(02-01 via -<01-02 10>)"
    )
  }

  test("simple route") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(6, "02")
      memberWay(10, "", 1, 2, 3)
      memberWay(11, "", 3, 4, 5)
      memberWay(12, "", 5, 6)
    }

    assertSegments(d,
      "forward=(01-02 via +<01- 10>+<11>+<-02 12>)," +
        "backward=(02-01 via -<-02 12>-<11>-<01- 10>)"
    )
  }

  test("route with reversed fragment") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(6, "02")
      memberWay(10, "", 1, 2, 3)
      memberWay(11, "", 5, 4, 3)
      memberWay(12, "", 5, 6)
    }

    assertSegments(d,
      "forward=(01-02 via +<01- 10>-<11>+<-02 12>)," +
        "backward=(02-01 via -<-02 12>+<11>-<01- 10>)"
    )
  }

  test("split route") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(8, "02")
      memberWay(10, "", 1, 2, 3)
      memberWay(11, "forward", 3, 4, 5)
      memberWay(12, "forward", 5, 6, 7)
      memberWay(13, "backward", 3, 9, 10)
      memberWay(14, "backward", 10, 11, 7)
      memberWay(15, "", 7, 8)
    }

    assertSegments(d,
      "forward=(01-02 via +<01- 10>+>11>+>12>+<-02 15>)," +
        "backward=(02-01 via -<-02 15>-<14<-<13<-<01- 10>)"
    )
  }

  test("start segment") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(5, "01")
      node(8, "02")
      memberWay(10, "", 1, 2, 3)
      memberWay(11, "", 3, 4, 5)
      memberWay(12, "", 5, 6, 7)
      memberWay(13, "", 7, 8)
    }

    assertSegments(d,
      "forward=(01.a-02 via +<01.a- 12>+<-02 13>)," +
        "backward=(02-01.a via -<-02 13>-<01.a- 12>)," +
        "startTentacles=(01.a-01.b via -<-01.a 11>-<01.b- 10>)"
    )
  }

  test("end segment") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(5, "02")
      node(9, "02")
      memberWay(10, "", 1, 2, 3)
      memberWay(11, "", 3, 4, 5)
      memberWay(12, "", 5, 6, 7)
      memberWay(13, "", 7, 8, 9)
    }

    assertSegments(d,
      "forward=(01-02.a via +<01- 10>+<-02.a 11>)," +
        "backward=(02.a-01 via -<-02.a 11>-<01- 10>)," +
        "endTentacles=(02.a-02.b via +<02.a- 12>+<-02.b 13>)"
    )
  }

  test("start segment with forward role (like 2923290)") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(3, "01")
      node(5, "02")
      memberWay(10, "forward", 1, 2, 3)
      memberWay(11, "", 3, 4, 5)
    }

    assertSegments(d,
      "forward=(01.a-02 via +<01.a-02 11>)," +
        "backward=(02-01.a via -<01.a-02 11>)," +
        "startTentacles=(01.b-01.a via +>01.b-01.a 10>)"
    )
  }

  test("start segment with forward role and preceeding stacked tentacle") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(3, "01")
      node(5, "01")
      node(7, "02")
      memberWay(10, "forward", 1, 2, 3)
      memberWay(11, "forward", 3, 4, 5)
      memberWay(12, "", 5, 6, 7)
    }

    assertSegments(d,
      "forward=(01.a-02 via +<01.a-02 12>)," +
        "backward=(02-01.a via -<01.a-02 12>)," +
        "startTentacles=(01.b-01.a via +>01.b-01.a 11>,01.c-01.b via +>01.c-01.b 10>)"
    )
  }

  test("end tentacle with backward role (like 2505614)") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(3, "02")
      node(5, "02")
      memberWay(11, "", 1, 2, 3)
      memberWay(12, "backward", 3, 4, 5)
    }

    assertSegments(d,
      "forward=(01-02.a via +<01-02.a 11>)," +
        "backward=(02.a-01 via -<01-02.a 11>)," +
        "endTentacles=(02.b-02.a via -<02.a-02.b 12<)"
    )
  }

  // TODO include tests with roundabout in tentacle, both with and without roles

  test("two start segments fork, without roles on tenctacles") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(5, "01")
      node(6, "01")
      node(13, "02")
      memberWay(10, "", 1, 2, 3)
      memberWay(11, "", 3, 4, 5)
      memberWay(12, "", 6, 7, 8)
      memberWay(13, "", 8, 9, 5)
      memberWay(14, "", 5, 10, 11)
      memberWay(15, "", 11, 12, 13)
    }

    assertSegments(d,
      "forward=(01.b-02 via +<01.b- 14>+<-02 15>)," +
        "backward=(02-01.b via -<-02 15>-<01.b- 14>)," +
        "startTentacles=(01.a-01.b via +<01.a- 12>+<-01.b 13>,01.b-01.c via -<-01.b 11>-<01.c- 10>)"
    )
  }

  test("two start segments fork, with roles on tenctacles") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(5, "01")
      node(9, "01")
      node(13, "02")
      memberWay(10, "backward", 1, 2, 3)
      memberWay(11, "backward", 3, 4, 5)
      memberWay(12, "forward", 5, 6, 7)
      memberWay(13, "forward", 7, 8, 9)
      memberWay(14, "", 5, 10, 11)
      memberWay(15, "", 11, 12, 13)
    }

    assertSegments(d,
      "forward=(01.b-02 via +<01.b- 14>+<-02 15>)," +
        "backward=(02-01.b via -<-02 15>-<01.b- 14>)," +
        "startTentacles=(01.b-01.a via +>01.b- 12>+>-01.a 13>,01.b-01.c via -<-01.b 11<-<01.c- 10<)"
    )
  }

  test("simple broken route") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(8, "02")
      memberWay(10, "", 1, 2, 3)
      memberWay(11, "", 3, 4, 5)
      memberWay(12, "", 6, 7, 8)
    }

    assertSegments(d, "forward=(01-None [broken] via +<01- 10>+<11>),backward=(02-None [broken] via -<-02 12>)")
  }

  test("start at roundabout - bicycle") {

    val d = new RouteTestData("01-02", ScopedNetworkType.rcn) {
      node(2, "01")
      node(6, "02")
      memberWay(10, Tags.from("junction" -> "roundabout"), "", 1, 2, 3, 4, 1)
      memberWay(11, "", 4, 5, 6)
    }

    assertSegments(d,
      "forward=(01-02 via +<01- 10(2-3-4)>+<-02 11>)," +
        "backward=(02-01 via -<-02 11>+<10(4-1)>+<-01 10(1-2)>)"
    )
  }

  test("start at roundabout - hiking") {

    val d = new RouteTestData("01-02", ScopedNetworkType.rwn) {
      node(2, "01")
      node(6, "02")
      memberWay(10, Tags.from("junction" -> "roundabout"), "", 1, 2, 3, 4, 1)
      memberWay(11, "", 4, 5, 6)
    }

    assertSegments(d,
      "forward=(01-02 via -<-01 10(1-2)>-<10(4-1)>+<-02 11>)," +
        "backward=(02-01 via -<-02 11>-<01- 10(2-3-4)>)"
    )
  }

  test("start at roundabout with forward and backward roles - this is an error situation") {

    val d = new RouteTestData("01-02", ScopedNetworkType.rcn) {
      node(2, "01")
      node(13, "02")
      memberWay(10, Tags.from("junction" -> "roundabout"), "backward", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 1)
      memberWay(10, Tags.from("junction" -> "roundabout"), "forward", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 1)
      memberWay(11, "", 5, 13)
    }

    assertSegments(d,
      "forward=(01-02 via +<01- 10(2-3-4-5)<+<-02 11>)," +
        "backward=(02-01 via -<-02 11>+<10(5-6-7-8-9-10-11-12-1)<+<-01 10(1-2)<)"
    )
  }

  test("roundabout consisting of separate ways (like route 1193198)") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(9, "02")
      memberWay(11, "", 1, 2, 3)
      memberWay(12, Tags.from("junction" -> "roundabout"), "forward", 3, 4)
      memberWay(13, Tags.from("junction" -> "roundabout"), "forward", 4, 5)
      memberWay(14, Tags.from("junction" -> "roundabout"), "forward", 5, 6)
      memberWay(16, Tags.from("junction" -> "roundabout"), "forward", 8, 3)
      memberWay(17, Tags.from("junction" -> "roundabout"), "forward", 7, 8)
      memberWay(18, Tags.from("junction" -> "roundabout"), "forward", 6, 7)
      memberWay(19, "", 6, 9)
    }

    assertSegments(d,
      "forward=(01-02 via +<01- 11>+>12>+>13>+>14>+<-02 19>)," +
        "backward=(02-01 via -<-02 19>+>18>+>17>+>16>-<01- 11>)"
    )
  }

  test("backward route starting from secondary end node (like 1014751)") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(6, "02")
      node(8, "02")
      memberWay(11, "", 1, 2, 3)
      memberWay(12, "", 3, 4) // 4: route splits here
      memberWay(13, "forward", 4, 5)
      memberWay(14, "forward", 5, 6) // 6: end node
      memberWay(16, "backward", 4, 7)
      memberWay(17, "backward", 7, 8) // 8: extra end node
    }

    assertSegments(d,
      "forward=(01-02.a via +<01- 11>+<12>+>13>+>-02.a 14>)," +
        "backward=(02.b-01 via -<-02.b 17<-<16<-<12>-<01- 11>)"
    )
  }

  test("forward: primary start node to secondary end node") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(5, "02")
      node(7, "02")
      memberWay(11, "", 1, 2, 3) // 3: route splits here
      memberWay(12, "backward", 3, 4)
      memberWay(13, "backward", 4, 5) // 5: primary end node
      memberWay(14, "forward", 3, 6)
      memberWay(15, "forward", 6, 7) // 7: secondary end node
    }

    assertSegments(d,
      "forward=(01-02.b via +<01- 11>+>14>+>-02.b 15>)," +
        "backward=(02.a-01 via -<-02.a 13<-<12<-<01- 11>)"
    )
  }

  test("forward: secondary start node to primary end node") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(4, "01")
      node(6, "02")
      memberWay(11, "forward", 1, 2) // 1: secondary start node
      memberWay(12, "forward", 2, 3) // 3: route splits here
      memberWay(13, "backward", 4, 5) // 5: primary start node
      memberWay(14, "backward", 5, 3)
      memberWay(15, "", 3, 6)
    }

    assertSegments(d,
      "forward=(01.b-02 via +>01.b- 11>+>12>+<-02 15>)," +
        "backward=(02-01.a via -<-02 15>-<14<-<01.a- 13<)"
    )
  }

  test("forward: secondary start node to secondary end node") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(4, "01")
      node(8, "02")
      node(10, "02")
      memberWay(11, "forward", 1, 2) // 1: secondary start node
      memberWay(12, "forward", 2, 3) // 3: route splits here
      memberWay(13, "backward", 4, 5) // 5: primary start node
      memberWay(14, "backward", 5, 3)
      memberWay(15, "", 3, 6) // 6: route splits here
      memberWay(16, "backward", 6, 7)
      memberWay(17, "backward", 7, 8) // 8: primary end node
      memberWay(18, "forward", 6, 9)
      memberWay(19, "forward", 9, 10) // 10: secondary end node
    }

    assertSegments(d,
      "forward=(01.b-02.b via +>01.b- 11>+>12>+<15>+>18>+>-02.b 19>)," +
        "backward=(02.a-01.a via -<-02.a 17<-<16<-<15>-<14<-<01.a- 13<)"
    )
  }

  test("backward: primary end node to secondary start node") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(4, "01")
      node(8, "02")
      node(10, "02")
      memberWay(11, "backward", 1, 2) // 1: secondary start node
      memberWay(12, "backward", 2, 3) // 3: route splits here
      memberWay(13, "forward", 4, 5) // 5: primary start node
      memberWay(14, "forward", 5, 3)
      memberWay(15, "", 3, 6) // 6: route splits here
      memberWay(16, "backward", 6, 7)
      memberWay(17, "backward", 7, 8) // 8: primary end node
      memberWay(18, "forward", 6, 9)
      memberWay(19, "forward", 9, 10) // 10: secondary end node
    }

    assertSegments(d,
      "forward=(01.a-02.b via +>01.a- 13>+>14>+<15>+>18>+>-02.b 19>)," +
        "backward=(02.a-01.b via -<-02.a 17<-<16<-<15>-<12<-<01.b- 11<)"
    )
  }

  test("backward: secondary end node to primary start node") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(4, "01")
      node(8, "02")
      node(10, "02")
      memberWay(11, "forward", 1, 2) // 1: secondary start node
      memberWay(12, "forward", 2, 3) // 3: route splits here
      memberWay(13, "backward", 4, 5) // 5: primary start node
      memberWay(14, "backward", 5, 3)
      memberWay(15, "", 3, 6) // 6: route splits here
      memberWay(16, "forward", 6, 7)
      memberWay(17, "forward", 7, 8) // 8: primary end node
      memberWay(18, "backward", 6, 9)
      memberWay(19, "backward", 9, 10) // 10: secondary end node
    }

    assertSegments(d,
      "forward=(01.b-02.a via +>01.b- 11>+>12>+<15>+>16>+>-02.a 17>)," +
        "backward=(02.b-01.a via -<-02.b 19<-<18<-<15>-<14<-<01.a- 13<)"
    )
  }

  test("backward: secondary end node to secondary start node") {

    val d = new RouteTestData("01-02") {
      node(1, "01")
      node(4, "01")
      node(8, "02")
      node(10, "02")
      memberWay(11, "backward", 1, 2) // 1: secondary start node
      memberWay(12, "backward", 2, 3) // 3: route splits here
      memberWay(13, "forward", 4, 5) // 5: primary start node
      memberWay(14, "forward", 5, 3)
      memberWay(15, "", 3, 6) // 6: route splits here
      memberWay(16, "forward", 6, 7)
      memberWay(17, "forward", 7, 8) // 8: primary end node
      memberWay(18, "backward", 6, 9)
      memberWay(19, "backward", 9, 10) // 10: secondary end node
    }

    assertSegments(d,
      "forward=(01.a-02.a via +>01.a- 13>+>14>+<15>+>16>+>-02.a 17>)," +
        "backward=(02.b-01.b via -<-02.b 19<-<18<-<15>-<12<-<01.b- 11<)"
    )
  }

  test("for hiking route, choose shortest path through roundabout") {

    def testData(scopedNetworkType: ScopedNetworkType) = new RouteTestData("01-02", scopedNetworkType) {
      node(1, "", 1, 0)
      node(2, "01", 1, 1)
      node(3, "", 0, 1)
      node(4, "", -1, 1)
      node(5, "", -1, 0)
      node(6, "", -1, -1)
      node(7, "", 0, -1)
      node(8, "", 1, -1)
      node(9, "02", 2, 0)
      memberWay(10, Tags.from("junction" -> "roundabout"), "", 1, 2, 3, 4, 5, 6, 7, 8, 1)
      memberWay(11, "", 8, 9)
    }

    assertSegments(testData(ScopedNetworkType.rwn),
      "forward=(01-02 via -<-01 10(1-2)>-<10(8-1)>+<-02 11>)," + // shortest path
        "backward=(02-01 via -<-02 11>+<10(8-1)>+<-01 10(1-2)>)"
    )

    assertSegments(testData(ScopedNetworkType.rcn),
      "forward=(01-02 via +<01- 10(2-3-4-5-6-7-8)>+<-02 11>)," + // follows roundabout direction
        "backward=(02-01 via -<-02 11>+<10(8-1)>+<-01 10(1-2)>)"
    )
  }

  test("closed loop") {

    val d = new RouteTestData("01-02") {
      node(1, "", 1, 0)
      node(2, "", 1, 1)
      node(3, "", 0, 1)
      node(4, "", -1, 1)
      node(5, "", -1, 0)
      node(6, "", -1, -1)
      node(7, "", 0, -1)
      node(8, "", 1, -1)
      node(9, "01")
      node(10, "02")
      memberWay(10, "", 9, 2)
      memberWay(11, "", 1, 2, 3, 4, 5, 6, 7, 8, 1)
      memberWay(12, "", 8, 10)
    }

    assertSegments(d,
      "forward=(01-02 via +<01- 10>-<11(1-2)>-<11(8-1)>+<-02 12>)," +
        "backward=(02-01 via -<-02 12>+<11(8-1)>+<11(1-2)>-<01- 10>)"
    )
  }

  private def assertSegments(d: RouteTestData, expected: String): Unit = {
    val data = d.data
    val routeRelation = data.relations(1)
    val analysisContext = new AnalysisContext()

    val routeNodeInfos = data.nodes.values.flatMap { node =>
      node.tags(d.scopedNetworkType.nodeRefTagKey).map { ref =>
        node.id -> RouteNodeInfo(node, ref)
      }
    }.toMap

    val context1 = RouteAnalysisContext(
      analysisContext,
      loadedRoute = LoadedRoute(
        country = None,
        scopedNetworkType = d.scopedNetworkType,
        data = data,
        relation = routeRelation
      ),
      orphan = false,
      routeNodeInfos
    )
    val context2 = new RouteNameAnalyzer(context1).analyze
    val context3 = new RouteNodeAnalyzer(context2).analyze
    if (context3.routeNodeAnalysis.get.startNodes.isEmpty) fail("expected start node, but found none")
    if (context3.routeNodeAnalysis.get.endNodes.isEmpty) fail("expected end node, but found none")
    val fragmentMap = new FragmentAnalyzer(context3.routeNodeAnalysis.get.usedNodes, routeRelation.wayMembers).fragmentMap

    val structure: RouteStructure = new SegmentAnalyzer(
      d.scopedNetworkType.networkType,
      1,
      false,
      FragmentMap(FragmentFilter.filter(fragmentMap.all)),
      context3.routeNodeAnalysis.get
    ).structure

    val actual = new RouteStructureFormatter(structure).string

    if (actual != expected) {
      val b = new StringBuffer

      b.append("nodes\n")
      b.append("  start=" + context3.routeNodeAnalysis.get.startNodes.map(formatRouteNode).mkString("(", ",", ")") + "\n")
      b.append("  end=" + context3.routeNodeAnalysis.get.endNodes.map(formatRouteNode).mkString("(", ",", ")") + "\n")
      b.append("expected=")
      b.append(expected)
      b.append("\nactual  =")
      b.append(actual)
      fail(b.toString)
    }
  }

  private def formatRouteNode(routeNode: RouteNode): String = {
    new RouteNodeFormatter(routeNode).shortString
  }
}
