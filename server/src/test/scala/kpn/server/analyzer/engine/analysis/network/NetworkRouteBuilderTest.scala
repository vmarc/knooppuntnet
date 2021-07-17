package kpn.server.analyzer.engine.analysis.network

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.Fact._
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeInfoAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedRoute
import org.scalamock.scalatest.MockFactory

/*

  Route 14-21 is considered broken, but it is not => end in square ==> adapt logic

  Route 14-29 is considered broken, is this because the end node was missing in the route definition?
   - display start and end nodes in route details
   - also display direction of each way in the route details?

  Walking network 'Brabantse Wal' fix broken route 01-69

  Waddenwandelen 81-82 considered broken:
    - route without node definitions
    - the nodes in first way need to be reversed to connect to the second way

 */

class TData extends SharedTestObjects {

  val node1: RawNode = newRawNodeWithName(1, "01")
  val node2: RawNode = newRawNode(2)
  val node3: RawNode = newRawNode(3)
  val node4: RawNode = newRawNodeWithName(4, "04")

  val node5: RawNode = newRawNode(5)
  val node6: RawNode = newRawNode(6)
  val node7: RawNode = newRawNode(7)

  val node1b: RawNode = newRawNodeWithName(8, "01")
  val node4b: RawNode = newRawNodeWithName(9, "04")

  val nodes: Seq[RawNode] = Seq(node1, node2, node3, node4, node5, node6, node7, node1b, node4b)

  val way12: RawWay = newRawWay(12, nodeIds = Seq(node1.id, node2.id), tags = Tags.from("highway" -> "track"))
  val way23: RawWay = newRawWay(23, nodeIds = Seq(node2.id, node3.id), tags = Tags.from("highway" -> "track"))
  val way34: RawWay = newRawWay(34, nodeIds = Seq(node3.id, node4.id), tags = Tags.from("highway" -> "track"))

  val way21: RawWay = newRawWay(21, nodeIds = Seq(node2.id, node1.id), tags = Tags.from("highway" -> "track"))
  val way32: RawWay = newRawWay(32, nodeIds = Seq(node3.id, node2.id), tags = Tags.from("highway" -> "track"))
  val way43: RawWay = newRawWay(43, nodeIds = Seq(node4.id, node3.id), tags = Tags.from("highway" -> "track"))

  val closedWay: RawWay = newRawWay(526375, nodeIds = Seq(node5.id, node2.id, node6.id, node3.id, node7.id, node5.id), tags = Tags.from("highway" -> "track"))

  val closedWay2: RawWay = newRawWay(5263475, nodeIds = Seq(node5.id, node2.id, node6.id, node3.id, node4.id, node7.id, node5.id), tags = Tags.from("highway" -> "track"))

  val closedWay3: RawWay = newRawWay(5263475, nodeIds = Seq(node5.id, node1.id, node2.id, node6.id, node3.id, node7.id, node5.id), tags = Tags.from("highway" -> "track"))

  val doubleStartWay: RawWay = newRawWay(13, nodeIds = Seq(node1b.id, node1.id, node2.id, node3.id), tags = Tags.from("highway" -> "track"))
  val doubleEndWay: RawWay = newRawWay(24, nodeIds = Seq(node2.id, node3.id, node4.id, node4b.id), tags = Tags.from("highway" -> "track"))

  val ways: Seq[RawWay] = Seq(way12, way23, way34, way21, way32, way43, closedWay, closedWay2)
}

class NetworkRouteBuilderTest extends UnitTest with MockFactory with SharedTestObjects {

  //  test("contiguous route - without explicit start and end nodes") {
  //
  //    val d = new Data()
  //
  //    val analysis = analyzeRoute(d, "01-04", Seq(member(d.way12), member(d.way23), member(d.way34)))
  //
  //    assert(!analysis.hasFact(RouteBroken))
  ////    assert(!route.startIncluded)
  ////    assert(!route.endIncluded)
  ////    route.start.get.node should equal(d.node1)
  ////    route.end.get.node should equal(d.node4)
  //
  ////    route.segments.size should equal(1)
  ////
  ////    route.segments.head match {
  ////
  ////      case segment: RouteMemberSimpleSegment =>
  ////
  ////        segment.members.size should equal(3)
  ////
  ////        //case class RouteMemberWay(name: String, role: Option[String], way: Way, from: String, to:String, reverse: Boolean, networkNodes: Seq[NetworkNode]) extends RouteMember {
  ////        segment.members(0).from should equal("1")
  ////        segment.members(0).to should equal("2")
  ////        segment.members(1).from should equal("2")
  ////        segment.members(1).to should equal("3")
  ////        segment.members(2).from should equal("3")
  ////        segment.members(2).to should equal("4")
  ////
  ////        assert(!segment.members(0).reverse)
  ////        assert(!segment.members(1).reverse)
  ////        assert(!segment.members(2).reverse)
  ////
  ////        segment.members(0).role should equal(None)
  ////        segment.members(1).role should equal(None)
  ////        segment.members(2).role should equal(None)
  ////
  ////      case _ => fail()
  ////    }
  //  }
  //
  //  test("route without start node") {
  //
  ////    val d = new Data()
  ////
  ////    val route = buildRoute(d, "02-04", Seq(member(d.way23), member(d.way34)))
  ////
  ////    route.facts.size should equal(1)  ===> need new fact to indicate that start node is missing
  ////    assert(route.broken)
  ////    assert(!route.startIncluded)
  ////    assert(!route.endIncluded)
  ////    route.start should equal(None)
  ////    route.end should equal(Some(d.node4))
  //  }
  //
  //  test("route without end node") {
  //
  ////    val d = new Data()
  ////
  ////    val route = buildRoute(d, "02-04", Seq(member(d.way12), member(d.way23)))
  ////
  ////    route.facts.size should equal(1)  ===> need new fact to indicate that end node is missing
  ////    assert(route.broken)
  ////    assert(!route.startIncluded)
  ////    assert(!route.endIncluded)
  ////    route.start should equal(None)
  ////    route.end should equal(Some(d.node4))
  //  }
  //
  //  test("broken route - without start and end nodes") {
  //
  //    val d = new Data()
  //
  //    val analysis = analyzeRoute(d, "01-04", Seq(member(d.way12), member(d.way34)))
  //    assert(analysis.hasFact(RouteBroken))
  ////    assert(!route.startIncluded)
  ////    assert(!route.endIncluded)
  ////    route.start.get.node should equal(d.node1)
  ////    route.end.get.node should equal(d.node4)
  //  }
  //
  //  test("contiguous route - with explicit start and end nodes") {
  //
  //    val d = new Data()
  //
  //    val analysis = analyzeRoute(d, "01-04", Seq(member(d.node1), member(d.way12), member(d.way23), member(d.way34), member(d.node4)))
  //    assert(!analysis.hasFact(RouteBroken))
  ////    assert(route.startIncluded)
  ////    assert(route.endIncluded)
  ////    route.start.get.node should equal(d.node1)
  ////    route.end.get.node should equal(d.node4)
  //  }
  //
  //  test("broken route - with explicit start and end nodes") {
  //
  //    val d = new Data()
  //
  //    val analysis = analyzeRoute(d, "01-04", Seq(member(d.node1), member(d.way12), member(d.way34), member(d.node4)))
  //
  //    assert(analysis.hasFact(RouteBroken))
  ////    assert(route.startIncluded)
  ////    assert(route.endIncluded)
  ////    route.start.get.node should equal(d.node1)
  ////    route.end.get.node should equal(d.node4)
  //  }
  //
  //  test("contiguous route - no start and end nodes - way in reverse direction at the start") {
  //
  //    val d = new Data()
  //
  //    val analysis = analyzeRoute(d, "01-04", Seq(member(d.way21), member(d.way23), member(d.way34)))
  //
  //    assert(!analysis.hasFact(RouteBroken))
  //
  ////    assert(!route.startIncluded)
  ////    assert(!route.endIncluded)
  ////    route.start.get.id should equal(d.node1.id)
  ////    route.end.get.id should equal(d.node4.id)
  //  }
  //
  //  test("contiguous route - no start and end nodes - way in reverse direction in the middle") {
  //
  //    val d = new Data()
  //
  //    val analysis = analyzeRoute(d, "01-04", Seq(member(d.way12), member(d.way32), member(d.way34)))
  //
  //    assert(!analysis.hasFact(RouteBroken))
  //
  ////    route.start.get.node should equal(d.node1)
  ////    route.end.get.node should equal(d.node4)
  //  }
  //
  //  test("contiguous route - no start and end nodes - way in reverse direction at the end") {
  //
  //    val d = new Data()
  //
  //    val analysis = analyzeRoute(d, "01-04", Seq(member(d.way12), member(d.way23), member(d.way43)))
  //
  //    assert(!analysis.hasFact(RouteBroken))
  ////    route.start.get.node should equal(d.node1)
  ////    route.end.get.node should equal(d.node4)
  //  }
  //
  //  test("contiguous route containing closed way in the middle") {
  //
  //    val d = new Data()
  //
  //    val analysis1 = analyzeRoute(d, "01-04", Seq(member(d.way12), member(d.closedWay), member(d.way34)))
  //    val analysis2 = analyzeRoute(d, "01-04", Seq(member(d.way21), member(d.closedWay), member(d.way34)))
  //    val analysis3 = analyzeRoute(d, "01-04", Seq(member(d.way12), member(d.closedWay), member(d.way43)))
  //
  //    assert(!analysis1.hasFact(RouteBroken))
  //    assert(!analysis2.hasFact(RouteBroken))
  //    assert(!analysis3.hasFact(RouteBroken))
  //
  ////    route1.start.get.node should equal(d.node1)
  ////    route2.start.get.node should equal(d.node1)
  ////    route3.start.get.node should equal(d.node1)
  //
  ////    route1.end.get.node should equal(d.node4)
  ////    route2.end.get.node should equal(d.node4)
  ////    route3.end.get.node should equal(d.node4)
  //  }
  //
  //  test("contiguous route containing closed way at the end") {
  //
  //    val d = new Data()
  //
  //    val analysis1 = analyzeRoute(d, "01-04", Seq(member(d.way12), member(d.closedWay2)))
  //    val analysis2 = analyzeRoute(d, "01-04", Seq(member(d.way21), member(d.closedWay2)))
  //
  //    println("FACTS1 = " + analysis1.facts.facts)
  //    println("FACTS2 = " + analysis2.facts.facts)
  //
  //
  //    assert(!analysis1.hasFact(RouteBroken))
  //    assert(!analysis2.hasFact(RouteBroken))
  //
  ////    route1.end should equal(None)
  ////    route1.start.get.node should equal(d.node1)
  ////
  ////    route2.end should equal(None)
  ////    route2.start.get.node should equal(d.node1)
  //  }

  ignore("contiguous route containing closed way at the start") {

    val d = new TData()

    val analysis1 = analyzeRoute(d, "01-04", Seq(member(d.closedWay3), member(d.way34)))
    val analysis2 = analyzeRoute(d, "01-04", Seq(member(d.closedWay3), member(d.way43)))

    println("STRUCTURE=" + analysis1.structure)

    println("FACTS3 = " + analysis1.route.facts)
    println("FACTS4 = " + analysis2.route.facts)
    assert(!analysis1.route.facts.contains(RouteBroken))
    assert(!analysis2.route.facts.contains(RouteBroken))

    //    route1.start should equal(None)
    //    route1.end.get.node should equal(d.node4)
    //
    //    route2.start should equal(None)
    //    route2.end.get.node should equal(d.node4)
  }

  //  test("route with double start") {
  //
  ////    val d = new Data()
  ////
  ////    val route = buildRoute(d, "01-04", Seq(member(d.doubleStartWay), member(d.way34)))
  ////
  ////    route.facts.size should equal(0)
  ////    assert(!route.isBroken)
  ////    assert(!route.startIncluded)
  ////    assert(!route.endIncluded)
  ////    route.start.get.node should equal(d.node1)
  ////    route.end.get.node should equal(d.node4)
  //  }
  //
  //  test("route with double end") {
  //
  ////    val d = new Data()
  ////
  ////    val route = buildRoute(d, "01-04", Seq(member(d.way21), member(d.doubleEndWay)))
  ////
  ////    route.facts.size should equal(0)
  ////    assert(!route.isBroken)
  ////    assert(!route.startIncluded)
  ////    assert(!route.endIncluded)
  ////    route.start.get.node should equal(d.node1)
  ////    route.end.get.node should equal(d.node4)
  //  }

  private def member(way: RawWay): RawMember = RawMember("way", way.id, None)

  private def member(node: RawNode): RawMember = RawMember("node", node.id, None)

  private def analyzeRoute(
    d: TData,
    name: String,
    members: Seq[RawMember]
  ): RouteAnalysis = {
    val relation = routeRelation(100, name, members)
    analyzeRoute(d, relation)
  }

  private def routeRelation(id: Long, name: String, members: Seq[RawMember]): RawRelation = {
    val tags = Tags.from(
      "network" -> "rwn",
      "type" -> "route",
      "route" -> "hiking",
      "note" -> name
    )
    newRawRelation(id, members = members, tags = tags)
  }

  private def analyzeRoute(d: TData, rawRouteRelation: RawRelation): RouteAnalysis = {

    val data = {
      val rawData = RawData(None, d.nodes, d.ways, Seq(rawRouteRelation))
      new DataBuilder(rawData).data
    }
    val routeRelation = data.relations(rawRouteRelation.id)

    val analysisContext = new AnalysisContext()
    val tileCalculator = new TileCalculatorImpl()
    val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val routeNodeInfoAnalyzer = new RouteNodeInfoAnalyzerImpl(analysisContext, oldNodeAnalyzer)
    val routeAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeLocationAnalyzer,
      routeTileAnalyzer,
      routeNodeInfoAnalyzer
    )
    routeAnalyzer.analyze(
      LoadedRoute(
        None,
        ScopedNetworkType.rwn,
        data,
        routeRelation
      ),
      orphan = false
    )
  }
}
