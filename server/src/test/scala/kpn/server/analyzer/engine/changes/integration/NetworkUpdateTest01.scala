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
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.TagDiff
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.diff.route.RouteNameDiff
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newBaseRouteChange
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetNetwork
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newNetworkChange
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.test.TestObjects.newRaw
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteData
import kpn.core.test.TestObjects.newRouteDiff
import kpn.core.test.TestObjects.newRouteNode
import kpn.core.test.TestObjects.newRouteNodeChange
import kpn.core.test.TestObjects.newRouteTags
import kpn.core.test.TestObjects.newWayUpdate

class NetworkUpdateTest01 extends IntegrationTest {

  test("FAIL NODE NAME CHANGE NOT DETECTED network update - node and route name changed") {

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

      processNode(ChangeAction.Modify, dataAfter.rawNodeWithId(1002))

      assert(watched.networks.contains(1))

      assertBaseNetwork()
      assertNetwork()
      assertNetworkChange()
      assertBaseRouteChange()
      assertRouteChange()
      database.nodeChanges.stringIds() should equal(Seq("1:1:1002")) // 1001 not changed
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

  private def assertBaseRouteChange(): Unit = {
    assertEqual(
      findBaseRouteChangeById("1:1:11"),
      newBaseRouteChange(
        "1:1:11",
        newChangeKey(elementId = 11),
        ChangeType.Update,
        routeDiff = newRouteDiff(
          nameDiff = Some(
            RouteNameDiff(
              before = Some("01-02"),
              after = Some("01-03")
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
        ),
        wayDiffs = Some(
          WayDiffsInfo(
            updated = Seq(
              newWayUpdate(
                101,
                MetaData(0, Timestamp(2015, 8, 11, 0, 0, 0), 1),
                MetaData(0, Timestamp(2015, 8, 11, 0, 0, 0), 1),
                //                Seq.empty,
                //                Seq.empty,
                //                Seq(
                //                  NodeUpdate(
                //                    newNodeWithName(1002, "02"),
                //                    newNodeWithName(1002, "03"),
                //                    None,
                //                    None
                //                  )
                //                )
              )
            )
          )
        )
      )
    )
  }

  private def assertRouteChange(): Unit = {
    assertEqual(
      findRouteChangeById("1:1:11"),
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-03",
        before = Some(
          newRouteData(
            relationId = 11,
            raw = newRaw(
              tags = newRouteTags("01-02")
            ),
            countries = Seq(Country.nl),
            routeTypes = Seq(RouteType.hiking),
            name = "01-02",
            networkNodes = Seq(
              newRouteNode(1001, "01"),
              newRouteNode(1002, "02")
            )
          )
        ),
        after = Some(
          newRouteData(
            relationId = 11,
            raw = newRaw(
              tags = newRouteTags("01-03")
            ),
            countries = Seq(Country.nl),
            routeTypes = Seq(RouteType.hiking),
            name = "01-03",
            networkNodes = Seq(
              newRouteNode(1001, "01"),
              newRouteNode(1002, "03")
            )
          )
        ),
        nodeChanges = Seq(
          newRouteNodeChange(1001),
          newRouteNodeChange(1002)
        ),
      )
    )
  }

  private def assertNodeChange1002(): Unit = {
    assertEqual(
      findNodeChangeById("1:1:1002"),
      newNodeChange(
        key = newChangeKey(elementId = 1002),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
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
      findChangeSetSummaryById("1:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        networkChanges = NetworkChanges(
          updates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              Some("name"),
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
        ),
        locationChanges = Seq(
          newLocationChanges(
            routeType = RouteType.hiking,
            locationNames = Seq("nl"),
            nodeChanges = ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1002, "03"),
              )
            )
          )
        )
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("1:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = Some("name"),
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
