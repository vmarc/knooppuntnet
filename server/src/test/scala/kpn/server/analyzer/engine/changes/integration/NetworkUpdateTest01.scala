package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.NodeUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.WayUpdate
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.diff.route.RouteNameDiff
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class NetworkUpdateTest01 extends AbstractIntegrationTest {

  test("network update - updated network is saved to the database and watched elements are updated in AnalysisData") {

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
      .networkNode(1002, "03")
      .way(101, 1001, 1002)
      .route(
        11,
        "01-03",
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

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Modify, dataAfter.rawNodeWithId(1002))

      assert(tc.analysisContext.data.networks.watched.contains(1))

      assertNetwork(tc)
      assertNetworkInfo(tc)
      //assertChangeSetSummary(tc)
      assertNetworkInfoChange(tc)
      assertRouteChange(tc)
      assertNodeChange(tc)
    }
  }

  private def assertNetwork(tc: IntegrationTestContext): Unit = {
    val networkDoc = tc.findNetworkById(1)
    networkDoc._id should equal(1)
  }

  private def assertNetworkInfo(tc: IntegrationTestContext): Unit = {
    val networkInfoDoc = tc.findNetworkInfoById(1)
    networkInfoDoc._id should equal(1)
  }

  private def assertChangeSetSummary(tc: IntegrationTestContext): Unit = {
    tc.findChangeSetSummaryById("123:1") should matchTo(
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
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking)
        )
      )
    )
  }

  private def assertNetworkInfoChange(tc: IntegrationTestContext): Unit = {
    tc.findNetworkInfoChangeById("123:1:1") should matchTo(
      newNetworkInfoChange(
        newChangeKey(elementId = 1),
        ChangeType.Update,
        Some(Country.nl),
        NetworkType.hiking,
        1,
        "name",
        networkDataUpdate = Some(
          NetworkDataUpdate(
            newNetworkData(name = "name"),
            newNetworkData(name = "name")
          )
        ),
        networkNodes = RefDiffs(
          updated = Seq(
            Ref(1002, "03")
          )
        ),
        routes = RefDiffs(
          updated = Seq(
            Ref(11, "01-03")
          )
        )
      )
    )
  }

  private def assertRouteChange(tc: IntegrationTestContext): Unit = {
    tc.findRouteChangeById("123:1:11") should matchTo(
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
        )
      )
    )
  }

  private def assertNodeChange(tc: IntegrationTestContext): Unit = {
    tc.findNodeChangeById("123:1:1001") should matchTo(
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
}
