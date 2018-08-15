package kpn.core.engine.analysis.route

import kpn.core.engine.analysis.route.segment.Fragment
import kpn.core.engine.analysis.route.segment.Segment
import kpn.core.engine.analysis.route.segment.SegmentFragment
import kpn.shared.SharedTestObjects
import kpn.shared.data.Node
import org.scalatest.FunSuite
import org.scalatest.Matchers

class RoutingSortingOrderAnalyzerTest extends FunSuite with Matchers with SharedTestObjects {

  private val (fragments, okSegments, nokSegments) = {

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

    val correctOrder = Seq(Segment(segmentFragments = Seq(sf1, sf2, sf3)))
    val wrongOrder = Seq(Segment(segmentFragments = Seq(sf1, sf3, sf2)))

    (fragments, correctOrder, wrongOrder)
  }

  test("all ok") {
    val analysis = analyze(RouteStructure(forwardSegment = Some(okSegments.head)))
    analysis.forwardOk should equal(true)
    analysis.backwardOk should equal(true)
    analysis.tentacleOk should equal(true)
  }

  test("forward nok") {
    val analysis = analyze(RouteStructure(forwardSegment = Some(nokSegments.head)))
    analysis.forwardOk should equal(false)
  }

  test("backward nok") {
    val analysis = analyze(RouteStructure(backwardSegment = Some(nokSegments.head)))
    analysis.backwardOk should equal(false)
  }

  test("tentacle nok") {
    val analysis = analyze(RouteStructure(tentacles = nokSegments))
    analysis.tentacleOk should equal(false)
  }

  private def fragment(wayId: Long, from: Node, to: Node): Fragment = {
    Fragment(None, None, newWay(wayId, nodes = Seq(from, to)), Seq(), None)
  }

  private def analyze(structure: RouteStructure): RouteSortingOrderAnalysis = {
    new RouteSortingOrderAnalyzer(fragments, structure).analysis
  }
}
