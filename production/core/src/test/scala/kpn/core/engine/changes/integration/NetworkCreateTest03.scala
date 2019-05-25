package kpn.core.engine.changes.integration

import kpn.core.analysis.Network
import kpn.core.changes.ElementIds
import kpn.core.test.TestData2
import kpn.shared.ChangeSetElementRef
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkChanges
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeAction
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RefChanges
import kpn.shared.changes.details.RouteChange
import kpn.shared.common.Ref
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawMember
import kpn.shared.data.raw.RawWay
import kpn.shared.diff.RefDiffs

class NetworkCreateTest03 extends AbstractTest {

  test("network create - ignored routes and ignored nodes are no longer ignored when part of the added network") {

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember("way", 101)))
      .data

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember("way", 101)))
      .networkRelation(1, "name", Seq(newMember("relation", 11)))
      .data

    val tc = new TestConfig()
    tc.relationBefore(dataBefore, 11)
    tc.nodesBefore(dataBefore, 1001, 1002)
    tc.relationAfter(dataAfter, 1)

    tc.analysisData.orphanRoutes.ignored.add(11, ElementIds())
    tc.analysisData.orphanNodes.ignored.add(1001L)
    tc.analysisData.orphanNodes.ignored.add(1002L)

    tc.analysisData.orphanRoutes.ignored.contains(11) should equal(true)
    tc.analysisData.orphanNodes.ignored.contains(1001) should equal(true)
    tc.analysisData.orphanNodes.ignored.contains(1002) should equal(true)

    tc.process(ChangeAction.Create, relation(dataAfter, 1))

    tc.analysisData.orphanRoutes.ignored.contains(11) should equal(false)
    tc.analysisData.orphanNodes.ignored.contains(1001) should equal(false)
    tc.analysisData.orphanNodes.ignored.contains(1002) should equal(false)

    (tc.analysisRepository.saveNetwork _).verify(
      where { network: Network =>
        network.id should equal(1)
        // for remaining network structure - see NetworkAnalyzerTest
        true
      }
    ).once()

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should equal(
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
            happy = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { networkChange: NetworkChange =>
        networkChange should equal(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Create,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "name",
            ignoredRoutes = RefChanges(oldRefs = Seq(Ref(11, "01-02"))),
            ignoredNodes = RefChanges(oldRefs = Seq(Ref(1001, "01"), Ref(1002, "02"))),
            networkNodes = RefDiffs(added = Seq(Ref(1001, "01"), Ref(1002, "02"))),
            routes = RefDiffs(added = Seq(Ref(11, "01-02"))),
            happy = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveRouteChange _).verify(
      where { routeChange: RouteChange =>
        routeChange should equal(
          newRouteChange(
            newChangeKey(elementId = 11),
            ChangeType.Update,
            "01-02",
            addedToNetwork = Seq(Ref(1, "name")),
            before = Some(
              newRouteData(
                Some(Country.nl),
                NetworkType.hiking,
                newRawRelation(
                  11,
                  members = Seq(RawMember("way", 101, None)),
                  tags = Tags.from("network" -> "rwn", "type" -> "route", "route" -> "foot", "note" -> "01-02")
                ),
                "01-02",
                networkNodes = Seq(
                  newRawNode(1001, tags = Tags.from("rwn_ref" -> "01")),
                  newRawNode(1002, tags = Tags.from("rwn_ref" -> "02"))
                ),
                nodes = Seq(
                  newRawNode(1001, tags = Tags.from("rwn_ref" -> "01")),
                  newRawNode(1002, tags = Tags.from("rwn_ref" -> "02"))
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
                newRawRelation(11,
                  members = Seq(
                    RawMember("way", 101, None)
                  ),
                  tags = Tags.from("network" -> "rwn", "type" -> "route", "route" -> "foot", "note" -> "01-02")
                ),
                "01-02",
                networkNodes = Seq(
                  newRawNode(1001, tags = Tags.from("rwn_ref" -> "01")),
                  newRawNode(1002, tags = Tags.from("rwn_ref" -> "02"))
                ),
                nodes = Seq(
                  newRawNode(1001, tags = Tags.from("rwn_ref" -> "01")),
                  newRawNode(1002, tags = Tags.from("rwn_ref" -> "02"))
                ),
                ways = Seq(
                  RawWay(101, 0, Timestamp(2015, 8, 11, 0, 0, 0), 0, Seq(1001, 1002), Tags.from("highway" -> "unclassified"))
                )
              )
            ),
            facts = Seq(Fact.WasIgnored)
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNodeChange _).verify(
      where { nodeChange: NodeChange =>
        nodeChange.key.elementId match {

          case 1001 =>
            nodeChange should equal(
              newNodeChange(
                newChangeKey(elementId = 1001),
                ChangeType.Update,
                Seq(Subset.nlHiking),
                "01",
                before = Some(
                  newRawNode(1001, tags = Tags.from("rwn_ref" -> "01"))
                ),
                after = Some(
                  newRawNode(1001, tags = Tags.from("rwn_ref" -> "01"))
                ),
                addedToNetwork = Seq(
                  Ref(1, "name")
                ),
                facts = Seq(Fact.WasIgnored)
              )
            )
            true

          case 1002 =>
            nodeChange should equal(
              newNodeChange(
                newChangeKey(elementId = 1002),
                ChangeType.Update,
                Seq(Subset.nlHiking),
                "02",
                before = Some(
                  newRawNode(1002, tags = Tags.from("rwn_ref" -> "02"))
                ),
                after = Some(
                  newRawNode(1002, tags = Tags.from("rwn_ref" -> "02"))
                ),
                addedToNetwork = Seq(
                  Ref(1, "name")
                ),
                facts = Seq(Fact.WasIgnored)
              )
            )
            true

          case _ => false
        }
      }
    ).repeated(2)
  }
}
