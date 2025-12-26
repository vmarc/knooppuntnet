package kpn.core.history

import kpn.api.common.diff.TagDiff
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.WayUpdate
import kpn.api.custom.Tags
import kpn.core.doc.DetailWay
import kpn.core.test.TestObjects.newDetailWay
import kpn.core.test.TestObjects.newWayUpdate
import kpn.core.util.UnitTest

class WayDiffAnalyzerTest extends UnitTest {

  test("node removed") {
    val before = newDetailWay(101, version = 2, nodeIds = Seq(1001, 1002))
    val after = newDetailWay(101, version = 3, nodeIds = Seq(1001))
    assertEqual(
      wayUpdate(before, after),
      newWayUpdate(
        101,
        before.toMeta,
        after.toMeta,
        removedNodeIds = Seq(1002)
      )
    )
  }

  test("node added") {
    val before = newDetailWay(101, version = 2, nodeIds = Seq(1001))
    val after = newDetailWay(101, version = 3, nodeIds = Seq(1001, 1002))
    assertEqual(
      wayUpdate(before, after),
      newWayUpdate(
        101,
        before.toMeta,
        after.toMeta,
        addedNodeIds = Seq(1002)
      )
    )
  }

  test("tags changed") {
    val nodeIds = Seq(1001L, 1002L)
    val before = newDetailWay(101, version = 2, nodeIds = nodeIds, tags = Tags.from("a" -> "1"))
    val after = newDetailWay(101, version = 3, nodeIds = nodeIds, tags = Tags.from("a" -> "2"))
    assertEqual(
      wayUpdate(before, after),
      newWayUpdate(
        101,
        before.toMeta,
        after.toMeta,
        tagDiffs = Some(
          TagDiffs(
            Seq.empty,
            Seq(
              TagDiff.update("a", "1", "2")
            )
          )
        )
      )
    )
  }

  test("direction reversed") {
    val before = newDetailWay(101, version = 2, nodeIds = Seq(1001, 1002))
    val after = newDetailWay(101, version = 3, nodeIds = Seq(1002, 1001))
    assertEqual(
      wayUpdate(before, after),
      newWayUpdate(
        101,
        before.toMeta,
        after.toMeta,
        directionReversed = true
      )
    )
  }

  private def wayUpdate(wayBefore: DetailWay, wayAfter: DetailWay): WayUpdate = {
    new WayDiffAnalyzer(wayBefore, wayAfter).analysis.get
  }
}
