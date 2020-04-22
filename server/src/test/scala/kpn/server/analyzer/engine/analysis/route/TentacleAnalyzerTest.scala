package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.Node
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.segment.FragmentBuilder
import kpn.server.analyzer.engine.analysis.route.segment.Path
import kpn.server.analyzer.engine.analysis.route.segment.SegmentFinder

class TentacleAnalyzerTest extends UnitTest with SharedTestObjects {

  private val n1 = newNode(1)
  private val n2 = newNode(2)
  private val n3 = newNode(3)
  private val n4 = newNode(4)
  private val n5 = newNode(5)
  private val n6 = newNode(6)
  private val n7 = newNode(7)
  private val n8 = newNode(8)
  private val n9 = newNode(9)

  test("2 tentacles arriving at 1 node") {

    val way1 = newWay(10)
    val way2 = newWay(30)

    val b = new FragmentBuilder

    // tentacle 1
    val f1 = b.fragment(way1, n1, n2)
    val f2 = b.fragment(way1, n2, n9)

    //  tentacle 2
    b.fragment(way1, n3, n4)
    b.fragment(way2, n4, n9)

    b.fragment(way2, n6, n7)
    b.fragment(way2, n7, n8)

    val allRouteNodes: Set[RouteNode] = Set.empty
    val allNodes: Set[Node] = Set(n1, n3, n9)

    val nodes: Seq[Node] = Seq(n1, n3, n9)
    val segmentFinder: SegmentFinder = new SegmentFinder(NetworkType.hiking, allRouteNodes, allNodes)

    val tentacles: Seq[Path] = new TentacleAnalyzer(segmentFinder, b.fragments.toSeq, nodes).findTentacles

    Path.toNodeIds(tentacles) should equal(Set(Seq(1, 2, 9), Seq(3, 4, 9)))
  }

  test("2 stacked tentacles") {

    val way1 = newWay(10)
    val way2 = newWay(30)

    val b = new FragmentBuilder

    // tentacle 1
    val f1 = b.fragment(way1, n1, n2)
    val f2 = b.fragment(way1, n2, n3)

    //  tentacle 2
    b.fragment(way1, n3, n4)
    b.fragment(way2, n4, n5)

    // unused fragments
    b.fragment(way2, n6, n7)
    b.fragment(way2, n7, n8)

    val allRouteNodes: Set[RouteNode] = Set.empty
    val allNodes: Set[Node] = Set(n1, n3, n5)

    val nodes: Seq[Node] = Seq(n1, n3, n5)
    val segmentFinder: SegmentFinder = new SegmentFinder(NetworkType.hiking, allRouteNodes, allNodes)

    val tentacles: Seq[Path] = new TentacleAnalyzer(segmentFinder, b.fragments.toSeq, nodes).findTentacles

    Path.toNodeIds(tentacles) should equal(Set(Seq(1, 2, 3), Seq(3, 4, 5)))
  }

  test("2 stacked tentacles reversed") {

    val way1 = newWay(10, tags = Tags.from("oneway" -> "yes"))
    val way2 = newWay(20, tags = Tags.from("oneway" -> "yes"))

    val b = new FragmentBuilder

    // tentacle 1
    val f1 = b.fragment(way1, n3, n2)
    val f2 = b.fragment(way1, n2, n1)

    //  tentacle 2
    b.fragment(way2, n5, n4)
    b.fragment(way2, n4, n3)

    // unused fragments
    b.fragment(way2, n6, n7)
    b.fragment(way2, n7, n8)

    val allRouteNodes: Set[RouteNode] = Set.empty
    val allNodes: Set[Node] = Set(n1, n3, n5)

    val nodes: Seq[Node] = Seq(n1, n3, n5)
    val segmentFinder: SegmentFinder = new SegmentFinder(NetworkType.cycling, allRouteNodes, allNodes)

    val tentacles: Seq[Path] = new TentacleAnalyzer(segmentFinder, b.fragments.toSeq, nodes).findTentacles

    Path.toNodeIds(tentacles) should equal(Set(Seq(3, 2, 1), Seq(5, 4, 3)))
  }
}
