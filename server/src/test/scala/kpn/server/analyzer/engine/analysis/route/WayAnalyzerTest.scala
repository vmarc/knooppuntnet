package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.SharedTestObjects
import kpn.core.data.Data
import kpn.core.util.UnitTest

class WayAnalyzerTest extends UnitTest with SharedTestObjects {

  test("linear way is not self intersecting or closed loop") {

    val w = new RouteTestData("01-02") {
      way(10, 1, 2, 3)
    }.data.ways.values.head

    assert(!WayAnalyzer.isClosedLoop(w))
    assert(!WayAnalyzer.isSelfIntersecting(w))
  }

  test("a self intersecting way is not necessarily a closed loop") {

    val w = new RouteTestData("01-02") {
      way(10, 1, 2, 3, 4, 2)
    }.data.ways.values.head

    assert(!WayAnalyzer.isClosedLoop(w))
    assert(WayAnalyzer.isSelfIntersecting(w))
  }

  test("closed loop when begin and endnode the same; do not consider this 'self intersecting'") {

    val w = new RouteTestData("01-02") {
      way(10, 1, 2, 3, 1)
    }.data.ways.values.head

    assert(WayAnalyzer.isClosedLoop(w))
    assert(!WayAnalyzer.isSelfIntersecting(w))
  }

  test("way with single segment cannot be closed loop or self intersecting") {

    val w = new RouteTestData("01-02") {
      way(10, 1, 2)
    }.data.ways.values.head

    assert(!WayAnalyzer.isClosedLoop(w))
    assert(!WayAnalyzer.isSelfIntersecting(w))
  }

  test("way without nodes cannot be closed loop or self intersecting") {

    val w = new RouteTestData("01-02") {
      way(10)
    }.data.ways.values.head

    assert(!WayAnalyzer.isClosedLoop(w))
    assert(!WayAnalyzer.isSelfIntersecting(w))
  }
}
