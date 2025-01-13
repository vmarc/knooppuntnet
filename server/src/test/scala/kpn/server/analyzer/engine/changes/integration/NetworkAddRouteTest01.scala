package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.LatLonImpl
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.data.MemberType
import kpn.api.common.data.MetaData
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData

class NetworkAddRouteTest01 extends IntegrationTest {

  test("network add route") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkRelation(
        1,
        "network",
        version = 1,
        members = Seq(
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember(MemberType.Way, 101)))
      .networkRelation(
        1,
        "network",
        version = 2,
        members = Seq(
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002),
          newMember(MemberType.Relation, 11)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      assertNetworkDoc()
      assertNetworkInfoChange()
      assertRouteChange()
      assertNodeChange1001()
      assertNodeChange1002()
      assertChangeSetSummary()
      assertNodeRouteReferences()
    }
  }

  private def assertNetworkDoc(): Unit = {
    assertEqual(
      findNetworkById(1),
      newNetworkDoc(
        1,
        country = Some(Country.nl),
        summary = newNetworkSummary(
          name = "network",
          nodeCount = 2,
          routeCount = 1,
          changeCount = 1
        ),
        detail = newNetworkDetail(
          version = 2,
          tags = Tags.from(
            "network:type" -> "node_network",
            "type" -> "network",
            "network" -> "rwn",
            "name" -> "network",
          ),
          center = Some(LatLonImpl("0.0", "0.0")),
        ),
        nodes = Seq(
          newNetworkInfoNodeDetail(
            1001,
            "01",
            definedInRelation = true,
          ),
          newNetworkInfoNodeDetail(
            1002,
            "02",
            definedInRelation = true,
          )
        ),
        routes = Seq(
          newNetworkInfoRouteDetail(
            11,
            "01-02",
            tags = Tags.from(
              "network" -> "rwn",
              "type" -> "route",
              "route" -> "foot",
              "ref" -> "01-02",
              "network:type" -> "node_network"
            ),
            nodeRefs = Seq(
              1001,
              1002)
          )
        )
      )
    )
  }

  private def assertNetworkInfoChange(): Unit = {
    assertEqual(
      findNetworkInfoChangeById("123:1:1"),
      newNetworkInfoChange(
        newChangeKey(elementId = 1),
        ChangeType.Update,
        Some(Country.nl),
        RouteType.hiking,
        1,
        "network",
        networkDataUpdate = Some(
          NetworkDataUpdate(
            Some(
              NetworkData(
                MetaData(1, defaultTimestamp, 1),
                "network"
              )
            ),
            Some(
              NetworkData(
                MetaData(2, defaultTimestamp, 1),
                "network"
              )
            )
          )
        ),
        nodeDiffs = RefDiffs(
          updated = Seq(
            Ref(1001, "01"),
            Ref(1002, "02"),
          )
        ),
        routeDiffs = RefDiffs(
          added = Seq(
            Ref(11, "01-02")
          )
        ),
        happy = true
      )
    )
  }

  private def assertRouteChange(): Unit = {

    val routeData = newRouteData()
    pending // TODO redesign
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
        ChangeType.Create,
        "01-02",
        addedToNetwork = Seq(Ref(1, "network")),
        before = None,
        after = Some(routeData),
        impactedNodeIds = Seq(1001, 1002),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1001(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1001"),
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        name = Some("01"),
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        addedToRoute = Seq(Ref(11, "01-02")),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1002(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1002"),
      newNodeChange(
        key = newChangeKey(elementId = 1002),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        name = Some("02"),
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        addedToRoute = Seq(Ref(11, "01-02")),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        networkChanges = NetworkChanges(
          updates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              "network",
              nodeChanges = ChangeSetElementRefs(
                updated = Seq(
                  ChangeSetElementRef(
                    1001,
                    "01",
                    happy = true,
                    investigate = false
                  ),
                  ChangeSetElementRef(
                    1002,
                    "02",
                    happy = true,
                    investigate = false
                  )
                )
              ),
              routeChanges = ChangeSetElementRefs(
                added = Seq(
                  ChangeSetElementRef(
                    11,
                    "01-02",
                    happy = true,
                    investigate = false
                  )
                )
              ),
              happy = true
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(
            Subset.nlHiking,
            happy = true
          )
        ),
        happy = true
      )
    )
  }

  private def assertNodeRouteReferences(): Unit = {
    val nodeRouteReferences = Seq(
      Reference(
        RouteType.hiking,
        RouteScope.regional,
        11,
        "01-02"
      )
    )
    context.nodeRepository.nodeRouteReferences(1001) should equal(nodeRouteReferences)
    context.nodeRepository.nodeRouteReferences(1002) should equal(nodeRouteReferences)
  }
}
