package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.NodeName
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class NetworkUpdateRouteTest03 extends AbstractIntegrationTest {

  test("network update - route no longer part of the network after deletion") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkNode(1003, "03")
      .way(101, 1001, 1002)
      .way(102, 1002, 1003)
      .route(11, "01-02", Seq(newMember("way", 101)))
      .route(12, "02-03", Seq(newMember("way", 102)))
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("relation", 11),
          newMember("relation", 12)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember("way", 101)))
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("relation", 11)
        )
      )

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Delete, newRawRelation(12))

      //  tc.analysisContext.watched.networks.get(1) match {
      //    case None => fail()
      //    case Some(elementIds) =>
      //
      //      elementIds.relationIds should contain(11)
      //      elementIds.relationIds should not contain 12
      //
      //      elementIds.wayIds should contain(101)
      //      elementIds.wayIds should not contain 102
      //
      //      elementIds.nodeIds should contain(1001)
      //      elementIds.nodeIds should contain(1002)
      //      elementIds.nodeIds should not contain 1003
      //  }

      assertNetwork(tc)
      assertNetworkInfo(tc)
      assertRoute1(tc)
      assertRoute2(tc)
      assertNode1003(tc)
      assertNetworkInfoChange(tc)
      assertRouteChange(tc)
      assertNodeChange1002(tc)
      assertNodeChange1003(tc)
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
    networkInfoDoc.routes.map(_.id) should equal(Seq(11L))
  }

  private def assertRoute1(tc: IntegrationTestContext): Unit = {
    val routeInfo1 = tc.findRouteById(11)
    routeInfo1.active should equal(true)
  }

  private def assertRoute2(tc: IntegrationTestContext): Unit = {
    val routeInfo2 = tc.findRouteById(12)
    routeInfo2.active should equal(false)
  }

  private def assertNode1003(tc: IntegrationTestContext): Unit = {
    tc.findNodeById(1003) should matchTo(
      newNodeDoc(
        1003,
        labels = Seq(
          "facts",
          "fact-Deleted",
          "network-type-hiking"
        ),
        active = false,
        country = Some(Country.nl),
        name = "03",
        names = Seq(
          NodeName(
            NetworkType.hiking,
            NetworkScope.regional,
            "03", None,
            proposed = false
          )
        ),
        tags = newNodeTags("03"),
        facts = Seq(Fact.Deleted)
      )
    )
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
                removed = Seq(newChangeSetElementRef(12, "02-03", investigate = true))
              ),
              nodeChanges = ChangeSetElementRefs(
                removed = Seq(newChangeSetElementRef(1003, "03", investigate = true)),
                updated = Seq(newChangeSetElementRef(1002, "02"))
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
          removed = Seq(
            Ref(1003, "03")
          ),
          updated = Seq(
            Ref(1002, "02")
          )
        ),
        routes = RefDiffs(
          removed = Seq(
            Ref(12, "02-03")
          )
        ),
        investigate = true
      )
    )
  }

  private def assertRouteChange(tc: IntegrationTestContext): Unit = {
    tc.findRouteChangeById("123:1:11") should matchTo(
      newRouteChange(
        newChangeKey(elementId = 12),
        ChangeType.Delete,
        "02-03",
        removedFromNetwork = Seq(Ref(1, "name")),
        before = Some(
          newRouteData(
            Some(Country.nl),
            NetworkType.hiking,
            relation = newRawRelation(
              12,
              members = Seq(RawMember("way", 102, None)),
              tags = newRouteTags("02-03")
            ),
            name = "02-03",
            networkNodes = Seq(
              newRawNodeWithName(1002, "02"),
              newRawNodeWithName(1003, "03")
            ),
            nodes = Seq(
              newRawNodeWithName(1002, "02"),
              newRawNodeWithName(1003, "03")
            ),
            ways = Seq(
              newRawWay(
                102,
                nodeIds = Seq(1002, 1003),
                tags = Tags.from("highway" -> "unclassified")
              )
            )
          )
        ),
        facts = Seq(Fact.Deleted),
        investigate = true,
        impact = true,
        locationInvestigate = true,
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
        removedFromRoute = Seq(Ref(12, "02-03")),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1003(tc: IntegrationTestContext): Unit = {
    tc.findNodeChangeById("123:1:1003") should matchTo(
      newNodeChange(
        key = newChangeKey(elementId = 1003),
        changeType = ChangeType.Delete,
        subsets = Seq(Subset.nlHiking),
        name = "03",
        before = Some(
          newMetaData()
        ),
        after = None,
        removedFromRoute = Seq(Ref(12, "02-03")),
        removedFromNetwork = Seq(Ref(1, "name")),
        facts = Seq(Fact.Deleted),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
      )
    )
  }
}
