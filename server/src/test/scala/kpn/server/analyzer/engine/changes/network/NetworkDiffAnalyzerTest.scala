package kpn.server.analyzer.engine.changes.network

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.IdDiffs
import kpn.core.util.UnitTest

class NetworkDiffAnalyzerTest extends UnitTest with SharedTestObjects {

  test("nodeDiffs") {

    val before = newNetworkDoc(
      1,
      members = Seq(
        RawMember(MemberType.Node, 1, None),
        RawMember(MemberType.Node, 3, None),
      )
    )

    val after = newNetworkDoc(
      1,
      members = Seq(
        RawMember(MemberType.Node, 2, None),
        RawMember(MemberType.Node, 3, Some("connection"))
      )
    )

    val analyzer = new NetworkDiffAnalyzer(before, after)
    assertEqual(
      analyzer.nodeDiffs,
      IdDiffs(
        removed = Seq(1),
        added = Seq(2),
        updated = Seq(3)
      )
    )
  }

  test("wayDiffs") {

    val before = newNetworkDoc(
      1,
      members = Seq(
        RawMember(MemberType.Way, 1, None),
      )
    )

    val after = newNetworkDoc(
      1,
      members = Seq(
        RawMember(MemberType.Way, 2, None),
      )
    )

    val analyzer = new NetworkDiffAnalyzer(before, after)
    assertEqual(
      analyzer.wayDiffs,
      IdDiffs(
        removed = Seq(1),
        added = Seq(2)
      )
    )
  }

  test("relationDiffs") {

    val before = newNetworkDoc(
      1,
      members = Seq(
        RawMember(MemberType.Relation, 1, None),
        RawMember(MemberType.Relation, 3, None),
      )
    )

    val after = newNetworkDoc(
      1,
      members = Seq(
        RawMember(MemberType.Relation, 2, None),
        RawMember(MemberType.Relation, 3, Some("connection"))
      )
    )

    val analyzer = new NetworkDiffAnalyzer(before, after)
    assertEqual(
      analyzer.relationDiffs,
      IdDiffs(
        removed = Seq(1),
        added = Seq(2),
        updated = Seq(3)
      )
    )
  }
}
