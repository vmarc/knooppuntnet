package kpn.core.history

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.Way
import kpn.api.common.diff.NodeUpdate
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.WayUpdate
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.util.UnitTest

class WayDiffAnalyzerTest extends UnitTest with SharedTestObjects {

  test("node removed") {
    val before = newWay(101, version = 2, nodes = Seq(newNode(1001), newNode(1002)))
    val after = newWay(101, version = 3, nodes = Seq(newNode(1001)))
    wayUpdate(before, after).shouldMatchTo(
      WayUpdate(
        101,
        before.toMeta,
        after.toMeta,
        removedNodes = Seq(
          newNode(1002).raw
        )
      )
    )
  }

  test("node added") {
    val before = newWay(101, version = 2, nodes = Seq(newNode(1001)))
    val after = newWay(101, version = 3, nodes = Seq(newNode(1001), newNode(1002)))
    wayUpdate(before, after).shouldMatchTo(
      WayUpdate(
        101,
        before.toMeta,
        after.toMeta,
        addedNodes = Seq(
          newNode(1002).raw
        )
      )
    )
  }

  test("node updated") {
    val nodeBefore = newNode(1002, version = 1)
    val nodeAfter = newNode(1002, version = 2)
    val before = newWay(101, 2, Timestamp(2015, 8, 11, 0, 0, 0), 100, Seq(newNode(1001), nodeBefore), Tags.from("a" -> "1"))
    val after = newWay(101, 3, Timestamp(2015, 8, 11, 12, 0, 0), 101, Seq(newNode(1001), nodeAfter), Tags.from("a" -> "1"))
    wayUpdate(before, after).shouldMatchTo(
      WayUpdate(
        101,
        before.toMeta,
        after.toMeta,
        updatedNodes = Seq(
          NodeUpdate(
            nodeBefore.raw,
            nodeAfter.raw
          )
        )
      )
    )
  }

  test("tags changed") {
    val nodes = Seq(newNode(1001), newNode(1002))
    val before = newWay(101, version = 2, nodes = nodes, tags = Tags.from("a" -> "1"))
    val after = newWay(101, version = 3, nodes = nodes, tags = Tags.from("a" -> "2"))
    wayUpdate(before, after).shouldMatchTo(
      WayUpdate(
        101,
        before.toMeta,
        after.toMeta,
        tagDiffs = Some(
          TagDiffs(
            Seq.empty,
            Seq(
              TagDetail(
                TagDetailType.Update,
                "a",
                Some("1"),
                Some("2")
              )
            )
          )
        )
      )
    )
  }

  test("direction reversed") {
    val before = newWay(101, version = 2, nodes = Seq(newNode(1001), newNode(1002)))
    val after = newWay(101, version = 3, nodes = Seq(newNode(1002), newNode(1001)))
    wayUpdate(before, after).shouldMatchTo(
      WayUpdate(
        101,
        before.toMeta,
        after.toMeta,
        directionReversed = true
      )
    )
  }

  private def wayUpdate(wayBefore: Way, wayAfter: Way): WayUpdate = {
    new WayDiffAnalyzer(wayBefore, wayAfter).analysis.get
  }
}
