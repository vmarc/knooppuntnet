package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData

class RouteDeleteTest03 extends IntegrationTest {

  test("route looses route tags") {

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
      .relation(
        11,
        Seq(
          newMember(MemberType.Way, 101)
        ),
        Tags.from(
          "network:type" -> "node_network"
        )
      )

    testIntegration(dataBefore, dataAfter) {

      assert(watched.routes.contains(11))
      assert(database.orphanNodes.isEmpty)

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(11))

      assert(!watched.routes.contains(11))

      assertRoute()
      assertRouteChange()
      assertNodeChange1001()
      assertNodeChange1002()
      assertOrphanNode1001()
      assertOrphanNode1002()
      assert(database.orphanRoutes.isEmpty)
      assertChangeSetSummary()
    }
  }

  private def assertRoute(): Unit = {
    val routeDoc = findRouteById(11)
    routeDoc.id should equal(11)
    assert(!routeDoc.isActive)
  }

  private def assertRouteChange(): Unit = {
    pending // TODO redesign
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
        //            NetworkType.hiking,
        //            relation = newRawRelation(
        //              11,
        //              members = Seq(
        //                RawMember("way", 101, None)
        //              ),
        //              tags = Tags.from(
        //                "network" -> "rwn",
        //                "type" -> "route",
        //                "route" -> "foot", // this is removed in 'after' situation
        //                "ref" -> "01-02",
        //                "network:type" -> "node_network"
        //              )
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
        after = None,
        diffs = RouteDiff(
          tagDiffs = Some(
            TagDiffs(
              mainTags = Seq(
                TagDetail(TagDetailType.Delete, "ref", Some("01-02"), None),
                TagDetail(TagDetailType.Delete, "network", Some("rwn"), None),
                TagDetail(TagDetailType.Delete, "type", Some("route"), None),
                TagDetail(TagDetailType.Delete, "route", Some("foot"), None),
                TagDetail(TagDetailType.Same, "network:type", Some("node_network"), Some("node_network"))
              )
            )
          )
        ),
        facts = Seq(Fact.LostRouteTags),
        impactedNodeIds = Seq(1001L, 1002L),
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
        networkType = NetworkType.hiking,
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
        networkType = NetworkType.hiking,
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
              removed = Seq(
                newChangeSetElementRef(11, "01-02", investigate = true)
              )
            )
          )
        ),
        orphanNodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1001L, "01", investigate = true),
                newChangeSetElementRef(1002L, "02", investigate = true)
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
