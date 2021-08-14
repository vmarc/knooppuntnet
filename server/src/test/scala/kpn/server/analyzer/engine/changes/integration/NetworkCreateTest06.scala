package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class NetworkCreateTest06 extends AbstractIntegrationTest {

  test("network create - with new route") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02", Tags.from("tag" -> "after"))
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember("way", 101)))
      .networkRelation(1, "name", Seq(newMember("relation", 11)))

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Create, dataAfter.rawRelationWithId(1))

      assert(tc.analysisContext.watched.networks.contains(1))

      assertNetwork(tc)
      assertNetworkInfo(tc)
      assertNetworkInfoChange(tc)
      assertRouteChange(tc)
      assertNodeChange1001(tc)
      assertNodeChange1002(tc)
      assertChangeSetSummary(tc)
    }
  }

  private def assertNetwork(tc: IntegrationTestContext): Unit = {
    val networkDoc = tc.findNetworkById(1)
    networkDoc._id should equal(1)
  }

  private def assertNetworkInfo(tc: IntegrationTestContext): Unit = {
    val networkInfoDoc = tc.findNetworkInfoById(1)
    networkInfoDoc._id should equal(1)
    //  networkInfoDoc.detail.networkFacts should matchTo(
    //    NetworkFacts(
    //      networkExtraMemberWay = Some(
    //        Seq(
    //          NetworkExtraMemberWay(102)
    //        )
    //      )
    //    )
    //  )
  }

  private def assertChangeSetSummary(tc: IntegrationTestContext): Unit = {
    tc.findChangeSetSummaryById("123:1") should matchTo(
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        networkChanges = NetworkChanges(
          creates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              NetworkType.hiking,
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
              happy = true
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking, happy = true)
        ),
        happy = true
      )
    )
  }

  private def assertNetworkInfoChange(tc: IntegrationTestContext): Unit = {
    tc.findNetworkInfoChangeById("123:1:1") should matchTo(
      newNetworkInfoChange(
        newChangeKey(elementId = 1),
        ChangeType.Create,
        Some(Country.nl),
        NetworkType.hiking,
        1,
        "name",
        networkNodes = RefDiffs(added = Seq(Ref(1001, "01"), Ref(1002, "02"))),
        routes = RefDiffs(added = Seq(Ref(11, "01-02"))),
        happy = true
      )
    )
  }

  private def assertRouteChange(tc: IntegrationTestContext): Unit = {
    tc.findRouteChangeById("123:1:11") should matchTo(
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Create,
        "01-02",
        addedToNetwork = Seq(Ref(1, "name")),
        before = None,
        after = Some(
          newRouteData(
            Some(Country.nl),
            NetworkType.hiking,
            relation = newRawRelation(
              11,
              members = Seq(
                RawMember("way", 101, None)
              ),
              tags = newRouteTags("01-02")
            ),
            name = "01-02",
            networkNodes = Seq(
              newRawNodeWithName(1001, "01"),
              newRawNodeWithName(1002, "02", Tags.from("tag" -> "after"))
            ),
            nodes = Seq(
              newRawNodeWithName(1001, "01"),
              newRawNodeWithName(1002, "02", Tags.from("tag" -> "after"))
            ),
            ways = Seq(
              newRawWay(
                101,
                nodeIds = List(1001, 1002),
                tags = Tags.from("highway" -> "unclassified")
              )
            )
          )
        ),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1001(tc: IntegrationTestContext): Unit = {
    tc.findNodeChangeById("123:1:1001") should matchTo(
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        name = "01",
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        addedToRoute = Seq(
          Ref(11, "01-02")
        ),
        addedToNetwork = Seq(
          Ref(1, "name")
        ),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1002(tc: IntegrationTestContext): Unit = {
    tc.findNodeChangeById("123:1:1002") should matchTo(
      newNodeChange(
        key = newChangeKey(elementId = 1002),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        name = "02",
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        tagDiffs = Some(
          TagDiffs(
            mainTags = Seq(
              TagDetail(TagDetailType.Same, "rwn_ref", Some("02"), Some("02")),
              TagDetail(TagDetailType.Same, "network:type", Some("node_network"), Some("node_network"))
            ),
            extraTags = Seq(
              TagDetail(TagDetailType.Add, "tag", None, Some("after"))
            )
          )
        ),
        addedToRoute = Seq(
          Ref(11, "01-02")
        ),
        addedToNetwork = Seq(
          Ref(1, "name")
        ),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }
}
