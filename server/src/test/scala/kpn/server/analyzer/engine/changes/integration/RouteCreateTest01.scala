package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NodeName
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.data.raw.RawMember
import kpn.api.common.route.RouteInfo
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.mongo.doc.NodeDoc
import kpn.core.test.TestData
import kpn.core.test.TestData2
import kpn.core.test.TestSupport.withDatabase

class RouteCreateTest01 extends AbstractTest {

  test("create route") {

    withDatabase { database =>

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

      val tc = new OldTestConfig(dataBefore, dataAfter)

      tc.process(ChangeAction.Create, relation(dataAfter, 11))

      assert(tc.analysisContext.data.routes.watched.contains(11))
      assert(tc.analysisContext.data.orphanNodes.watched.contains(1001))
      assert(tc.analysisContext.data.orphanNodes.watched.contains(1002))

      (tc.routeRepository.save _).verify(
        where { routeInfo: RouteInfo =>
          routeInfo.id should equal(11)
          true
        }
      ).once()

      (tc.nodeRepository.save _).verify(
        where { nodeDoc: NodeDoc =>
          nodeDoc._id match {
            case 1001 =>
              nodeDoc should matchTo(
                newNodeDoc(
                  1001,
                  labels = Seq(
                    "active",
                    "network-type-hiking"
                  ),
                  country = Some(Country.nl),
                  name = "01",
                  names = Seq(
                    NodeName(
                      NetworkType.hiking,
                      NetworkScope.regional,
                      "01",
                      None,
                      proposed = false
                    )
                  ),
                  tags = newNodeTags("01")
                )
              )

            case 1002 =>
              nodeDoc should matchTo(
                newNodeDoc(
                  1002,
                  labels = Seq(
                    "active",
                    "network-type-hiking"
                  ),
                  country = Some(Country.nl),
                  name = "02",
                  names = Seq(
                    NodeName(
                      NetworkType.hiking,
                      NetworkScope.regional,
                      "02",
                      None,
                      proposed = false
                    )
                  ),
                  tags = newNodeTags("02")
                )
              )
          }
          true
        }
      ).repeated(2)

      (tc.changeSetRepository.saveChangeSetSummary _).verify(
        where { changeSetSummary: ChangeSetSummary =>
          changeSetSummary should matchTo(
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
              subsetAnalyses = Seq(
                ChangeSetSubsetAnalysis(Subset.nlHiking, happy = true)
              ),
              happy = true
            )
          )
          true
        }
      )

      (tc.changeSetRepository.saveRouteChange _).verify(
        where { routeChange: RouteChange =>
          routeChange should matchTo(
            newRouteChange(
              newChangeKey(elementId = 11),
              ChangeType.Create,
              "01-02",
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
                      nodeIds = Seq(1001, 1002),
                      tags = Tags.from("highway" -> "unclassified")
                    )
                  )
                )
              ),
              facts = Seq(Fact.OrphanRoute),
              happy = true,
              impact = true,
              locationHappy = true,
              locationImpact = true
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
      //            nodeChange should matchTo(
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
      //            nodeChange should matchTo(
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
}
