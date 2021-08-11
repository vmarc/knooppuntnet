package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.NodeName
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class RouteCreateTest01 extends AbstractIntegrationTest {

  test("create route") {

    withDatabase { database =>

      val dataBefore = OverpassData.empty

      val dataAfter = OverpassData()
        .networkNode(1001, "01")
        .networkNode(1002, "02")
        .way(101, 1001, 1002)
        .route(11, "01-02",
          Seq(
            newMember("way", 101)
          )
        )

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Create, dataAfter.rawRelationWithId(11))

      assert(tc.analysisContext.data.routes.watched.contains(11))
      assert(tc.analysisContext.data.nodes.watched.contains(1001))
      assert(tc.analysisContext.data.nodes.watched.contains(1002))

      val routeInfo = tc.findRouteById(11)
      routeInfo.summary.name should equal("01-02")

      tc.findNodeById(1001) should matchTo {
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
      }

      tc.findNodeById(1002) should matchTo {
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
      }

      tc.findChangeSetSummaryById("123:1") should matchTo {
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
          orphanNodeChanges = Seq(
            ChangeSetSubsetElementRefs(
              Subset.nlHiking,
              ChangeSetElementRefs(
                added = Seq(
                  newChangeSetElementRef(1001, "01", happy = true),
                  newChangeSetElementRef(1002, "02", happy = true)
                )
              )
            )
          ),
          subsetAnalyses = Seq(
            ChangeSetSubsetAnalysis(Subset.nlHiking, happy = true)
          ),
          happy = true
        )
      }

      tc.findRouteChangeById("123:1:11") should matchTo {
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
          impactedNodeIds = Seq(1001, 1002),
          happy = true,
          impact = true,
          locationHappy = true,
          locationImpact = true
        )
      }

      tc.findNodeChangeById("123:1:1001") should matchTo {
        newNodeChange(
          newChangeKey(elementId = 1001),
          ChangeType.Create,
          Seq(Subset.nlHiking),
          name = "01",
          before = None,
          after = Some(
            newMetaData()
          ),
          addedToRoute = Seq(
            // TODO Ref(11, "01-02")
          ),
          happy = true,
          impact = true,
          locationHappy = true,
          locationImpact = true
        )
      }

      tc.findNodeChangeById("123:1:1002") should matchTo {
        newNodeChange(
          newChangeKey(elementId = 1002),
          ChangeType.Create,
          Seq(Subset.nlHiking),
          name = "02",
          before = None,
          after = Some(
            newMetaData()
          ),
          addedToRoute = Seq(
            // TODO Ref(11, "01-02")
          ),
          happy = true,
          impact = true,
          locationHappy = true,
          locationImpact = true
        )
      }
    }
  }
}
