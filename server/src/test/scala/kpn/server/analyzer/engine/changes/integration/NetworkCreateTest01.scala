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
import kpn.api.common.data.MemberType
import kpn.api.common.data.MetaData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.BaseNetworkDoc
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetNetwork
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newNetworkChange
import kpn.core.test.TestObjects.newNetworkDetail
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newNetworkInfoNodeDetail
import kpn.core.test.TestObjects.newNetworkRouteDetail
import kpn.core.test.TestObjects.newNetworkSummary
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteData
import kpn.core.test.TestObjects.newRouteNode
import kpn.core.test.TestObjects.newRouteNodeChange
import kpn.core.test.Timestamps

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
      .networkRelation( // create
        1,
        "network-name",
        Seq(
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002),
          newMember(MemberType.Relation, 11)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      assert(!watched.networks.contains(1))
      watched.routes.ids should contain(11)
      assert(watched.nodes.contains(1001))
      assert(watched.nodes.contains(1002))
      findOrphanNodes() shouldBe empty
      findOrphanRoutes().map(_.id) should equal(Seq(11))

      process(ChangeAction.Create, dataAfter.rawRelationWithId(1))

      assert(watched.networks.contains(1))
      watched.routes.ids should contain(11)
      assert(watched.nodes.contains(1001))
      assert(watched.nodes.contains(1002))

      assertBaseNetworkDoc()
      assertNetworkDoc()
      assertNetworkChange()
      assertRouteChange()
      assertNodeChange1001()
      assertNodeChange1002()
      assertChangeSetSummary()

      findOrphanNodes() shouldBe empty
      findOrphanRoutes() shouldBe empty
    }
  }

  private def assertBaseNetworkDoc(): Unit = {
    assertEqual(
      findBaseNetworkById(1),
      BaseNetworkDoc(
        1,
        active = true,
        routeType = RouteType.hiking,
        routeScope = RouteScope.regional,
        name = Some("network-name"),
        version = 0,
        changeSetId = 1,
        timestamp = Timestamps.default,
        members = Seq(
          RawMember(MemberType.Node, 1001, None),
          RawMember(MemberType.Node, 1002, None),
          RawMember(MemberType.Relation, 11, None),
        ),
        tags = Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rwn",
          "name" -> "network-name",
        ),
        nodeIds = Seq(
          1001,
          1002
        ),
        relationIds = Seq(
          11
        )
      )
    )
  }

  private def assertNetworkDoc(): Unit = {
    assertEqual(
      findNetworkById(1),
      newNetworkDoc(
        1,
        summary = newNetworkSummary(
          name = "network-name",
          nodeCount = 2,
          routeCount = 1,
        ),
        detail = newNetworkDetail(
          tags = Tags.from(
            "network:type" -> "node_network",
            "type" -> "network",
            "network" -> "rwn",
            "name" -> "network-name"
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
          newNetworkRouteDetail(
            11,
            "01-02",
            tags = Tags.from(
              "network" -> "rwn",
              "type" -> "route",
              "route" -> "foot",
              "ref" -> "01-02",
              "network:type" -> "node_network"
            ),
            networkNodeIds = Some(
              Seq(
                1001,
                1002
              )
            )
          )
        ),
        members = Seq(
          RawMember(MemberType.Node, 1001, None),
          RawMember(MemberType.Node, 1002, None),
          RawMember(MemberType.Relation, 11, None),
        ),
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("123:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        networkChanges = NetworkChanges(
          creates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              "network-name",
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
        locationChanges = Seq(
          newLocationChanges(
            routeType = RouteType.hiking,
            locationNames = Seq("nl"),
            nodeChanges = ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1001, "01"),
                newChangeSetElementRef(1002, "02"),
              )
            )
          )
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
        networkName = "network-name",
        changeType = ChangeType.Create,
        country = Some(Country.nl),
        networkDataUpdate = Some(
          NetworkDataUpdate(
            None,
            Some(
              NetworkData(
                MetaData(0, Timestamps.default, 1),
                "network-name"
              )
            )
          )
        ),
        nodes = IdDiffs(
          added = Seq(1001, 1002)
        ),
        relations = IdDiffs(
          added = Seq(11)
        ),
        nodeDiffs = RefDiffs(
          added = Seq(
            Ref(1001, "01"),
            Ref(1002, "02"),
          )
        ),
        routeDiffs = RefDiffs(
          added = Seq(
            Ref(11, "01-02"),

          )
        ),
        happy = true,
        impact = true,
      )
    )
  }

  private def assertRouteChange(): Unit = {
    assertEqual(
      findRouteChangeById("123:1:11"),
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-02",
        addedToNetwork = Seq(Ref(1, "network-name")),
        before = Some(
          newRouteData(
            relationId = 11,
            meta = MetaData(
              version = 0,
              timestamp = Timestamp(2015, 8, 11),
              changeSetId = 1
            ),
            countries = Seq(Country.nl),
            routeTypes = Seq(RouteType.hiking),
            name = "01-02",
            networkNodes = Seq(
              newRouteNode(1001, "01"),
              newRouteNode(1002, "02")
            ),
            tags = Tags.from(
              "network" -> "rwn",
              "type" -> "route",
              "route" -> "foot",
              "ref" -> "01-02",
              "network:type" -> "node_network",
            )
          )
        ),
        after = Some(
          newRouteData(
            relationId = 11,
            meta = MetaData(
              version = 0,
              timestamp = Timestamp(2015, 8, 11),
              changeSetId = 1
            ),
            Seq(Country.nl),
            Seq(RouteType.hiking),
            name = "01-02",
            networkNodes = Seq(
              newRouteNode(1001, "01"),
              newRouteNode(1002, "02")
            ),
            tags = Tags.from(
              "network" -> "rwn",
              "type" -> "route",
              "route" -> "foot",
              "ref" -> "01-02",
              "network:type" -> "node_network",
            )
          )
        ),
        nodeChanges = Seq(
          newRouteNodeChange(
            1001,
          ),
          newRouteNodeChange(
            1002,
          )
        ),
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
        locations = Seq("nl"),
        name = Some("01"),
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        addedToNetwork = Seq(
          Ref(1, "network-name")
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
        locations = Seq("nl"),
        name = Some("02"),
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        addedToNetwork = Seq(
          Ref(1, "network-name")
        ),
        happy = true,
        impact = true
      )
    )
  }
}
