package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.route.UnusedSegmentAnalyzer
import kpn.api.common.SharedTestObjects
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class UnusedSegmentAnalyzerTest extends FunSuite with Matchers with SharedTestObjects {

  private val n1 = newNode(1)
  private val n2 = newNode(2)
  private val n3 = newNode(3)
  private val n4 = newNode(4)
  private val n5 = newNode(5)
  private val n6 = newNode(6)
  private val n7 = newNode(7)
  private val n8 = newNode(8)
  private val n9 = newNode(9)

  test("adjecent unused fragments are combined into segments") {

    val way1 = newWay(10)
    val way2 = newWay(30)

    val b = new FragmentBuilder

    val f1 = b.fragment(way1, n1, n2)
    val f2 = b.fragment(way1, n2, n3)
    b.fragment(way1, n3, n4)
    b.fragment(way2, n4, n5)
    b.fragment(way2, n6, n7)
    b.fragment(way2, n7, n8)

    val segment1 = Segment("", Seq(f1, f2))
    val usedSegments = Seq(segment1)

    analyze(usedSegments, b.fragments.toSeq) should equal(Set(Seq(3, 4, 5), Seq(6, 7, 8)))
  }

  test("if one roundabout fragment is used, then entire roundabout is considered used") {

    val way1 = newWay(10, tags = Tags.from("junction" -> "roundabout"))
    val way2 = newWay(30)

    val b = new FragmentBuilder

    val f1 = b.fragment(way1, n1, n2)
    val f2 = b.fragment(way1, n2, n3)
    b.fragment(way1, n3, n4)
    b.fragment(way2, n4, n5)
    b.fragment(way2, n6, n7)
    b.fragment(way2, n7, n8)

    val segment1 = Segment("", Seq(f1, f2))
    val usedSegments = Seq(segment1)

    analyze(usedSegments, b.fragments.toSeq) should equal(Set(Seq(4, 5), Seq(6, 7, 8)))
  }

  test("if one fragment of a closed loop is used, then entire loop is considered used") {

    val way1 = newWay(10, nodes = Seq(n1, n2, n3, n4, n1))
    val way2 = newWay(30)

    val b = new FragmentBuilder

    val f1 = b.fragment(way1, n1, n2, n3)
    val f2 = b.fragment(way1, n3, n4, n1)
    b.fragment(way2, n4, n5)
    b.fragment(way2, n5, n6)

    val segment1 = Segment("", Seq(f1, f2))
    val usedSegments = Seq(segment1)

    analyze(usedSegments, b.fragments.toSeq) should equal(Set(Seq(4, 5, 6)))
  }

  private def analyze(usedSegments: Seq[Segment], fragments: Seq[Fragment]): Set[Seq[Long]] = {
    val segments: Seq[Segment] = new UnusedSegmentAnalyzer(usedSegments, fragments).find
    Segment.toNodeIds(segments)
  }
}
