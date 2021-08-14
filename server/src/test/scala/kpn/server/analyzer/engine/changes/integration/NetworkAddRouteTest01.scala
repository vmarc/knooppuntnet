package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.LatLonImpl
import kpn.api.common.NetworkChanges
import kpn.api.common.NetworkFacts
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.network.NetworkInfoDetail
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData

class NetworkAddRouteTest01 extends IntegrationTest {

  test("network add route") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkRelation(
        1,
        "network",
        Seq(
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
        Seq(
          newMember("node", 1001),
          newMember("node", 1002),
          newMember("relation", 11)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

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
      //)

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
                ChangeSetElementRefs(
                  added = Seq(
                    ChangeSetElementRef(
                      11,
                      "01-02",
                      happy = true,
                      investigate = false
                    )
                  )
                ),
                ChangeSetElementRefs(
                  updated = Seq(
                    ChangeSetElementRef(
                      1001,
                      "01",
                      happy = false,
                      investigate = false
                    ),
                    ChangeSetElementRef(
                      1002,
                      "02",
                      happy = false,
                      investigate = false
                    )
                  )
                ),
                happy = true
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

      findNetworkInfoChangeById("123:1:1") should matchTo(
        newNetworkInfoChange(
          newChangeKey(elementId = 1),
          ChangeType.Update,
          Some(Country.nl),
          NetworkType.hiking,
          1,
          "network",
          networkDataUpdate = Some(
            NetworkDataUpdate(
              NetworkData(
                MetaData(1, Timestamp(2015, 8, 11, 0, 0, 0), 1),
                "network"
              ),
              NetworkData(
                MetaData(1, Timestamp(2015, 8, 11, 0, 0, 0), 1),
                "network"
              )
            )
          ),
          networkNodes = RefDiffs(
            updated = Seq(
              Ref(1001, "01"),
              Ref(1002, "02")
            )
          ),
          routes = RefDiffs(
            added = Seq(
              Ref(11, "01-02")
            )
          ),
          happy = true
        )
      )

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
          happy = true,
          impact = true,
          locationHappy = true,
          locationImpact = true
        )
      )

      // TODO (tc.nodeRepository.nodeRouteReferences _).verify(*, *).never()

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
  }
}
