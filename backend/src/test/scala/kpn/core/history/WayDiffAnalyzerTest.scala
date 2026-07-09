package kpn.core.history

import kpn.api.common.data.Way
import kpn.api.common.diff.TagDiff
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.WayUpdate
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newNode
import kpn.core.test.TestObjects.newWay
import kpn.core.test.TestObjects.newWayUpdate
import kpn.core.util.UnitTest

class WayDiffAnalyzerTest extends UnitTest {

  test("node removed") {
    val before = newWay(101, version = 2, nodes = Vector(newNode(1001), newNode(1002)))
    val after = newWay(101, version = 3, nodes = Vector(newNode(1001)))
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
    val before = newWay(101, version = 2, nodes = Vector(newNode(1001)))
    val after = newWay(101, version = 3, nodes = Vector(newNode(1001), newNode(1002)))
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
    val nodes = Vector(newNode(1001), newNode(1002))
    val before = newWay(101, version = 2, nodes = nodes, tags = Tags.from("a" -> "1"))
    val after = newWay(101, version = 3, nodes = nodes, tags = Tags.from("a" -> "2"))
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
    val before = newWay(101, version = 2, nodes = Vector(newNode(1001), newNode(1002)))
    val after = newWay(101, version = 3, nodes = Vector(newNode(1002), newNode(1001)))
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

  private def wayUpdate(wayBefore: Way, wayAfter: Way): WayUpdate = {
    new WayDiffAnalyzer(wayBefore, wayAfter).analysis.get
  }
}
