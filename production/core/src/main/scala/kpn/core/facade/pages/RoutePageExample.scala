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
import kpn.shared.route.RouteChangeInfo
import kpn.shared.route.RouteChangeInfos
import kpn.shared.route.RouteInfo
import kpn.shared.route.RouteInfoAnalysis
import kpn.shared.route.RouteMap
import kpn.shared.route.RouteMemberInfo
import kpn.shared.route.RouteNetworkNodeInfo
import kpn.shared.route.RoutePage
import kpn.shared.route.RouteReferences

object RoutePageExample {

  val page: RoutePage = {
    RoutePage(
      route = RouteInfo(
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
        ignored = true,
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
          Fact.RouteUnusedSegments
        ),
        analysis = Some(
          RouteInfoAnalysis(
            startNodes = Seq[RouteNetworkNodeInfo](
              RouteNetworkNodeInfo(
                1001,
                "01",
                "01.a",
                "lat",
                "lon"
              ),
              RouteNetworkNodeInfo(
                1002,
                "02",
                "02.a",
                "lat",
                "lon"
              )
            ),
            endNodes = Seq[RouteNetworkNodeInfo](
              RouteNetworkNodeInfo(
                1001,
                "01",
                "01.a",
                "lat",
                "lon"
              ),
              RouteNetworkNodeInfo(
                1002,
                "02",
                "02.a",
                "lat",
                "lon"
              )
            ),
            startTentacleNodes = Seq[RouteNetworkNodeInfo](
              RouteNetworkNodeInfo(
                1001,
                "01",
                "01.a",
                "lat",
                "lon"
              ),
              RouteNetworkNodeInfo(
                1002,
                "02",
                "02.a",
                "lat",
                "lon"
              )
            ),
            endTentacleNodes = Seq[RouteNetworkNodeInfo](
              RouteNetworkNodeInfo(
                1001,
                "01",
                "01.a",
                "lat",
                "lon"
              ),
              RouteNetworkNodeInfo(
                1002,
                "02",
                "02.a",
                "lat",
                "lon"
              )
            ),
            unexpectedNodeIds = Seq(
              1001
            ),
            members = Seq[RouteMemberInfo](
            ),
            expectedName = "01-02",
            map = RouteMap(
            ),
            structureStrings = Seq[String](
              "one",
              "two",
              "three"
            )
          )
        )
      ),
      references = RouteReferences(
        networkReferences = Seq(
          Reference(1, "network one", NetworkType.bicycle),
          Reference(2, "network two", NetworkType.hiking, connection = true)
        )
      ),
      routeChangeInfos = RouteChangeInfos(
        changes = Seq(
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
        ),
        incompleteWarning = true
      )
    )
  }
}
