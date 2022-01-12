package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.api.common.diff.RefDiffs
import kpn.core.doc.NetworkInfoDoc
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.context.ElementIds

class NetworkInfoUpdateNodeDiffsAnalyzerTest extends UnitTest with SharedTestObjects {

  test("removed network node") {

    val before = newNetworkInfoDoc(
      1,
      nodes = Seq(
        newNetworkInfoNodeDetail(
          1001,
          "01"
        )
      )
    )

    val after = newNetworkInfoDoc(
      1
    )

    analyze(before, after).shouldMatchTo(
      RefDiffs(
        removed = Seq(
          Ref(1001, "01")
        )
      )
    )
  }

  test("added network node") {

    val before = newNetworkInfoDoc(
      1
    )

    val after = newNetworkInfoDoc(
      1,
      nodes = Seq(
        newNetworkInfoNodeDetail(
          1001,
          "01"
        )
      )
    )

    analyze(before, after).shouldMatchTo(
      RefDiffs(
        added = Seq(
          Ref(1001, "01")
        )
      )
    )
  }

  test("updated network node") {

    val before = newNetworkInfoDoc(
      1,
      nodes = Seq(
        newNetworkInfoNodeDetail(
          1001,
          "01"
        )
      )
    )

    val after = newNetworkInfoDoc(
      1,
      nodes = Seq(
        newNetworkInfoNodeDetail(
          1001,
          "02"
        )
      )
    )

    analyze(before, after).shouldMatchTo(
      RefDiffs(
        updated = Seq(
          Ref(1001, "02")
        )
      )
    )
  }

  test("updated network node - other than in NetworkInfoNodeDetail") {

    val context = ChangeSetContext(
      ReplicationId(1),
      newChangeSet(),
      ElementIds(),
      changes = ChangeSetChanges(
        nodeChanges = Seq(
          newNodeChange(
            key = newChangeKey(elementId = 1001L),
            name = "01"
          )
        )
      )
    )

    val networkDoc = newNetworkInfoDoc(
      1,
      nodes = Seq(
        newNetworkInfoNodeDetail(
          1001,
          "01"
        )
      )
    )

    analyze(context, networkDoc, networkDoc).shouldMatchTo(
      RefDiffs(
        updated = Seq(
          Ref(1001, "01")
        )
      )
    )
  }

  private def analyze(before: NetworkInfoDoc, after: NetworkInfoDoc): RefDiffs = {
    val context: ChangeSetContext = ChangeSetContext(
      ReplicationId(1),
      newChangeSet(),
      ElementIds()
    )
    analyze(context, before, after)
  }

  private def analyze(context: ChangeSetContext, before: NetworkInfoDoc, after: NetworkInfoDoc): RefDiffs = {
    NetworkInfoUpdateNodeDiffsAnalyzer.analyze(context, before, after)
  }
}
