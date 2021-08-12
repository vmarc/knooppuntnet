package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.NetworkFacts
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.IdDiffs
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.mongo.doc.NetworkDoc
import kpn.core.mongo.doc.NetworkRelationMember
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class NetworkCreateTest01 extends AbstractIntegrationTest with SharedTestObjects {

  test("network create - added to watched list in memory and added to repository") {

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

    val dataAfter = OverpassData()
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

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Create, dataAfter.rawRelationWithId(1))

      assert(tc.analysisContext.data.networks.watched.contains(1))

      assertNetworkDoc(tc)

      // val networkInfoDoc = tc.findNetworkInfoById(1)
      // networkInfoDoc._id should equal(1)
      // networkInfoDoc.active should equal(true)
      // TODO networkInfoDoc.nodeRefs should equal(Seq(1001L,1002L))
      // TODO networkInfoDoc.routeRefs should equal(Seq(11L))
      // for remaining network structure - see NetworkAnalyzerTest

      assertChangeSetSummary(tc)
      assertNetworkChange(tc)
      assertRouteChange(tc)
      assertNodeChange1001(tc)
      assertNodeChange1002(tc)
    }
  }

  private def assertNetworkDoc(tc: IntegrationTestContext): Unit = {
    tc.findNetworkById(1) should matchTo(
      NetworkDoc(
        1,
        active = true,
        relationLastUpdated = defaultTimestamp,
        nodeMembers = Seq.empty,
        wayMembers = Seq.empty,
        relationMembers = Seq(
          NetworkRelationMember(11L, None)
        ),
        networkFacts = NetworkFacts(),
        tags = Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rwn",
          "name" -> "name",
        )
      )
    )
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

  private def assertNetworkChange(tc: IntegrationTestContext): Unit = {
    tc.findNewNetworkChangeById("123:1:1") should matchTo(
      newNewNetworkChange(
        newChangeKey(elementId = 1),
        networkName = "name",
        changeType = ChangeType.Create,
        relations = IdDiffs(added = Seq(11))
      )
    )
  }

  private def assertRouteChange(tc: IntegrationTestContext): Unit = {
    tc.findRouteChangeById("123:1:11") should matchTo(
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-02",
        addedToNetwork = Seq(Ref(1, "name")),
        before = Some(
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
                nodeIds = List(1001, 1002),
                tags = Tags.from("highway" -> "unclassified")
              )
            )
          )
        ),
        impactedNodeIds = Seq(1001, 1002),

        happy = true, // TODO should change so that adding a route to a network becomes neutral?
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
        )
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
        )
      )
    )
  }
}
