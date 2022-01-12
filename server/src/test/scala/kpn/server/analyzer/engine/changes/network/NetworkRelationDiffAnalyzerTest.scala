package kpn.server.analyzer.engine.changes.network

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.IdDiffs
import kpn.core.util.UnitTest

class NetworkRelationDiffAnalyzerTest extends UnitTest with SharedTestObjects {

  test("nodeDiffs") {

    val before = newRawRelation(
      members = Seq(
        RawMember("node", 1, None),
        RawMember("node", 3, None),
      )
    )

    val after = newRawRelation(
      members = Seq(
        RawMember("node", 2, None),
        RawMember("node", 3, Some("connection"))
      )
    )

    val analyzer = new NetworkRelationDiffAnalyzer(before, after)
    analyzer.nodeDiffs.shouldMatchTo(
      IdDiffs(
        removed = Seq(1),
        added = Seq(2),
        updated = Seq(3)
      )
    )
  }

  test("wayDiffs") {

    val before = newRawRelation(
      members = Seq(
        RawMember("way", 1, None),
      )
    )

    val after = newRawRelation(
      members = Seq(
        RawMember("way", 2, None),
      )
    )

    val analyzer = new NetworkRelationDiffAnalyzer(before, after)
    analyzer.wayDiffs.shouldMatchTo(
      IdDiffs(
        removed = Seq(1),
        added = Seq(2)
      )
    )
  }

  test("relationDiffs") {

    val before = newRawRelation(
      members = Seq(
        RawMember("relation", 1, None),
        RawMember("relation", 3, None),
      )
    )

    val after = newRawRelation(
      members = Seq(
        RawMember("relation", 2, None),
        RawMember("relation", 3, Some("connection"))
      )
    )

    val analyzer = new NetworkRelationDiffAnalyzer(before, after)
    analyzer.relationDiffs.shouldMatchTo(
      IdDiffs(
        removed = Seq(1),
        added = Seq(2),
        updated = Seq(3)
      )
    )
  }
}
