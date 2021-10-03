package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.LatLonImpl
import kpn.api.common.NetworkChanges
import kpn.api.common.NetworkFacts
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.RefDiffs
import kpn.api.common.network.NetworkInfoDetail
import kpn.api.custom.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData

class NetworkAddRouteTest01 extends IntegrationTest {

  test("network add route") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkRelation(
        1,
        "network",
        version = 1,
        members = Seq(
          newMember("node", 1001),
          newMember("node", 1002)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember("way", 101)))
      .networkRelation(
        1,
        "network",
        version = 2,
        members = Seq(
          newMember("node", 1001),
          newMember("node", 1002),
          newMember("relation", 11)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      assertNetworkInfo()
      assertNetworkInfoChange()
      assertRouteChange()

      // TODO (tc.nodeRepository.nodeRouteReferences _).verify(*, *).never()

      assertNodeChange1001()
      assertNodeChange1002()
      assertChangeSetSummary()
    }
  }

  private def assertNetworkInfo(): Unit = {
    findNetworkInfoById(1) // TODO should matchTo(
    newNetworkInfo(
      newNetworkAttributes(
        1,
        Some(Country.nl),
        NetworkType.hiking,
        name = "network",
        lastUpdated = defaultTimestamp,
        relationLastUpdated = defaultTimestamp,
        nodeCount = 2,
        routeCount = 1,
        brokenRoutePercentage = "-",
        integrity = newIntegrity(
          isOk = false,
          coverage = "-",
          okRate = "-",
          nokRate = "-"
        ),
        center = Some(LatLonImpl("0.0", "0.0"))
      ),
      nodeRefs = Seq(
        1001L,
        1002L
      ),
      routeRefs = Seq(
        11L
      ),
      tags = Tags.from(
        "network" -> "rwn",
        "type" -> "network",
        "name" -> "network",
        "network:type" -> "node_network"
      ),
      detail = Some(
        NetworkInfoDetail(
          nodes = Seq(
            newNetworkInfoNode(
              1001,
              "01",
              latitude = "0",
              longitude = "0",
              definedInRelation = true,
              routeReferences = Seq(Ref(11, "01-02")),
              tags = Tags.from(
                "rwn_ref" -> "01",
                "network:type" -> "node_network"
              )
            ),
            newNetworkInfoNode(
              1002,
              "02",
              latitude = "0",
              longitude = "0",
              definedInRelation = true,
              routeReferences = Seq(Ref(11, "01-02")),
              tags = Tags.from(
                "rwn_ref" -> "02",
                "network:type" -> "node_network"
              )
            )
          ),
          routes = Seq(
            newNetworkInfoRoute(
              11,
              "01-02",
              wayCount = 1
            )
          ),
          networkFacts = NetworkFacts(),
          shape = None
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
        "network",
        networkDataUpdate = None,
        //  Some( // TODO MONGO should contain change from version 1 to 2
        //    NetworkDataUpdate(
        //      NetworkData(
        //        MetaData(1, Timestamp(2015, 8, 11, 0, 0, 0), 1),
        //        "network"
        //      ),
        //      NetworkData(
        //        MetaData(1, Timestamp(2015, 8, 11, 0, 0, 0), 1),
        //        "network"
        //      )
        //    )
        //  ),
        routes = RefDiffs(
          added = Seq(
            Ref(11, "01-02")
          )
        ),
        happy = true
      )
    )
  }

  private def assertRouteChange() = {

    val routeData = newRouteData(
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

    findRouteChangeById("123:1:11") should matchTo(
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Create,
        "01-02",
        addedToNetwork = Seq(Ref(1, "network")),
        before = None,
        after = Some(routeData),
        impactedNodeIds = Seq(1001, 1002),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1001(): Unit = {
    findNodeChangeById("123:1:1001") should matchTo(
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
        addedToRoute = Seq(Ref(11, "01-02")),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1002(): Unit = {
    findNodeChangeById("123:1:1002") should matchTo(
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
        addedToRoute = Seq(Ref(11, "01-02")),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
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
              "network",
              routeChanges = ChangeSetElementRefs(
                added = Seq(
                  ChangeSetElementRef(
                    11,
                    "01-02",
                    happy = true,
                    investigate = false
                  )
                )
              ),
              happy = true
            )
          )
        ),

        routeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              added = Seq(
                newChangeSetElementRef(11, "01-02", happy = true))
            )
          )
        ),
        nodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1001, "01", happy = true),
                newChangeSetElementRef(1002, "02", happy = true)
              )
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(
            Subset.nlHiking,
            happy = true
          )
        ),
        happy = true
      )
    )
  }
}
