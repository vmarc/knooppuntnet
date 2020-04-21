package kpn.core.history

import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.api.common.SharedTestObjects
import kpn.api.common.diff.NodeUpdate
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.WayUpdate
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class WayDiffAnalyzerTest extends AnyFunSuite with Matchers with SharedTestObjects {

  test("node removed") {
    val before = newWay(101, version = 2, nodes = Seq(newNode(1001), newNode(1002)))
    val after = newWay(101, version = 3, nodes = Seq(newNode(1001)))
    val expected = WayUpdate(101, before.toMeta, after.toMeta, removedNodes = Seq(newNode(1002).raw))
    new WayDiffAnalyzer(before, after).analysis should equal(Some(expected))
  }

  test("node added") {
    val before = newWay(101, version = 2, nodes = Seq(newNode(1001)))
    val after = newWay(101, version = 3, nodes = Seq(newNode(1001), newNode(1002)))
    val expected = WayUpdate(101, before.toMeta, after.toMeta, addedNodes = Seq(newNode(1002).raw))
    new WayDiffAnalyzer(before, after).analysis should equal(Some(expected))
  }

  test("node updated") {
    val nodeBefore = newNode(1002, version = 1)
    val nodeAfter = newNode(1002, version = 2)
    val before = newWay(101, 2, Timestamp(2015, 8, 11, 0, 0, 0), 100, Seq(newNode(1001), nodeBefore), Tags.from("a" -> "1"))
    val after = newWay(101, 3, Timestamp(2015, 8, 11, 12, 0, 0), 101, Seq(newNode(1001), nodeAfter), Tags.from("a" -> "1"))
    val nodeUpdate = NodeUpdate(nodeBefore.raw, nodeAfter.raw)
    val expected = WayUpdate(101, before.toMeta, after.toMeta, updatedNodes = Seq(nodeUpdate))
    new WayDiffAnalyzer(before, after).analysis should equal(Some(expected))
  }

  test("tags changed") {
    val nodes = Seq(newNode(1001), newNode(1002))
    val before = newWay(101, version = 2, nodes = nodes, tags = Tags.from("a" -> "1"))
    val after = newWay(101, version = 3, nodes = nodes, tags = Tags.from("a" -> "2"))
    val expectedDiffs = TagDiffs(Seq(), Seq(TagDetail(TagDetailType.Update, "a", Some("1"), Some("2"))))
    val expected = WayUpdate(101, before.toMeta, after.toMeta, tagDiffs = Some(expectedDiffs))
    new WayDiffAnalyzer(before, after).analysis should equal(Some(expected))
  }

  test("direction reversed") {
    val before = newWay(101, version = 2, nodes = Seq(newNode(1001), newNode(1002)))
    val after = newWay(101, version = 3, nodes = Seq(newNode(1002), newNode(1001)))
    val expected = WayUpdate(101, before.toMeta, after.toMeta, directionReversed = true)
    new WayDiffAnalyzer(before, after).analysis should equal(Some(expected))
  }
}
