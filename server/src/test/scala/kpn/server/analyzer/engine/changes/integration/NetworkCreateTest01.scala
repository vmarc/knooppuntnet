package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.LatLonImpl
import kpn.api.common.NetworkChanges
import kpn.api.common.NetworkType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.data.MetaData
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NetworkNodeMember
import kpn.core.doc.NetworkRelationMember
import kpn.core.test.OverpassData

class NetworkCreateTest01 extends IntegrationTest {

  test("network create - added to watched list in memory and added to repository") {

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
      .networkRelation(
        1,
        "name",
        Seq(
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002),
          newMember(MemberType.Relation, 11)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      assert(database.orphanNodes.isEmpty)
      assert(database.orphanRoutes.findAll().sizeIs == 1)

      process(ChangeAction.Create, dataAfter.rawRelationWithId(1))

      assert(watched.networks.contains(1))
      assert(watched.routes.contains(11))
      assert(watched.nodes.contains(1001))
      assert(watched.nodes.contains(1002))

      assertNetworkDoc()
      assertNetworkInfoDoc()
      assertNetworkChange()
      assertNetworkInfoChange()
      assertRouteChange()
      assertNodeChange1001()
      assertNodeChange1002()
      assertChangeSetSummary()

      assert(database.orphanNodes.isEmpty)
      assert(database.orphanRoutes.isEmpty)
    }
  }

  private def assertNetworkDoc(): Unit = {
    assertEqual(
      findNetworkById(1),
      NetworkDoc(
        1,
        active = true,
        version = 0,
        changeSetId = 1,
        relationLastUpdated = defaultTimestamp,
        nodeMembers = Seq(
          NetworkNodeMember(1001, None),
          NetworkNodeMember(1002, None)
        ),
        wayMembers = Seq.empty,
        relationMembers = Seq(
          NetworkRelationMember(11L, None)
        ),
        tags = Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rwn",
          "name" -> "name",
        )
      )
    )
  }

  private def assertNetworkInfoDoc(): Unit = {
    assertEqual(
      findNetworkInfoById(1),
      newNetworkInfoDoc(
        1,
        summary = newNetworkSummary(
          name = "name",
          nodeCount = 2,
          routeCount = 1,
          changeCount = 1
        ),
        detail = newNetworkDetail(
          tags = Tags.from(
            "network:type" -> "node_network",
            "type" -> "network",
            "network" -> "rwn",
            "name" -> "name"
          ),
          center = Some(LatLonImpl("0.0", "0.0"))
        ),
        nodes = Seq(
          newNetworkInfoNodeDetail(
            1001,
            "01",
            definedInRelation = true
          ),
          newNetworkInfoNodeDetail(
            1002,
            "02",
            definedInRelation = true
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
              1002
            )
          )
        )
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
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
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("123:1:1"),
      newNetworkChange(
        newChangeKey(elementId = 1),
        networkName = "name",
        changeType = ChangeType.Create,
        networkDataUpdate = Some(
          NetworkDataUpdate(
            None,
            Some(
              NetworkData(
                MetaData(0, defaultTimestamp, 1),
                "name"
              )
            )
          )
        ),
        nodes = IdDiffs(
          added = Seq(1001, 1002)
        ),
        relations = IdDiffs(
          added = Seq(11)
        )
      )
    )
  }

  private def assertNetworkInfoChange(): Unit = {
    assertEqual(
      findNetworkInfoChangeById("123:1:1"),
      newNetworkInfoChange(
        newChangeKey(elementId = 1),
        networkName = "name",
        changeType = ChangeType.Create,
        country = Some(Country.nl),
        networkId = 1,
        nodeDiffs = RefDiffs(
          added = Seq(
            Ref(1001, "01"),
            Ref(1002, "02")
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
    pending // TODO redesign
    assertEqual(
      findRouteChangeById("123:1:11"),
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-02",
        addedToNetwork = Seq(Ref(1, "name")),
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
        after = None,
        //        Some(
        //          newRouteData(
        //            Some(Country.nl),
        //            NetworkType.hiking,
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
        addedToNetwork = Seq(
          Ref(1, "name")
        ),
        happy = true,
        impact = true
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
        addedToNetwork = Seq(
          Ref(1, "name")
        ),
        happy = true,
        impact = true
      )
    )
  }
}
