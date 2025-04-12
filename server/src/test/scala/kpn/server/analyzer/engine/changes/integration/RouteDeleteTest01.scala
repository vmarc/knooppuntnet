package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NodeName
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.test.OverpassData

class RouteDeleteTest01 extends IntegrationTest {

  test("delete route") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawRelation(11))

      watched.nodes.ids should contain(1001)
      watched.nodes.ids should contain(1002)
      watched.routes.ids shouldNot contain(11)

      assertRoute()
      assertNode1001()
      assertNode1002()
      assertRouteChange()
      assertNodeChange1001()
      assertNodeChange1002()
      assertOrphanNode1001()
      assertOrphanNode1002()
      database.orphanRoutes shouldBe empty
      assertChangeSetSummary()
    }
  }

  private def assertRoute(): Unit = {
    val routeDoc = findRouteById(11)
    routeDoc.id should equal(11)
    assert(!routeDoc.isActive)
  }

  private def assertNode1001(): Unit = {
    assertEqual(
      findNodeById(1001),
      newNodeDoc(
        1001,
        labels = Seq(
          Label.active,
          Label.routeType(RouteType.hiking)
        ),
        country = Some(Country.nl),
        name = Some("01"),
        names = Seq(
          NodeName(
            RouteType.hiking,
            RouteScope.regional,
            "01",
            None,
            proposed = false
          )
        ),
        tags = newNodeTags("01")
      )
    )
  }

  private def assertNode1002(): Unit = {
    assertEqual(
      findNodeById(1002),
      newNodeDoc(
        1002,
        labels = Seq(
          Label.active,
          Label.routeType(RouteType.hiking)
        ),
        country = Some(Country.nl),
        name = Some("02"),
        names = Seq(
          NodeName(
            RouteType.hiking,
            RouteScope.regional,
            "02",
            None,
            proposed = false
          )
        ),
        tags = newNodeTags("02")
      )
    )
  }

  private def assertRouteChange(): Unit = {
    assertEqual(
      findRouteChangeById("123:1:11"),
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Delete,
        "01-02",
        before = None,
        //        Some(
        //          newRouteData(
        //            Some(Country.nl),
        //            routeType.hiking,
        //            relation = newRawRelation(
        //              11,
        //              members = Seq(
        //                RawMember("way", 101, None)
        //              ),
        //              tags = newRouteTags("01-02")
        //            ),
        //            name = "01-02",
        //            networkNodes = Seq(
        //              newNodeWithName(1001, "01"),
        //              newNodeWithName(1002, "02")
        //            ),
        //            nodes = Seq(
        //              newNodeWithName(1001, "01"),
        //              newNodeWithName(1002, "02")
        //            ),
        //            ways = Seq(
        //              newRawWay(
        //                101,
        //                nodeIds = Vector(1001, 1002),
        //                tags = Tags.from("highway" -> "unclassified")
        //              )
        //            )
        //          )
        //        ),
        facts = Seq(Fact.Deleted),
        impactedNodeIds = Seq(1001, 1002),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1001(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1001"),
      newNodeChange(
        newChangeKey(elementId = 1001),
        ChangeType.Update,
        Seq(Subset.nlHiking),
        name = Some("01"),
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
    )
  }

  private def assertNodeChange1002(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1002"),
      newNodeChange(
        newChangeKey(elementId = 1002),
        ChangeType.Update,
        Seq(Subset.nlHiking),
        name = Some("02"),
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
    )
  }

  private def assertOrphanNode1001(): Unit = {
    assertEqual(
      findOrphanNodeById("nl:hiking:1001"),
      newOrphanNodeDoc(
        country = Country.nl,
        routeType = RouteType.hiking,
        nodeId = 1001L,
        name = "01"
      )
    )
  }

  private def assertOrphanNode1002(): Unit = {
    assertEqual(
      findOrphanNodeById("nl:hiking:1002"),
      newOrphanNodeDoc(
        country = Country.nl,
        routeType = RouteType.hiking,
        nodeId = 1002L,
        name = "02"
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        orphanRouteChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              removed = Seq(newChangeSetElementRef(11, "01-02", investigate = true))
            )
          )
        ),
        orphanNodeChanges = Seq(
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
}
