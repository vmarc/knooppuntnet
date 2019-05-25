package kpn.core.engine.changes.integration

import kpn.core.test.TestData
import kpn.core.test.TestData2
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetSubsetElementRefs
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.NodeInfo
import kpn.shared.Subset
import kpn.shared.changes.ChangeAction
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.RouteChange
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawMember
import kpn.shared.route.RouteInfo

class OrphanRouteTest01 extends AbstractTest {

  test("create orphan route") {

    val dataBefore = TestData2().data

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      .data

    val tc = new TestConfig()

    tc.relationAfter(dataAfter, 11)
    tc.process(ChangeAction.Create, TestData.relation(dataAfter, 11))

    tc.analysisData.orphanRoutes.watched.contains(11) should equal(true)

    (tc.analysisRepository.saveRoute _).verify(
      where { routeInfo: RouteInfo =>
        routeInfo.id should equal(11)
        true
      }
    ).once()

    (tc.analysisRepository.saveNode _).verify(
      where { nodeInfo: NodeInfo =>
        nodeInfo.id match {
          case 1001 =>
            nodeInfo should equal(
              newNodeInfo(
                1001,
                country = Some(Country.nl),
                tags = Tags.from("rwn_ref" -> "01")
              )
            )

          case 1002 =>
            nodeInfo should equal(
              newNodeInfo(
                1002,
                country = Some(Country.nl),
                tags = Tags.from("rwn_ref" -> "02")
              )
            )
        }
        true
      }
    ).repeated(2)

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should equal(
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            orphanRouteChanges = Seq(
              ChangeSetSubsetElementRefs(
                Subset.nlHiking,
                ChangeSetElementRefs(
                  added = Seq(newChangeSetElementRef(11, "01-02", happy = true))
                )
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
        routeChange should equal(
          newRouteChange(
            newChangeKey(elementId = 11),
            ChangeType.Create,
            "01-02",
            after = Some(
              newRouteData(
                Some(Country.nl),
                NetworkType.hiking,
                newRawRelation(
                  11,
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
                  newRawWay(
                    101,
                    nodeIds = Seq(1001, 1002),
                    tags = Tags.from("highway" -> "unclassified")
                  )
                )
              )
            ),
            facts = Seq(Fact.OrphanRoute)
          )
        )
        true
      }
    )

    // TODO ROUTE node changes are not saved yet for orphan routes apparantly...
    //    (tc.changeSetRepository.saveNodeChange _).verify(
    //      where { (nodeChange: NodeChange) =>
    //        nodeChange.key.elementId match {
    //          case (1001) =>
    //            nodeChange should equal(
    //              newNodeChange(
    //                newChangeKey(elementId = 1001),
    //                ChangeType.Create,
    //                Seq(Subset.nlHiking),
    //                "01",
    //                before = None,
    //                after = Some(
    //                  newRawNodeWithName(1001, "01")
    //                ),
    //                addedToRoute = Seq(
    //                  Ref(11, "01-02")
    //                )
    //              )
    //            )
    //            true
    //
    //          case (1002) =>
    //            nodeChange should equal(
    //              newNodeChange(
    //                newChangeKey(elementId = 1002),
    //                ChangeType.Create,
    //                Seq(Subset.nlHiking),
    //                "02",
    //                before = None,
    //                after = Some(
    //                  newRawNodeWithName(1002, "02")
    //                ),
    //                addedToRoute = Seq(
    //                  Ref(11, "01-02")
    //                )
    //              )
    //            )
    //            true
    //
    //          case _ => false
    //        }
    //      }
    //    ).repeated(2)
  }
}
