package kpn.server.analyzer.engine.changes.network.main

import kpn.api.common.ReplicationId
import kpn.api.common.common.Ref
import kpn.api.common.diff.RefDiffs
import kpn.core.doc.NetworkDoc
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSet
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newNetworkInfoNodeDetail
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.context.ElementIds

class NetworkUpdateNodeDiffsAnalyzerTest extends UnitTest {

  test("removed network node") {

    val before = newNetworkDoc(
      1,
      nodes = Seq(
        newNetworkInfoNodeDetail(
          1001,
          "01"
        )
      )
    )

    val after = newNetworkDoc(
      1
    )

    assertEqual(
      analyze(before, after),
      RefDiffs(
        removed = Seq(
          Ref(1001, "01")
        )
      )
    )
  }

  test("added network node") {

    val before = newNetworkDoc(
      1
    )

    val after = newNetworkDoc(
      1,
      nodes = Seq(
        newNetworkInfoNodeDetail(
          1001,
          "01"
        )
      )
    )

    assertEqual(
      analyze(before, after),
      RefDiffs(
        added = Seq(
          Ref(1001, "01")
        )
      )
    )
  }

  test("updated network node") {

    val before = newNetworkDoc(
      1,
      nodes = Seq(
        newNetworkInfoNodeDetail(
          1001,
          "01"
        )
      )
    )

    val after = newNetworkDoc(
      1,
      nodes = Seq(
        newNetworkInfoNodeDetail(
          1001,
          "02"
        )
      )
    )

    assertEqual(
      analyze(before, after),
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
            name = Some("01")
          )
        )
      )
    )

    val networkDoc = newNetworkDoc(
      1,
      nodes = Seq(
        newNetworkInfoNodeDetail(
          1001,
          "01"
        )
      )
    )

    assertEqual(
      analyze(context, networkDoc, networkDoc),
      RefDiffs(
        updated = Seq(
          Ref(1001, "01")
        )
      )
    )
  }

  private def analyze(before: NetworkDoc, after: NetworkDoc): RefDiffs = {
    val context: ChangeSetContext = ChangeSetContext(
      ReplicationId(1),
      newChangeSet(),
      ElementIds()
    )
    analyze(context, before, after)
  }

  private def analyze(context: ChangeSetContext, before: NetworkDoc, after: NetworkDoc): RefDiffs = {
    NetworkUpdateNodeDiffsAnalyzer.analyze(context, before, after)
  }
}
