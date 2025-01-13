package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.api.common.diff.RefDiffs
import kpn.core.doc.NetworkDoc
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.context.ElementIds

class NetworkInfoUpdateRouteDiffsAnalyzerTest extends UnitTest with SharedTestObjects {

  test("removed route") {

    val before = newNetworkDoc(
      1,
      routes = Seq(
        newNetworkInfoRouteDetail(
          11,
          "01-02"
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
          Ref(11, "01-02")
        )
      )
    )
  }

  test("added route") {

    val before = newNetworkDoc(
      1
    )

    val after = newNetworkDoc(
      1,
      routes = Seq(
        newNetworkInfoRouteDetail(
          11,
          "01-02"
        )
      )
    )

    assertEqual(
      analyze(before, after),
      RefDiffs(
        added = Seq(
          Ref(11, "01-02")
        )
      )
    )
  }

  test("updated route") {

    val before = newNetworkDoc(
      1,
      routes = Seq(
        newNetworkInfoRouteDetail(
          11,
          "01-02"
        )
      )
    )

    val after = newNetworkDoc(
      1,
      routes = Seq(
        newNetworkInfoRouteDetail(
          11,
          "01-03"
        )
      )
    )

    assertEqual(
      analyze(before, after),
      RefDiffs(
        updated = Seq(
          Ref(11, "01-03")
        )
      )
    )
  }

  test("updated route - other than in NetworkInfoRouteDetail") {

    val context = ChangeSetContext(
      ReplicationId(1),
      newChangeSet(),
      ElementIds(),
      changes = ChangeSetChanges(
        routeChanges = Seq(
          newRouteChange(
            key = newChangeKey(elementId = 11L),
            name = "01-02"
          )
        )
      )
    )

    val networkInfoDoc = newNetworkDoc(
      1,
      routes = Seq(
        newNetworkInfoRouteDetail(
          11,
          "01-02"
        )
      )
    )

    assertEqual(
      analyze(context, networkInfoDoc, networkInfoDoc),
      RefDiffs(
        updated = Seq(
          Ref(11, "01-02")
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
    NetworkInfoUpdateRouteDiffsAnalyzer.analyze(context, before, after)
  }
}
