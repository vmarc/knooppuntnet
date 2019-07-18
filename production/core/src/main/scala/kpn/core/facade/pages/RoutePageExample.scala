package kpn.core.facade.pages

import kpn.shared.Bounds
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.RouteSummary
import kpn.shared.Timestamp
import kpn.shared.changes.details.ChangeKey
import kpn.shared.common.Ref
import kpn.shared.common.Reference
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawNode
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.common.FactDiffs
import kpn.shared.diff.route.RouteDiff
import kpn.shared.diff.route.RouteNameDiff
import kpn.shared.diff.route.RouteNodeDiff
import kpn.shared.diff.route.RouteRoleDiff
import kpn.shared.route.Both
import kpn.shared.route.RouteChangeInfo
import kpn.shared.route.RouteChangeInfos
import kpn.shared.route.RouteChangesPage
import kpn.shared.route.RouteDetailsPage
import kpn.shared.route.RouteInfo
import kpn.shared.route.RouteInfoAnalysis
import kpn.shared.route.RouteMap
import kpn.shared.route.RouteMapPage
import kpn.shared.route.RouteMemberInfo
import kpn.shared.route.RouteNetworkNodeInfo
import kpn.shared.route.RoutePage
import kpn.shared.route.RouteReferences

object RoutePageExample {

  private val route: RouteInfo = RouteInfo(
    summary = RouteSummary(
      id = 1,
      country = Some(Country.nl),
      networkType = NetworkType.hiking,
      name = "01-02",
      meters = 1234,
      isBroken = true,
      wayCount = 10,
      timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
      nodeNames = Seq(
        "01",
        "02"
      ),
      tags = Tags.from(
        "key1" -> "value1",
        "key2" -> "value2",
        "key3" -> "value3",
        "key4" -> "value4",
        "key5" -> "value5"
      )
    ),
    active = false,
    display = false,
    ignored = false,
    orphan = true,
    version = 1,
    changeSetId = 1,
    lastUpdated = Timestamp(2020, 10, 11, 12, 34, 56),
    tags = Tags.from(
      "key1" -> "value1",
      "key2" -> "value2",
      "key3" -> "value3",
      "key4" -> "value4",
      "key5" -> "value5"
    ),
    facts = Seq(
      Fact.RouteNotContinious,
      Fact.RouteNotForward,
      Fact.RouteNotBackward,
      Fact.RouteUnusedSegments,
      Fact.RouteBroken,
      Fact.RouteIncomplete
    ),
    analysis = Some(
      RouteInfoAnalysis(
        startNodes = Seq[RouteNetworkNodeInfo](
          RouteNetworkNodeInfo(
            1001,
            "01",
            "01.a",
            "1",
            "1"
          ),
          RouteNetworkNodeInfo(
            1002,
            "02",
            "02.a",
            "2",
            "2"
          )
        ),
        endNodes = Seq[RouteNetworkNodeInfo](
          RouteNetworkNodeInfo(
            1001,
            "01",
            "01.a",
            "1",
            "1"
          ),
          RouteNetworkNodeInfo(
            1002,
            "02",
            "02.a",
            "2",
            "2"
          )
        ),
        startTentacleNodes = Seq[RouteNetworkNodeInfo](
          RouteNetworkNodeInfo(
            1001,
            "01",
            "01.a",
            "2",
            "2"
          ),
          RouteNetworkNodeInfo(
            1002,
            "02",
            "02.a",
            "2",
            "2"
          )
        ),
        endTentacleNodes = Seq[RouteNetworkNodeInfo](
          RouteNetworkNodeInfo(
            1001,
            "01",
            "01.a",
            "1",
            "1"
          ),
          RouteNetworkNodeInfo(
            1002,
            "02",
            "02.a",
            "2",
            "2"
          )
        ),
        unexpectedNodeIds = Seq(
          1001
        ),
        members = Seq(

          RouteMemberInfo(
            id = 1L,
            memberType = "node",
            isWay = false,
            nodes = Seq(),
            linkName = "n",
            from = "01",
            fromNodeId = 1,
            to = "",
            toNodeId = 1,
            role = "connection",
            timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
            isAccessible = true,
            length = "",
            nodeCount = "",
            description = "",
            oneWay = Both,
            oneWayTags = Tags.empty
          ),
          RouteMemberInfo(
            id = 1L,
            memberType = "way",
            isWay = true,
            nodes = Seq(),
            linkName = "wb003",
            from = "01",
            fromNodeId = 1,
            to = "02",
            toNodeId = 2,
            role = "",
            timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
            isAccessible = false,
            length = "",
            nodeCount = "3",
            description = "description",
            oneWay = Both,
            oneWayTags = Tags.from(
              "key1" -> "value1",
              "key2" -> "value2",
              "key3" -> "value3"
            )
          )
        ),
        expectedName = "01-02",
        map = RouteMap(
          redundantNodes = Seq(
            RouteNetworkNodeInfo(
              1009,
              "09",
              "09",
              "9",
              "9"
            )
          )
        ),
        structureStrings = Seq[String](
          "one",
          "two",
          "three"
        )
      )
    )
  )

  private val routeReferences: RouteReferences = RouteReferences(
    networkReferences = Seq(
      Reference(1, "network one", NetworkType.bicycle),
      Reference(2, "network two", NetworkType.hiking, connection = true)
    )
  )

  private val changes: Seq[RouteChangeInfo] = Seq(
    RouteChangeInfo(
      id = 1,
      version = 1,
      changeKey = ChangeKey(
        replicationNumber = 1,
        timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
        changeSetId = 1,
        elementId = 1
      ),
      comment = Some("comment"),
      None, // TODO CHANGE provide some value
      None, // TODO CHANGE provide some value
      Seq(), // TODO CHANGE provide some value
      Seq(), // TODO CHANGE provide some value
      Seq(), // TODO CHANGE provide some value
      diffs = RouteDiff(
        nameDiff = Some(
          RouteNameDiff(
            "before",
            "after"
          )
        ),
        roleDiff = Some(
          RouteRoleDiff(
            Some("before"),
            Some("after")
          )
        ),
        factDiffs = Some(
          FactDiffs(
            resolved = Set(Fact.RouteRedundantNodes),
            introduced = Set(Fact.RouteIncomplete),
            remaining = Set(Fact.RouteEndNodeMismatch)
          )
        ),
        nodeDiffs = Seq(
          RouteNodeDiff(
            title = "title",
            added = Seq[Ref](
              Ref(1001, "01")
            ),
            removed = Seq[Ref](
              Ref(1002, "02")
            )
          )
        ),
        memberOrderChanged = true,
        tagDiffs = Some(
          TagDiffs()
        )
      ),
      nodes = Seq[RawNode](
      ),
      changeSetInfo = None, //Some(ChangeSetInfo()),  TODO do we actually show this???
      addedNodes = Seq[Long](1001),
      deletedNodes = Seq[Long](1002),
      commonNodes = Seq[Long](1003),
      addedWayIds = Seq[Long](101),
      deletedWayIds = Seq[Long](102),
      commonWayIds = Seq[Long](103),
      geometryDiff = None,
      bounds = Bounds(
      ),
      happy = true,
      investigate = true
    )
  )

  private val incompleteWarning: Boolean = true

  private val totalCount: Int = 3

  val detailsPage: RouteDetailsPage = RouteDetailsPage(route, routeReferences)

  val mapPage: RouteMapPage = RouteMapPage(route)

  val changesPage: RouteChangesPage = RouteChangesPage(route, changes, incompleteWarning, totalCount)

  val page: RoutePage = RoutePage(route, routeReferences, routeChangeInfos = RouteChangeInfos(changes, incompleteWarning = true))
}
