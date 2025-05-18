package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.Bounds
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteMemberInfo
import kpn.api.common.RouteMemberInfoWay
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.data.MetaData
import kpn.api.common.data.WayMember
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.route.RouteEdge
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.common.route.RouteNodes
import kpn.api.common.route.RoutePath
import kpn.api.common.route.RouteSegment
import kpn.api.common.route.RouteStructureRow
import kpn.api.common.route.RouteStructureWay
import kpn.api.common.route.WayDirection
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.BaseRoutePath
import kpn.core.doc.BaseRouteSegment
import kpn.core.doc.BaseRouteSegmentElement
import kpn.core.doc.Label
import kpn.core.test.OverpassData
import kpn.server.analyzer.engine.context.ElementIds

class NetworkUpdateRouteTest01 extends IntegrationTest {

  test("network update - route that is no longer part of the network after update, becomes orphan route if also not referenced in any other network") {

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
        "name",
        Seq(
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002),
          newMember(MemberType.Relation, 11)
        ),
        version = 1
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route( // route still exists
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
          newMember(MemberType.Node, 1002)
          // route member is no longer included here
        ),
        version = 2
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      watched.routes.ids should contain(11)

      assertBaseRoute()
      assertRoute()

      assertOrphanRoute()
      assertNetworkChange()
      assertRouteChange()
      assertChangeSetSummary()

      database.nodeChanges shouldBe empty
    }
  }

  private def assertBaseRoute(): Unit = {
    assertEqual(
      findBaseRouteById(11).copy(geometryDigest = "", tiles = Seq.empty),
      newBaseRouteDoc(
        newRouteSummary(
          11,
          name = "01-02",
          countries = Seq(Country.nl),
          wayCount = 1,
          tags = newRouteTags("01-02")
        ),
        labels = Seq(
          Label.active,
          Label.country(Country.nl),
          Label.routeType(RouteType.hiking),
          Label.scope(RouteScope.regional),
        ),
        members = Seq(
          RouteMemberInfo(
            id = 101,
            memberType = MemberType.Way,
            role = None,
            name = None,
            poi = None,
            way = Some(
              RouteMemberInfoWay(
                wayType = Some("unclassified"),
                nodes = Seq(
                  RouteNetworkNodeInfo(1001, "01", "01", None, "0", "0"),
                  RouteNetworkNodeInfo(1002, "02", "02", None, "0", "0")
                ),
                timestamp = Timestamp(2015, 8, 11, 0, 0, 0),
                accessible = true,
                distance = 0,
                nodeCount = "2",
                oneWay = WayDirection.Both,
                oneWayTags = Seq.empty,
                link = newLink() // "wn000"
              )
            )
          )
        ),
        nodes = RouteNodes(
          startNode = Some(newRouteNode(1001, "01")),
          endNode = Some(newRouteNode(1002, "02")),
        ),
        analysis = newRouteInfoAnalysis(
          expectedName = "01-02",
        ),
        nodeRefs = Seq(
          1001,
          1002
        ),
        elementIds = ElementIds(
          nodeIds = Set(1001, 1002),
          wayIds = Set(101)
        ),
        edges = Seq(
          RouteEdge(1, 1001, 1002, 0),
          RouteEdge(2, 1002, 1001, 0),
        ),
        segments = Seq(
          BaseRouteSegment(
            id = 1,
            startNodeId = 1001,
            endNodeId = 1002,
            meters = 0,
            bounds = Bounds(),
            elementIds = Seq(1)
          )
        ),
        segmentElements = Seq(
          BaseRouteSegmentElement(
            segmentId = 1,
            segmentElementId = 1,
            surface = "paved",
            coordinates = "[[0,0],[0,0]]"
          )
        ),
        paths = Seq(
          BaseRoutePath(
            id = 1,
            name = "forward",
            elementIds = Seq(1),
          ),
          BaseRoutePath(
            id = 2,
            name = "backward",
            elementIds = Seq(1),
          ),
        ),
        relation = Some(
          newRelation(
            11,
            members = Seq(
              WayMember(
                newWay(
                  101,
                  nodes = Vector(
                    newNodeWithName(1001, "01"),
                    newNodeWithName(1002, "02"),
                  ),
                  tags = Tags.from(
                    "highway" -> "unclassified"
                  )
                ),
                None
              ),
            ),
            tags = Tags.from(
              "network" -> "rwn",
              "type" -> "route",
              "route" -> "foot",
              "ref" -> "01-02",
              "network:type" -> "node_network"
            )
          )
        ),
        bounds = Some(Bounds())
      )
    )
  }

  private def assertRoute(): Unit = {
    val baseRouteDoc = findBaseRouteById(11)

    assertEqual(
      findRouteById(11),
      newRouteDoc(
        newRouteSummary(
          11,
          name = "01-02",
          countries = Seq(Country.nl),
          wayCount = 1,
          tags = newRouteTags("01-02")
        ),
        labels = Seq(
          Label.active,
          Label.country(Country.nl),
          Label.routeType(RouteType.hiking),
          Label.scope(RouteScope.regional),
        ),
        members = Seq(
          RouteMemberInfo(
            101,
            MemberType.Way,
            None,
            None,
            None,
            Some(
              RouteMemberInfoWay(
                Some("unclassified"),
                Seq(
                  RouteNetworkNodeInfo(1001, "01", "01", None, "0", "0"),
                  RouteNetworkNodeInfo(1002, "02", "02", None, "0", "0")
                ),
                Timestamp(2015, 8, 11, 0, 0, 0),
                accessible = true,
                0,
                "2",
                WayDirection.Both,
                Seq.empty,
                newLink() // "wn000"
              )
            )
          )
        ),
        nodes = RouteNodes(
          startNode = Some(newRouteNode(1001, "01")),
          endNode = Some(newRouteNode(1002, "02")),
        ),
        analysis = newRouteInfoAnalysis(
          expectedName = "01-02",
        ),
        segments = Seq(
          RouteSegment(
            id = 1,
            startNodeId = 1001,
            endNodeId = 1002,
            meters = 0,
            bounds = Bounds(),
            elementIds = Seq(1)
          )
        ),
        paths = Seq(
          RoutePath(
            id = 1,
            name = "forward",
            elementIds = Seq(1),
          ),
          RoutePath(
            id = 2,
            name = "backward",
            elementIds = Seq(1),
          )
        ),
        routeIds = Seq(11),
        bounds = Some(Bounds()),
        structureRows = Seq(
          RouteStructureRow(
            id = 101,
            memberType = MemberType.Way,
            role = None,
            link = Some(
              newLink(

              )
            ),
            distance = 0,
            name = None,
            poi = None,
            way = Some(
              RouteStructureWay(
                wayType = Some("unclassified"),
                nodes = Seq(
                  newRouteNetworkNodeInfo(
                    id = 1001,
                    name = "01",
                    alternateName = "01",
                  ),
                  newRouteNetworkNodeInfo(
                    id = 1002,
                    name = "02",
                    alternateName = "02",
                  ),
                ),
                accessible = true,
                nodeCount = "2",
                oneWay = WayDirection.Both,
                oneWayTags = Seq.empty,
              )
            ),
            relation = None,
          ),
        ),
        edges = Seq(
          RouteEdge(1, 1001, 1002, 0),
          RouteEdge(2, 1002, 1001, 0),
        ),
      )
    )
  }

  private def assertOrphanRoute(): Unit = {
    assertEqual(
      findOrphanRouteById(11),
      newOrphanRouteDoc(
        11L,
        Country.nl,
        RouteType.hiking,
        "01-02"
      )
    )
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("123:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = "name",
        changeType = ChangeType.Update,
        country = Some(Country.nl),
        routeType = RouteType.hiking,
        networkDataUpdate = Some(
          NetworkDataUpdate(
            Some(
              NetworkData(
                MetaData(1, defaultTimestamp, 1),
                name = "name"
              )
            ),
            Some(
              NetworkData(
                MetaData(2, defaultTimestamp, 1),
                name = "name"
              )
            ),
          )
        ),
        relations = IdDiffs(
          removed = Seq(11)
        ),
        routeDiffs = RefDiffs(
          removed = Seq(
            Ref(11, "01-02")
          )
        ),
        investigate = true,
        impact = true,
      )
    )
  }

  private def assertRouteChange(): Unit = {

    val routeData = newRouteData(
      relationId = 11,
      meta = newMetaData(changeSetId = 1),
      countries = Seq(Country.nl),
      routeTypes = Seq(RouteType.hiking),
      name = "01-02",
      networkNodes = Seq(
        newRouteNode(1001, "01"),
        newRouteNode(1002, "02")
      ),
      tags = newRouteTags("01-02")
    )

    assertEqual(
      findRouteChangeById("123:1:11"),
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-02",
        removedFromNetwork = Seq(Ref(1, "name")),
        before = Some(routeData),
        after = Some(routeData),
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
          updates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              "name",
              routeChanges = ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(11, "01-02", investigate = true)
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
