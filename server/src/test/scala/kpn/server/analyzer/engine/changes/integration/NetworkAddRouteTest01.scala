package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.Bounds
import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.ElementChangeType
import kpn.api.common.LatLonImpl
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.data.MemberType
import kpn.api.common.data.MetaData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.route.GeometryDiff
import kpn.api.common.route.WayLine
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newBaseRouteChange
import kpn.core.test.TestObjects.newChange
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetNetwork
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newNetworkBaseData
import kpn.core.test.TestObjects.newNetworkChange
import kpn.core.test.TestObjects.newNetworkDetail
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newNetworkInfoNodeDetail
import kpn.core.test.TestObjects.newNetworkRouteDetail
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.test.TestObjects.newRaw
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteData
import kpn.core.test.TestObjects.newRouteNode
import kpn.core.test.TestObjects.newRouteNodeChange
import kpn.core.test.TestObjects.newRouteTags
import kpn.core.test.TestObjects.newWayGeometryUpdate
import kpn.core.test.TestObjects.newWayInfo
import kpn.core.test.Timestamps

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
      .way(101, 1001, 1002) // create
      .route(11, "01-02", Seq(newMember(MemberType.Way, 101))) // create
      .networkRelation( // update
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

      process(
        Seq(
          newChange(
            ChangeAction.Create,
            ways = Seq(
              dataAfter.rawWayWithId(101),
            ),
            relations = Seq(
              dataAfter.rawRelationWithId(11)
            )
          ),
          newChange(
            ChangeAction.Modify,
            relations = Seq(
              dataAfter.rawRelationWithId(1)
            )
          ),
        )
      )

      assertNetworkDoc()
      assertNetworkChange()
      assertBaseRouteChange()
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
        base = newNetworkBaseData(
          raw = newRaw(
            version = 2,
            tags = Tags.from(
              "network:type" -> "node_network",
              "type" -> "network",
              "network" -> "rwn",
              "name" -> "network",
            ),
          ),
          name = Some("network"),
          members = Seq(
            RawMember(MemberType.Node, 1001, None),
            RawMember(MemberType.Node, 1002, None),
            RawMember(MemberType.Relation, 11, None),
          )
        ),
        nodeCount = 2,
        routeCount = 1,
        country = Some(Country.nl),
        detail = newNetworkDetail(
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
        )
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("1:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = Some("network"),
        changeType = ChangeType.Update,
        country = Some(Country.nl),
        routeType = RouteType.hiking,
        networkDataUpdate = Some(
          NetworkDataUpdate(
            Some(
              NetworkData(
                MetaData(1, Timestamps.default, 1),
                Some("network")
              )
            ),
            Some(
              NetworkData(
                MetaData(2, Timestamps.default, 1),
                Some("network")
              )
            )
          )
        ),
        relations = IdDiffs(
          added = Seq(11)
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
        happy = true,
        impact = true,
      )
    )
  }

  private def assertBaseRouteChange(): Unit = {
    assertEqual(
      findBaseRouteChangeById("1:1:11"),
      newBaseRouteChange(
        "1:1:11",
        newChangeKey(elementId = 11),
        ChangeType.Create,
        wayDiffs = Some(
          WayDiffsInfo(
            added = Seq(
              newWayInfo(
                id = 101,
                tags = Tags.from(
                  "highway" -> "unclassified"
                )
              )
            )
          )
        ),
        geometryDiff = Some(
          GeometryDiff(
            common = Seq.empty,
            update = Seq(
              newWayGeometryUpdate(
                wayId = 101,
                added = Some(
                  Seq(WayLine(2, 0, "[[0.0,0.0],[0,0]]"))
                )
              )
            )
          )
        ),
        bounds = Some(
          Bounds()
        )
      )
    )
  }

  private def assertRouteChange(): Unit = {

    val routeData = newRouteData(
      relationId = 11,
      raw = newRaw(
        tags = newRouteTags("01-02")
      ),
      countries = Seq(Country.nl),
      routeTypes = Seq(RouteType.hiking),
      name = "01-02",
      networkNodes = Seq(
        newRouteNode(1001, "01"),
        newRouteNode(1002, "02")
      )
    )

    assertEqual(
      findRouteChangeById("1:1:11"),
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Create,
        "01-02",
        addedToNetwork = Seq(Ref(1, "network")),
        before = None,
        after = Some(routeData),
        nodeChanges = Seq(
          newRouteNodeChange(
            1001,
            changeType = ElementChangeType.Added
          ),
          newRouteNodeChange(
            1002,
            changeType = ElementChangeType.Added
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
      findNodeChangeById("1:1:1001"),
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
      findNodeChangeById("1:1:1002"),
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
      findChangeSetSummaryById("1:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        networkChanges = NetworkChanges(
          updates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              Some("network"),
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
        locationChanges = Seq(
          newLocationChanges(
            routeType = RouteType.hiking,
            locationNames = Seq("nl"),
            nodeChanges = ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1001, "01", happy = true),
                newChangeSetElementRef(1002, "02", happy = true),
              )
            ),
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
        "01-02",
        None
      )
    )
    context.nodeRepository.nodeRouteReferences(1001) should equal(nodeRouteReferences)
    context.nodeRepository.nodeRouteReferences(1002) should equal(nodeRouteReferences)
  }
}
