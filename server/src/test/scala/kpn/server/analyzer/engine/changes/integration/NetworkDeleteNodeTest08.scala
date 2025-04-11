package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Change
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.test.OverpassData

class NetworkDeleteNodeTest08 extends IntegrationTest {

  test("network delete - node deleted") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkRelation(
        1,
        "network1",
        Seq(
          newMember(MemberType.Node, 1001)
        )
      )

    val dataAfter = OverpassData.empty

    testIntegration(dataBefore, dataAfter) {

      process(
        Seq(
          Change(
            ChangeAction.Delete,
            Seq(
              newRawNode(1001),
              newRawRelation(1)
            )
          )
        )
      )

      watched.networks.ids should not contain (1)
      watched.nodes.ids should not contain (1001)

      assertBaseNode()
      assertNode()
      assertBaseNetwork()
      assertNetwork()
      assertNetworkChange()
      assertNodeChange()
      assertChangeSetSummary()
    }
  }

  private def assertBaseNode(): Unit = {
    assertEqual(
      findBaseNodeById(1001),
      newBaseNodeDoc(
        1001,
        active = false,
        name = Some("01"),
        names = Seq(
          newNodeName(RouteType.hiking, RouteScope.regional, "01")
        ),
        latitude = "0",
        longitude = "0",
        tags = newNodeTags("01"),
        country = Some(Country.nl),
      )
    )
  }

  private def assertNode(): Unit = {
    assertEqual(
      findNodeById(1001).copy(stamp = None),
      newNodeDoc(
        1001,
        labels = Seq(
          Label.routeType(RouteType.hiking)
          // not active
        ),
        country = Some(Country.nl),
        name = Some("01"),
        names = Seq(
          newNodeName(RouteType.hiking, RouteScope.regional, "01")
        ),
        tags = newNodeTags("01")
      )
    )
  }

  private def assertBaseNetwork(): Unit = {
    val networkDoc = findBaseNetworkById(1)
    networkDoc._id should equal(1)
  }

  private def assertNetwork(): Unit = {
    assertEqual(
      findNetworkById(1),
      newNetworkDoc(
        1,
        active = false, // <--- !!!
        country = Some(Country.nl),
        newNetworkSummary(
          name = "network1",
          routeType = RouteType.hiking,
        ),
        newNetworkDetail(
          lastUpdated = defaultTimestamp,
          relationLastUpdated = defaultTimestamp,
          tags = newNetworkTags("network1")
        )
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("123:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = "network1",
        changeType = ChangeType.Delete,
        country = Some(Country.nl),
        routeType = RouteType.hiking,
        nodes = IdDiffs(
          removed = Seq(
            1001
          )
        ),
        nodeDiffs = RefDiffs(
          removed = Seq(
            Ref(1001, "01")
          )
        ),
        investigate = true,
        impact = true,
      )
    )
  }

  private def assertNodeChange(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1001"),
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Delete,
        subsets = Seq(Subset.nlHiking),
        name = Some("01"),
        before = Some(
          newMetaData()
        ),
        removedFromNetwork = Seq(
          Ref(1, "network1")
        ),
        facts = Seq(Fact.Deleted),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        networkChanges = NetworkChanges(
          deletes = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              "network1",
              nodeChanges = ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(1001, "01", investigate = true)
                )
              ),
              investigate = true
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking, investigate = true)
        ),
        investigate = true
      )
    )
  }
}
