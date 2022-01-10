package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.NodeUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.WayUpdate
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.diff.route.RouteNameDiff
import kpn.api.custom.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
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
          newMember("way", 101)
        )
      )
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("relation", 11)
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
          newMember("way", 101)
        )
      )
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("relation", 11)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawNodeWithId(1002))

      assert(watched.networks.contains(1))

      assertNetwork()
      assertNetworkInfo()
      assertNetworkInfoChange()
      assertRouteChange()
      database.nodeChanges.stringIds() should equal(Seq("123:1:1002")) // 1001 not changed
      assertNodeChange1002()
      assertChangeSetSummary()
    }
  }

  private def assertNetwork(): Unit = {
    val networkDoc = findNetworkById(1)
    networkDoc._id should equal(1)
  }

  private def assertNetworkInfo(): Unit = {
    val networkInfoDoc = findNetworkInfoById(1)
    networkInfoDoc._id should equal(1)
  }

  private def assertRouteChange(): Unit = {
    findRouteChangeById("123:1:11") should matchTo(
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-03",
        before = Some(
          newRouteData(
            Some(Country.nl),
            NetworkType.hiking,
            relation = newRawRelation(
              11,
              members = Seq(RawMember("way", 101, None)),
              tags = newRouteTags("01-02")
            ),
            name = "01-02",
            networkNodes = Seq(
              newRawNodeWithName(1001, "01"),
              newRawNodeWithName(1002, "02")
            ),
            nodes = Seq(
              newRawNodeWithName(1001, "01"),
              newRawNodeWithName(1002, "02")
            ),
            ways = Seq(
              newRawWay(
                101,
                nodeIds = Seq(1001, 1002),
                tags = Tags.from("highway" -> "unclassified")
              )
            )
          )
        ),
        after = Some(
          newRouteData(
            Some(Country.nl),
            NetworkType.hiking,
            relation = newRawRelation(
              11,
              members = Seq(
                RawMember("way", 101, None)
              ),
              tags = newRouteTags("01-03")
            ),
            name = "01-03",
            networkNodes = Seq(
              newRawNodeWithName(1001, "01"),
              newRawNodeWithName(1002, "03")
            ),
            nodes = Seq(
              newRawNodeWithName(1001, "01"),
              newRawNodeWithName(1002, "03")
            ),
            ways = Seq(
              newRawWay(
                101,
                nodeIds = Seq(1001, 1002),
                tags = Tags.from("highway" -> "unclassified")
              )
            )
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
                newRawNodeWithName(1002, "02"),
                newRawNodeWithName(1002, "03"),
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
                TagDetail(
                  action = TagDetailType.Update,
                  key = "note",
                  valueBefore = Some("01-02"),
                  valueAfter = Some("01-03"),
                ),
                TagDetail(
                  action = TagDetailType.Same,
                  key = "network",
                  valueBefore = Some("rwn"),
                  valueAfter = Some("rwn")
                ),
                TagDetail(
                  action = TagDetailType.Same,
                  key = "type",
                  valueBefore = Some("route"),
                  valueAfter = Some("route"),
                ),
                TagDetail(
                  action = TagDetailType.Same,
                  key = "route",
                  valueBefore = Some("foot"),
                  valueAfter = Some("foot")
                ),
                TagDetail(
                  action = TagDetailType.Same,
                  key = "network:type",
                  valueBefore = Some("node_network"),
                  valueAfter = Some("node_network")
                )
              )
            )
          )
        ),
        impactedNodeIds = Seq(1001, 1002)
      )
    )
  }

  private def assertNodeChange1002(): Unit = {
    findNodeChangeById("123:1:1002") should matchTo(
      newNodeChange(
        key = newChangeKey(elementId = 1002),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        name = "03",
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        tagDiffs = Some(
          TagDiffs(
            mainTags = Seq(
              TagDetail(
                TagDetailType.Update,
                "rwn_ref",
                Some("02"),
                Some("03")
              ),
              TagDetail(
                TagDetailType.Same,
                "network:type",
                Some("node_network"),
                Some("node_network")
              )
            )
          )
        )
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    findChangeSetSummaryById("123:1") should matchTo(
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        networkChanges = NetworkChanges(
          updates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              NetworkType.hiking,
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
        orphanRouteChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(11, "01-03")
              )
            )
          )
        ),
        orphanNodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1002, "03")
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

  private def assertNetworkInfoChange(): Unit = {
    findNetworkInfoChangeById("123:1:1") should matchTo(
      newNetworkInfoChange(
        newChangeKey(elementId = 1),
        ChangeType.Update,
        Some(Country.nl),
        NetworkType.hiking,
        1,
        "name",
        networkDataUpdate = None,
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
      )
    )
  }
}
