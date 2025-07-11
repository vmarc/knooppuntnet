package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.data.MetaData
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetNetwork
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newNetworkChange

class NetworkCreateTest04 extends IntegrationTest {

  test("network create - investigate flag is set when issue in added network") {

    val dataBefore = OverpassData.empty
    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .way(102)
      .route(11, "01-02", Seq(newMember(MemberType.Way, 101)))
      .networkRelation(
        1,
        "name",
        Seq(
          newMember(MemberType.Relation, 11),
          newMember(MemberType.Way, 102)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(
        ChangeAction.Create,
        dataAfter.rawNodeWithId(1001),
        dataAfter.rawNodeWithId(1002),
        dataAfter.rawRelationWithId(11),
        dataAfter.rawRelationWithId(1)
      )

      watched.networks.ids should contain(1)

      assertBaseNetwork()
      assertNetwork()
      assertNetworkChange()
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
        networkName = "name",
        changeType = ChangeType.Create,
        country = Some(Country.nl),
        routeType = RouteType.hiking,
        networkDataUpdate = Some(
          NetworkDataUpdate(
            before = None,
            after = Some(
              NetworkData(
                MetaData(
                  version = 0,
                  timestamp = Timestamp(2015, 8, 11),
                  changeSetId = 1
                ),
                name = "name"
              )
            )
          )
        ),
        nodes = IdDiffs.empty,
        ways = IdDiffs(added = Seq(102)),
        relations = IdDiffs(added = Seq(11)),
        nodeDiffs = RefDiffs(added = Seq(Ref(1001, "01"), Ref(1002, "02"))),
        routeDiffs = RefDiffs(added = Seq(Ref(11, "01-02"))),
        extraNodeDiffs = IdDiffs.empty,
        extraWayDiffs = IdDiffs(added = Seq(102)),
        extraRelationDiffs = IdDiffs.empty,
        happy = true,
        investigate = true,
        impact = true,
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        networkChanges = NetworkChanges(
          creates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              "name",
              routeChanges = ChangeSetElementRefs(
                added = Seq(
                  ChangeSetElementRef(11, "01-02", happy = true, investigate = false)
                )
              ),
              nodeChanges = ChangeSetElementRefs(
                added = Seq(
                  ChangeSetElementRef(1001, "01", happy = true, investigate = false),
                  ChangeSetElementRef(1002, "02", happy = true, investigate = false)
                )
              ),
              happy = true,
              investigate = true
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking, happy = true, investigate = true)
        ),
        locationChanges = Seq(
          newLocationChanges(
            routeType = RouteType.hiking,
            locationNames = Seq("nl"),
            nodeChanges = ChangeSetElementRefs(
              added = Seq(
                newChangeSetElementRef(1001, "01", happy = true),
                newChangeSetElementRef(1002, "02", happy = true),
              )
            ),
            happy = true,
          )
        ),
        happy = true,
        investigate = true
      )
    )
  }
}
