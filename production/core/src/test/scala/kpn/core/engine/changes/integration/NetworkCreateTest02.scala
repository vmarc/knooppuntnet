package kpn.core.engine.changes.integration

import kpn.core.analysis.Network
import kpn.core.changes.ElementIds
import kpn.core.test.TestData2
import kpn.shared.ChangeSetElementRef
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetSubsetAnalysis
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

class NetworkCreateTest02 extends AbstractTest {

  test("network create - orphan routes and orphan nodes are no longer orphan when part of the added network") {

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

    tc.analysisData.orphanRoutes.watched.add(11, ElementIds())
    tc.analysisData.orphanNodes.watched.add(1001L)
    tc.analysisData.orphanNodes.watched.add(1002L)

    tc.analysisData.orphanRoutes.watched.contains(11) should equal(true)
    tc.analysisData.orphanNodes.watched.contains(1001) should equal(true)
    tc.analysisData.orphanNodes.watched.contains(1002) should equal(true)

    tc.process(ChangeAction.Create, relation(dataAfter, 1))

    tc.analysisData.orphanRoutes.watched.contains(11) should equal(false)
    tc.analysisData.orphanNodes.watched.contains(1001) should equal(false)
    tc.analysisData.orphanNodes.watched.contains(1002) should equal(false)

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
            subsetAnalyses = Seq(
              ChangeSetSubsetAnalysis(Subset.nlHiking, happy = true)
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
            orphanRoutes = RefChanges(oldRefs = Seq(Ref(11, "01-02"))),
            orphanNodes = RefChanges(oldRefs = Seq(Ref(1001, "01"), Ref(1002, "02"))),
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
                  tags = newRouteTags("01-02")
                ),
                "01-02",
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
                newRawRelation(11,
                  members = Seq(
                    RawMember("way", 101, None)
                  ),
                  tags = newRouteTags("01-02")
                ),
                "01-02",
                networkNodes = Seq(
                  newRawNodeWithName(1001, "01"),
                  newRawNodeWithName(1002, "02")
                ),
                nodes = Seq(
                  newRawNodeWithName(1001, "01"),
                  newRawNodeWithName(1002, "02")
                ),
                ways = Seq(
                  RawWay(101, 0, Timestamp(2015, 8, 11, 0, 0, 0), 0, Seq(1001, 1002), Tags.from("highway" -> "unclassified"))
                )
              )
            ),
            facts = Seq(Fact.WasOrphan),
            happy = true
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
                  newRawNodeWithName(1001, "01")
                ),
                after = Some(
                  newRawNodeWithName(1001, "01")
                ),
                addedToNetwork = Seq(
                  Ref(1, "name")
                ),
                facts = Seq(Fact.WasOrphan),
                happy = true
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
                  newRawNodeWithName(1002, "02")
                ),
                after = Some(
                  newRawNodeWithName(1002, "02")
                ),
                addedToNetwork = Seq(
                  Ref(1, "name")
                ),
                facts = Seq(Fact.WasOrphan),
                happy = true
              )
            )
            true

          case _ => false
        }
      }
    ).repeated(2)
  }
}
