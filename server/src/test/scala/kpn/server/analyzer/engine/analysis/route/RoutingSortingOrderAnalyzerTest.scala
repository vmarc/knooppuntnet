package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.Node
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.segment.Fragment
import kpn.server.analyzer.engine.analysis.route.segment.Path
import kpn.server.analyzer.engine.analysis.route.segment.Segment
import kpn.server.analyzer.engine.analysis.route.segment.SegmentFragment

class RoutingSortingOrderAnalyzerTest extends UnitTest with SharedTestObjects {

  private val (fragments, correctOrderPath, wrongOrderPath) = {

    val node1 = newNode(1)
    val node2 = newNode(2)
    val node3 = newNode(3)
    val node4 = newNode(4)

    val fragment1 = fragment(11, node1, node2)
    val fragment2 = fragment(12, node2, node3)
    val fragment3 = fragment(13, node3, node4)

    val fragments = Seq(fragment1, fragment2, fragment3)

    val sf1 = SegmentFragment(fragment1)
    val sf2 = SegmentFragment(fragment2)
    val sf3 = SegmentFragment(fragment3)

    val correctOrder = Path(None, None, node1.id, node4.id, Seq(Segment("", fragments = Seq(sf1, sf2, sf3))))
    val wrongOrder = Path(None, None, node1.id, node4.id, Seq(Segment("", fragments = Seq(sf1, sf3, sf2))))

    (fragments, correctOrder, wrongOrder)
  }

  test("all ok") {
    val analysis = analyze(RouteStructure(forwardPath = Some(correctOrderPath)))
    assert(analysis.forwardOk)
    assert(analysis.backwardOk)
    assert(analysis.startTentaclesOk)
    assert(analysis.endTentaclesOk)
  }

  test("forward nok") {
    val analysis = analyze(RouteStructure(forwardPath = Some(wrongOrderPath)))
    assert(!analysis.forwardOk)
  }

  test("backward nok") {
    val analysis = analyze(RouteStructure(backwardPath = Some(wrongOrderPath)))
    assert(!analysis.backwardOk)
  }

  test("start tentacle nok") {
    val analysis = analyze(RouteStructure(startTentaclePaths = Seq(wrongOrderPath)))
    assert(!analysis.startTentaclesOk)
  }

  test("end tentacle nok") {
    val analysis = analyze(RouteStructure(endTentaclePaths = Seq(wrongOrderPath)))
    assert(!analysis.endTentaclesOk)
  }

  private def fragment(wayId: Long, from: Node, to: Node): Fragment = {
    Fragment.create(None, None, newWay(wayId, nodes = Seq(from, to)), Seq(), None)
  }

  private def analyze(structure: RouteStructure): RouteSortingOrderAnalysis = {
    new RouteSortingOrderAnalyzer(fragments, structure).analysis
  }
}
