package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.core.test.OverpassData

class NetworkDeleteRouteTest01 extends IntegrationTest {

  test("network delete - route becomes orphan") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
      .networkRelation(
        1,
        "network",
        Seq(
          newMember(MemberType.Relation, 11)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawRelation(1))

      assert(!watched.networks.contains(1))
      assert(watched.routes.contains(11))

      assertOrphanRoute()
      assertNetworkInfo()
      assertNetworkInfoChange()
      assertRouteChange()
      assertChangeSetSummary()
    }
  }

  private def assertOrphanRoute(): Unit = {
    assertEqual(
      findOrphanRouteById(11),
      newOrphanRouteDoc(
        11,
        Country.nl,
        RouteType.hiking,
        "01-02",
      )
    )
  }

  private def assertNetworkInfo(): Unit = {
    assertEqual(
      findNetworkById(1),
      newNetworkInfoDoc(
        1,
        active = false, // <--- !!!
        country = Some(Country.nl),
        newNetworkSummary(
          name = "network",
          routeType = RouteType.hiking,
          changeCount = 1
        ),
        newNetworkDetail(
          lastUpdated = defaultTimestamp,
          relationLastUpdated = defaultTimestamp,
          tags = newNetworkTags("network")
        )
      )
    )
  }

  private def assertNetworkInfoChange(): Unit = {
    assertEqual(
      findNetworkInfoChangeById("123:1:1"),
      newNetworkInfoChange(
        newChangeKey(elementId = 1),
        ChangeType.Delete,
        Some(Country.nl),
        RouteType.hiking,
        1,
        "network",
        nodeDiffs = RefDiffs(
          removed = Seq(
            Ref(1001, "01"),
            Ref(1002, "02")
          )
        ),
        routeDiffs = RefDiffs(
          removed = Seq(
            Ref(11, "01-02")
          )
        ),
        investigate = true
      )
    )
  }

  private def assertRouteChange(): Unit = {
    pending // TODO redesign
    val routeData = newRouteData()
    //  val routeData = newRouteData(
    //    Some(Country.nl),
    //    routeType.hiking,
    //    relation = newRawRelation(
    //      11,
    //      members = Seq(
    //        RawMember("way", 101, None)
    //      ),
    //      tags = newRouteTags("01-02")
    //    ),
    //    name = "01-02",
    //    networkNodes = Seq(
    //      newNodeWithName(1001, "01"),
    //      newNodeWithName(1002, "02")
    //    ),
    //    nodes = Seq(
    //      newNodeWithName(1001, "01"),
    //      newNodeWithName(1002, "02")
    //    ),
    //    ways = Seq(
    //      newRawWay(
    //        101,
    //        nodeIds = Vector(1001, 1002),
    //        tags = Tags.from("highway" -> "unclassified")
    //      )
    //    )
    //  )

    assertEqual(
      findRouteChangeById("123:1:11"),
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-02",
        removedFromNetwork = Seq(Ref(1, "network")),
        before = Some(routeData),
        after = Some(routeData),
        impactedNodeIds = Seq(1001, 1002),
        investigate = true,
        impact = true
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        networkChanges = NetworkChanges(
          deletes = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              "network",
              routeChanges = ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(11, "01-02", investigate = true)
                )
              ),
              nodeChanges = ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(1001, "01", investigate = true),
                  newChangeSetElementRef(1002, "02", investigate = true)
                )
              ),
              investigate = true
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
