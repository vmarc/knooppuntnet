package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.ReplicationId
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.common.Ref
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.core.doc.NetworkInfoDoc
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.ElementIds

class NetworkInfoUpdateAnalyzerTest extends UnitTest with SharedTestObjects {

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

    val networkInfoChange = analyze(before, after)

    assert(networkInfoChange.impact)
    assert(networkInfoChange.investigate)
    assert(!networkInfoChange.happy)

    networkInfoChange.nodeDiffs.shouldMatchTo(
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

    val networkInfoChange = analyze(before, after)

    assert(networkInfoChange.happy)
    assert(networkInfoChange.impact)
    assert(!networkInfoChange.investigate)

    networkInfoChange.nodeDiffs.shouldMatchTo(
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

    val networkInfoChange = analyze(before, after)

    assert(!networkInfoChange.happy)
    assert(!networkInfoChange.investigate)
    assert(!networkInfoChange.impact)

    networkInfoChange.nodeDiffs.shouldMatchTo(
      RefDiffs(
        updated = Seq(
          Ref(1001, "02")
        )
      )
    )
  }

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

    val networkInfoChange = analyze(before, after)

    assert(!networkInfoChange.happy)
    assert(networkInfoChange.investigate)
    assert(networkInfoChange.impact)

    networkInfoChange.routeDiffs.shouldMatchTo(
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

    val networkInfoChange = analyze(before, after)

    assert(networkInfoChange.happy)
    assert(!networkInfoChange.investigate)
    assert(networkInfoChange.impact)

    networkInfoChange.routeDiffs.shouldMatchTo(
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

    val networkInfoChange = analyze(before, after)

    assert(!networkInfoChange.happy)
    assert(!networkInfoChange.investigate)
    assert(!networkInfoChange.impact)

    networkInfoChange.routeDiffs.shouldMatchTo(
      RefDiffs(
        updated = Seq(
          Ref(11, "01-03")
        )
      )
    )
  }

  test("network relation invalid member change - non network node removed") {

    val before = newNetworkInfoDoc(
      1,
      extraNodeIds = Seq(1001)
    )

    val after = newNetworkInfoDoc(
      1
    )

    val networkInfoChange = analyze(before, after)

    assert(networkInfoChange.happy)
    assert(!networkInfoChange.investigate)
    assert(networkInfoChange.impact)

    networkInfoChange.extraNodeDiffs.shouldMatchTo(
      IdDiffs(
        removed = Seq(
          1001
        )
      )
    )
  }

  test("network relation invalid member change - non network node added") {

    val before = newNetworkInfoDoc(
      1
    )

    val after = newNetworkInfoDoc(
      1,
      extraNodeIds = Seq(1001)
    )

    val networkInfoChange = analyze(before, after)

    assert(!networkInfoChange.happy)
    assert(networkInfoChange.investigate)
    assert(networkInfoChange.impact)

    networkInfoChange.extraNodeDiffs.shouldMatchTo(
      IdDiffs(
        added = Seq(
          1001
        )
      )
    )
  }

  test("network relation invalid member change - way removed") {

    val before = newNetworkInfoDoc(
      1,
      extraWayIds = Seq(101)
    )

    val after = newNetworkInfoDoc(
      1
    )

    val networkInfoChange = analyze(before, after)

    assert(networkInfoChange.happy)
    assert(!networkInfoChange.investigate)
    assert(networkInfoChange.impact)

    networkInfoChange.extraWayDiffs.shouldMatchTo(
      IdDiffs(
        removed = Seq(
          101
        )
      )
    )
  }

  test("network relation invalid member change - way added") {

    val before = newNetworkInfoDoc(
      1
    )

    val after = newNetworkInfoDoc(
      1,
      extraWayIds = Seq(101)
    )

    val networkInfoChange = analyze(before, after)

    assert(!networkInfoChange.happy)
    assert(networkInfoChange.investigate)
    assert(networkInfoChange.impact)

    networkInfoChange.extraWayDiffs.shouldMatchTo(
      IdDiffs(
        added = Seq(
          101
        )
      )
    )
  }

  test("network relation invalid member change - non route relation removed") {

    val before = newNetworkInfoDoc(
      1,
      extraRelationIds = Seq(2)
    )

    val after = newNetworkInfoDoc(
      1
    )

    val networkInfoChange = analyze(before, after)

    assert(networkInfoChange.happy)
    assert(!networkInfoChange.investigate)
    assert(networkInfoChange.impact)

    networkInfoChange.extraRelationDiffs.shouldMatchTo(
      IdDiffs(
        removed = Seq(
          2
        )
      )
    )
  }

  test("network relation invalid member change - non route relation added") {

    val before = newNetworkInfoDoc(
      1
    )

    val after = newNetworkInfoDoc(
      1,
      extraRelationIds = Seq(2)
    )

    val networkInfoChange = analyze(before, after)

    assert(!networkInfoChange.happy)
    assert(networkInfoChange.investigate)
    assert(networkInfoChange.impact)

    networkInfoChange.extraRelationDiffs.shouldMatchTo(
      IdDiffs(
        added = Seq(
          2
        )
      )
    )
  }

  private def analyze(before: NetworkInfoDoc, after: NetworkInfoDoc): NetworkInfoChange = {
    val networkId: Long = 1
    val context: ChangeSetContext = ChangeSetContext(
      ReplicationId(1),
      newChangeSet(),
      ElementIds()
    )
    new NetworkInfoUpdateAnalyzer(context, before, after, networkId).analyze()
  }
}
