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
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.core.test.OverpassData

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

      process(ChangeAction.Create, dataAfter.rawRelationWithId(1))

      assert(watched.networks.contains(1))

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
    pending
    assertEqual(
      findNetworkChangeById("123:1:1"),
      newNetworkInfoChange(
        newChangeKey(elementId = 1),
        ChangeType.Create,
        Some(Country.nl),
        RouteType.hiking,
        1,
        "name",
        nodeDiffs = RefDiffs(added = Seq(Ref(1001, "01"), Ref(1002, "02"))),
        routeDiffs = RefDiffs(added = Seq(Ref(11, "01-02"))),
        extraWays = IdDiffs(
          added = Seq(
            102
          )
        ),
        happy = true,
        investigate = true
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
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
        happy = true,
        investigate = true
      )
    )
  }
}
