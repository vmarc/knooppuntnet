package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSummary
import kpn.api.common.LatLonImpl
import kpn.api.common.NetworkChanges
import kpn.api.common.NetworkFacts
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkInfoDetail
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.TestData2

class NetworkAddRouteTest01 extends AbstractTest {

  test("network add route") {

    pending

    val dataBefore = TestData2()
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
      .data

    val dataAfter = TestData2()
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
      .data

    val tc = new TestConfig()
    tc.nodesBefore(dataAfter, 1001, 1002)
    tc.relationBefore(dataBefore, 1)
    tc.relationBefore(dataBefore, 11)

    tc.nodesAfter(dataAfter, 1001, 1002)
    tc.relationAfter(dataAfter, 1)
    tc.relationAfter(dataAfter, 11)

    tc.analysisContext.data.networks.watched.add(1, tc.relationAnalyzer.toElementIds(dataBefore.relations(1)))

    tc.process(ChangeAction.Modify, relation(dataAfter, 1))

    (tc.networkRepository.save _).verify(
      where { networkInfo: NetworkInfo =>
        networkInfo should matchTo(
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
        )
        true
      }
    )

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should matchTo(
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
        true
      }
    )

    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { networkChange: NetworkChange =>
        networkChange should matchTo(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Update,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "network",
            networkDataUpdate = Some(
              NetworkDataUpdate(
                NetworkData(
                  None,
                  Some(MetaData(1, Timestamp(2015, 8, 11, 0, 0, 0), 1)),
                  "network"
                ),
                NetworkData(
                  None,
                  Some(MetaData(1, Timestamp(2015, 8, 11, 0, 0, 0), 1)),
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
        true
      }
    )

    (tc.changeSetRepository.saveRouteChange _).verify(
      where { routeChange: RouteChange =>

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

        routeChange should matchTo(
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

        true
      }
    )

    (tc.nodeRepository.nodeRouteReferences _).verify(*, *).never()

    (tc.changeSetRepository.saveNodeChange _).verify(
      where { nodeChange: NodeChange =>
        nodeChange.key.elementId match {
          case 1001 =>
            nodeChange should matchTo(
              newNodeChange(
                key = newChangeKey(elementId = 1001),
                changeType = ChangeType.Update,
                subsets = Seq(Subset.nlHiking),
                name = "01",
                before = Some(
                  newRawNodeWithName(1001, "01")
                ),
                after = Some(
                  newRawNodeWithName(1001, "01")
                ),
                addedToRoute = Seq(Ref(11, "01-02")),
                happy = true,
                impact = true,
                locationHappy = true,
                locationImpact = true
              )
            )
          case 1002 =>
            nodeChange should matchTo(
              newNodeChange(
                key = newChangeKey(elementId = 1002),
                changeType = ChangeType.Update,
                subsets = Seq(Subset.nlHiking),
                name = "02",
                before = Some(
                  newRawNodeWithName(1002, "02")
                ),
                after = Some(
                  newRawNodeWithName(1002, "02")
                ),
                addedToRoute = Seq(Ref(11, "01-02")),
                happy = true,
                impact = true,
                locationHappy = true,
                locationImpact = true
              )
            )
        }
        true
      }
    ).repeat(2)
  }
}
