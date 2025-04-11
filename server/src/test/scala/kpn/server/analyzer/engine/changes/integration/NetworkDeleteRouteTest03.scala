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

class NetworkDeleteRouteTest03 extends IntegrationTest {

  test("network delete - route looses route tags") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01") // referenced in network1 and network2 and orphan route
      .networkNode(1002, "02") // referenced in network1
      .networkNode(1003, "03") // referenced in network2
      .networkNode(1004, "04") // referenced in orphan route
      .way(101, 1001, 1002) // route 11 only referenced in network 1
      .route(11, "01-02", Seq(newMember(MemberType.Way, 101)))
      .way(102, 1001, 1003) // route 12 referenced in network 1 and network 2
      .route(12, "01-03", Seq(newMember(MemberType.Way, 102)))
      .networkRelation(1, "network1", Seq(newMember(MemberType.Relation, 11), newMember(MemberType.Relation, 12)))
      .networkRelation(2, "network2", Seq(newMember(MemberType.Relation, 12)))

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkNode(1003, "03")
      .networkNode(1004, "04")
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember(MemberType.Way, 101))) // route has become orphan
      .way(102, 1001, 1003) // route 12 still referenced in network 2
      .route(12, "01-03", Seq(newMember(MemberType.Way, 102)))
      .networkRelation(2, "network2", Seq(newMember(MemberType.Relation, 12)))

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawRelation(1))

      // network 1 is no longer in memory
      watched.networks.ids should not contain (1)

      watched.routes.ids should contain(11) // network 1 was removed, route no longer referenced
      watched.routes.ids should contain(12) // network 1 was removed, but route still referenced in network 2

      watched.nodes.ids should contain(1001)
      watched.nodes.ids should contain(1002) // still referenced in orphan route
      watched.nodes.ids should contain(1003)

      assertNetwork()
      assertNetworkChange()
      assertRoute11()
      assertRoute12()
      assertChangeSetSummary()
    }
  }

  private def assertNetwork(): Unit = {
    assertEqual(
      findNetworkById(1),
      newNetworkDoc(
        1,
        active = false, // <--- !!!
        country = Some(Country.nl),
        newNetworkSummary(
          name = "network1",
          routeType = RouteType.hiking,
        ),
        newNetworkDetail(
          lastUpdated = defaultTimestamp,
          relationLastUpdated = defaultTimestamp,
          tags = newNetworkTags("network1")
        )
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("123:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = "network1",
        changeType = ChangeType.Delete,
        country = Some(Country.nl),
        routeType = RouteType.hiking,
        //  networkDataUpdate = None,
        //  nodes= IdDiffs.empty,
        //  ways = IdDiffs.empty,
        //  relations = IdDiffs.empty,
        nodeDiffs = RefDiffs(
          removed = Seq(
            Ref(1001, "01"),
            Ref(1002, "02"),
            Ref(1003, "03")
          )
        ),
        routeDiffs = RefDiffs(
          removed = Seq(
            Ref(11, "01-02"),
            Ref(12, "01-03")
          )
        ),
        //  extraNodeDiffs = IdDiffs.empty,
        //  extraWayDiffs = IdDiffs.empty,
        //  extraRelationDiffs = IdDiffs.empty,
        //  happy = false,
        investigate = true,
        impact = true,
      )
    )
  }

  private def assertRoute11(): Unit = {

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
    //    ),
    //  )

    assertEqual(
      findRouteChangeById("123:1:11"),
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-02",
        removedFromNetwork = Seq(Ref(1, "network1")),
        before = Some(routeData),
        after = Some(routeData),
        impactedNodeIds = Seq(1001, 1002),
        investigate = true,
        impact = true
      )
    )
  }

  private def assertRoute12(): Unit = {

    pending // TODO redesign
    val routeData = newRouteData()
    //  val routeData = newRouteData(
    //    Some(Country.nl),
    //    routeType.hiking,
    //    relation = newRawRelation(
    //      12,
    //      members = Seq(
    //        RawMember("way", 102, None)
    //      ),
    //      tags = newRouteTags("01-03")
    //    ),
    //    name = "01-03",
    //    networkNodes = Seq(
    //      newNodeWithName(1001, "01"),
    //      newNodeWithName(1003, "03")
    //    ),
    //    nodes = Seq(
    //      newNodeWithName(1001, "01"),
    //      newNodeWithName(1003, "03")
    //    ),
    //    ways = Seq(
    //      newRawWay(
    //        102,
    //        nodeIds = Vector(1001, 1003),
    //        tags = Tags.from("highway" -> "unclassified")
    //      )
    //    )
    //  )

    assertEqual(
      findRouteChangeById("123:1:12"),
      newRouteChange(
        newChangeKey(elementId = 12),
        ChangeType.Update,
        "01-03",
        removedFromNetwork = Seq(Ref(1, "network1")),
        before = Some(routeData),
        after = Some(routeData),
        impactedNodeIds = Seq(1001, 1003),
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
              "network1",
              routeChanges = ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(11, "01-02", investigate = true),
                  newChangeSetElementRef(12, "01-03", investigate = true)
                )
              ),
              nodeChanges = ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(1001, "01", investigate = true),
                  newChangeSetElementRef(1002, "02", investigate = true),
                  newChangeSetElementRef(1003, "03", investigate = true)
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
