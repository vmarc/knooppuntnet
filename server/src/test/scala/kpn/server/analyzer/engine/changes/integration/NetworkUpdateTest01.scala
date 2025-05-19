package kpn.server.analyzer.engine.changes.integration

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
import kpn.api.common.diff.NodeUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.TagDiff
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.WayUpdate
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.diff.route.RouteNameDiff
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData

class NetworkUpdateTest01 extends IntegrationTest {

  test("network update - node and route name changed") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
      .networkRelation(
        1,
        "name",
        Seq(
          newMember(MemberType.Relation, 11)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "03") // <-- node name changed
      .way(101, 1001, 1002)
      .route(
        11,
        "01-03", // <-- route name changed
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
      .networkRelation(
        1,
        "name",
        Seq(
          newMember(MemberType.Relation, 11)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawNodeWithId(1002))

      watched.networks.ids should contain(1)

      assertBaseNetwork()
      assertNetwork()
      assertNetworkChange()
      assertRouteChange()
      database.nodeChanges.stringIds() should equal(Seq("123:1:1002")) // 1001 not changed
      assertNodeChange1002()
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

  private def assertRouteChange(): Unit = {
    assertEqual(
      findRouteChangeById("123:1:11"),
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-03",
        before = Some(
          newRouteData(
            relationId = 11,
            meta = newMetaData(changeSetId = 1),
            countries = Seq(Country.nl),
            routeTypes = Seq(RouteType.hiking),
            name = "01-02",
            networkNodes = Seq(
              newRouteNode(1001, "01"),
              newRouteNode(1002, "02")
            ),
            tags = newRouteTags("01-02")
          )
        ),
        after = Some(
          newRouteData(
            relationId = 11,
            meta = newMetaData(changeSetId = 1),
            countries = Seq(Country.nl),
            routeTypes = Seq(RouteType.hiking),
            name = "01-03",
            networkNodes = Seq(
              newRouteNode(1001, "01"),
              newRouteNode(1002, "03")
            ),
            tags = newRouteTags("01-03")
          )
        ),
        updatedWays = Seq(
          WayUpdate(
            101,
            MetaData(0, Timestamp(2015, 8, 11, 0, 0, 0), 0),
            MetaData(0, Timestamp(2015, 8, 11, 0, 0, 0), 0),
            Seq.empty,
            Seq.empty,
            Seq(
              NodeUpdate(
                newNodeWithName(1002, "02"),
                newNodeWithName(1002, "03"),
                None,
                None
              )
            )
          )
        ),
        diffs = RouteDiff(
          nameDiff = Some(
            RouteNameDiff(
              before = "01-02",
              after = "01-03"
            )
          ),
          tagDiffs = Some(
            TagDiffs(
              mainTags = Seq(
                TagDiff.update("ref", "01-02", "01-03"),
                TagDiff.same("network", "rwn"),
                TagDiff.same("type", "route"),
                TagDiff.same("route", "foot"),
                TagDiff.same("network:type", "node_network")
              )
            )
          )
        )
      )
    )
  }

  private def assertNodeChange1002(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1002"),
      newNodeChange(
        key = newChangeKey(elementId = 1002),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        name = Some("03"),
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        tagDiffs = Some(
          TagDiffs(
            mainTags = Seq(
              TagDiff.update("rwn_ref", "02", "03"),
              TagDiff.same("network:type", "node_network")
            )
          )
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
              "name",
              routeChanges = ChangeSetElementRefs(
                updated = Seq(newChangeSetElementRef(11, "01-03"))
              ),
              nodeChanges = ChangeSetElementRefs(
                updated = Seq(newChangeSetElementRef(1002, "03"))
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

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("123:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = "name",
        changeType = ChangeType.Update,
        country = Some(Country.nl),
        routeType = RouteType.hiking,
        //  networkDataUpdate = None,
        //  nodes= IdDiffs.empty,
        //  ways = IdDiffs.empty,
        //  relations = IdDiffs.empty,
        nodeDiffs = RefDiffs(
          updated = Seq(
            Ref(1002, "03")
          )
        ),
        routeDiffs = RefDiffs(
          updated = Seq(
            Ref(11, "01-03")
          )
        )
        //  extraNodeDiffs = IdDiffs.empty,
        //  extraWayDiffs = IdDiffs.empty,
        //  extraRelationDiffs = IdDiffs.empty,
        //  happy = false,
        //  investigate = false,
        //  impact = false,
      )
    )
  }
}
