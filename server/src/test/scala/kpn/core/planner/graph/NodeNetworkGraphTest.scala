package kpn.core.planner.graph

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.TrackPathKey
import kpn.core.util.UnitTest

class NodeNetworkGraphTest extends UnitTest with SharedTestObjects {

  test("find shortest path when there are 2 paths with the same number of nodes") {

    //      ______
    //   1 /   2  \ 4
    //     \___3__/
    //

    val leg12 = TrackPathKey(12, 1)
    val leg14 = TrackPathKey(14, 1)
    val leg13 = TrackPathKey(13, 1)
    val leg34 = TrackPathKey(34, 1)

    val graph = new NodeNetworkGraphImpl()
    graph.add(GraphEdge(1, 2, 2, leg12))
    graph.add(GraphEdge(2, 4, 2, leg14))
    graph.add(GraphEdge(1, 3, 1, leg13))
    graph.add(GraphEdge(3, 4, 1, leg34))

    val expected = GraphPath(1, Seq(GraphPathSegment(3, leg13), GraphPathSegment(4, leg34)))
    graph.findPath(1, 4) should equal(Some(expected))
  }

  test("source node not in graph") {
    val graph = new NodeNetworkGraphImpl()
    graph.findPath(1, 4) should equal(None)
  }

  test("sink node not in graph") {
    val graph = new NodeNetworkGraphImpl()
    graph.add(GraphEdge(1, 2, 1, TrackPathKey(12, 1)))
    graph.findPath(1, 4) should equal(None)
  }

}
