package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.NodeName
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class RouteDeleteTest01 extends AbstractIntegrationTest {

  test("delete route") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02",
        Seq(
          newMember("way", 101)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Delete, newRawRelation(11))

      assert(tc.analysisContext.watched.nodes.contains(1001))
      assert(tc.analysisContext.watched.nodes.contains(1002))
      assert(!tc.analysisContext.watched.routes.contains(11))

      assertRoute(tc)
      assertNode1001(tc)
      assertNode1002(tc)
      assertRouteChange(tc)
      assertNodeChange1001(tc)
      assertNodeChange1002(tc)
      assertOrphanNode1001(tc)
      assertOrphanNode1002(tc)
      assert(database.orphanRoutes.findAll().isEmpty)
      assertChangeSetSummary(tc)
    }
  }

  private def assertRoute(tc: IntegrationTestContext): Unit = {
    val routeInfo = tc.findRouteById(11)
    routeInfo.id should equal(11)
    assert(!routeInfo.active)
  }

  private def assertNode1001(tc: IntegrationTestContext): Unit = {
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
  }

  private def assertNode1002(tc: IntegrationTestContext): Unit = {
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
  }

  private def assertChangeSetSummary(tc: IntegrationTestContext): Unit = {
    tc.findChangeSetSummaryById("123:1") should matchTo(
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        routeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              removed = Seq(newChangeSetElementRef(11, "01-02", investigate = true))
            )
          )
        ),
        nodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1001, "01", investigate = true),
                newChangeSetElementRef(1002, "02", investigate = true)
              )
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

  private def assertRouteChange(tc: IntegrationTestContext): Unit = {
    tc.findRouteChangeById("123:1:11") should matchTo(
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Delete,
        "01-02",
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
        facts = Seq(Fact.Deleted),
        impactedNodeIds = Seq(1001, 1002),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1001(tc: IntegrationTestContext): Unit = {
    tc.findNodeChangeById("123:1:1001") should matchTo {
      newNodeChange(
        newChangeKey(elementId = 1001),
        ChangeType.Update,
        Seq(Subset.nlHiking),
        name = "01",
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        removedFromRoute = Seq(
          Ref(11, "01-02")
        ),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
      )
    }
  }

  private def assertNodeChange1002(tc: IntegrationTestContext): Unit = {
    tc.findNodeChangeById("123:1:1002") should matchTo {
      newNodeChange(
        newChangeKey(elementId = 1002),
        ChangeType.Update,
        Seq(Subset.nlHiking),
        name = "02",
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        removedFromRoute = Seq(
          Ref(11, "01-02")
        ),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
      )
    }
  }

  private def assertOrphanNode1001(tc: IntegrationTestContext): Unit = {
    tc.findOrphanNodeById("nl:hiking:1001") should matchTo(
      newOrphanNodeDoc(
        country = Country.nl,
        networkType = NetworkType.hiking,
        nodeId = 1001L,
        name = "01"
      )
    )
  }

  private def assertOrphanNode1002(tc: IntegrationTestContext): Unit = {
    tc.findOrphanNodeById("nl:hiking:1002") should matchTo(
      newOrphanNodeDoc(
        country = Country.nl,
        networkType = NetworkType.hiking,
        nodeId = 1002L,
        name = "02"
      )
    )
  }
}
