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

class NetworkInfoUpdateRouteDiffsAnalyzerTest extends UnitTest with SharedTestObjects {

  test("removed route") {

    val before = newNetworkInfoDoc(
      1,
      routes = Seq(
        newNetworkInfoRouteDetail(
          11,
          "01-02"
        )
      )
    )

    val after = newNetworkInfoDoc(
      1
    )

    analyze(before, after) should matchTo(
      RefDiffs(
        removed = Seq(
          Ref(11, "01-02")
        )
      )
    )
  }

  test("added route") {

    val before = newNetworkInfoDoc(
      1
    )

    val after = newNetworkInfoDoc(
      1,
      routes = Seq(
        newNetworkInfoRouteDetail(
          11,
          "01-02"
        )
      )
    )

    analyze(before, after) should matchTo(
      RefDiffs(
        added = Seq(
          Ref(11, "01-02")
        )
      )
    )
  }

  test("updated route") {

    val before = newNetworkInfoDoc(
      1,
      routes = Seq(
        newNetworkInfoRouteDetail(
          11,
          "01-02"
        )
      )
    )

    val after = newNetworkInfoDoc(
      1,
      routes = Seq(
        newNetworkInfoRouteDetail(
          11,
          "01-03"
        )
      )
    )

    analyze(before, after) should matchTo(
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

    val networkInfoDoc = newNetworkInfoDoc(
      1,
      routes = Seq(
        newNetworkInfoRouteDetail(
          11,
          "01-02"
        )
      )
    )

    analyze(context, networkInfoDoc, networkInfoDoc) should matchTo(
      RefDiffs(
        updated = Seq(
          Ref(11, "01-02")
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
    NetworkInfoUpdateRouteDiffsAnalyzer.analyze(context, before, after)
  }
}
