package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.core.test.OverpassData

class NetworkUpdateNodeTest10 extends IntegrationTest {

  test("network update - node role 'connection' added") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkRelation(
        1,
        "network-name",
        Seq(
          newMember(MemberType.Node, 1001)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkRelation(
        1,
        "network-name",
        Seq(
          newMember(MemberType.Node, 1001, "connection")
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      assertBaseNetwork()
      assertNetwork()
      assertNetworkChange()
      assertNodeChange()
      assertChangeSetSummary()
    }
  }

  private def assertBaseNetwork(): Unit = {
    val baseNetworkDoc = findBaseNetworkById(1)
    baseNetworkDoc._id should equal(1)
  }

  private def assertNetwork(): Unit = {
    val networkDoc = findNetworkById(1)
    networkDoc._id should equal(1)
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("123:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = "network-name",
        changeType = ChangeType.Update,
        country = Some(Country.nl),
        routeType = RouteType.hiking,
        //  networkDataUpdate = None,
        //  nodes= IdDiffs.empty,
        //  ways = IdDiffs.empty,
        //  relations = IdDiffs.empty,
        nodeDiffs = RefDiffs(
          updated = Seq(
            Ref(1001, "01")
          )
        )
        //  routeDiffs = RefDiffs.empty,
        //  extraNodeDiffs = IdDiffs.empty,
        //  extraWayDiffs = IdDiffs.empty,
        //  extraRelationDiffs = IdDiffs.empty,
        //  happy = false,
        //  investigate = false,
        //  impact = false,

      )
    )
  }

  private def assertNodeChange(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1001"),
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        name = Some("01"),
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        roleConnectionChanges = Seq(
          RefBooleanChange(Ref(1, "network-name"), after = true)
        )
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        networkChanges = NetworkChanges(
          updates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              "network-name",
              nodeChanges = ChangeSetElementRefs(
                updated = Seq(newChangeSetElementRef(1001, "01"))
              )
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking)
        )
      )
    )
  }
}
