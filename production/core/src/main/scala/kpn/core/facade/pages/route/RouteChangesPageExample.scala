package kpn.core.facade.pages.route

import kpn.shared.Bounds
import kpn.shared.Fact
import kpn.shared.Timestamp
import kpn.shared.changes.details.ChangeKey
import kpn.shared.changes.filter.ChangesFilter
import kpn.shared.common.Ref
import kpn.shared.data.MetaData
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawNode
import kpn.shared.diff.NodeUpdate
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.WayInfo
import kpn.shared.diff.WayUpdate
import kpn.shared.diff.common.FactDiffs
import kpn.shared.diff.route.RouteDiff
import kpn.shared.diff.route.RouteNameDiff
import kpn.shared.diff.route.RouteNodeDiff
import kpn.shared.diff.route.RouteRoleDiff
import kpn.shared.route.RouteChangeInfo
import kpn.shared.route.RouteChangesPage

object RouteChangesPageExample {

  val page: RouteChangesPage = {
    RouteChangesPage(
      RouteDetailsPageExample.page.route,
      ChangesFilter(Seq()),
      changes(),
      incompleteWarning = true,
      totalCount = 3,
      changeCount = 3
    )
  }

  def changes(): Seq[RouteChangeInfo] = {
    Seq(
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
        before = None, // TODO CHANGE provide some value
        after = None, // TODO CHANGE provide some value

        removedWays = removedWays(),
        addedWays = addedWays(),
        updatedWays = updatedWays(),
        diffs = routeDiff(),
        nodes = Seq[RawNode](
        ),
        changeSetInfo = None, //Some(ChangeSetInfo()),  TODO do we actually show this???
        geometryDiff = None,
        bounds = Bounds(
        ),
        happy = true,
        investigate = true
      )
    )
  }

  private def routeDiff(): RouteDiff = {
    RouteDiff(
      nameDiff = Some(RouteNameDiff("before", "after")),
      roleDiff = Some(RouteRoleDiff(Some("before"), Some("after"))),
      factDiffs = Some(routeFactDiffs()),
      nodeDiffs = routeNodeDiffs(),
      memberOrderChanged = true,
      tagDiffs = Some(routeTagDiffs())
    )
  }

  private def routeFactDiffs(): FactDiffs = {
    FactDiffs(
      resolved = Set(Fact.RouteRedundantNodes),
      introduced = Set(Fact.RouteIncomplete),
      remaining = Set(Fact.RouteUnaccessible)
    )
  }

  private def routeNodeDiffs(): Seq[RouteNodeDiff] = {
    Seq(
      RouteNodeDiff(
        title = "startNodes",
        added = Seq[Ref](
          Ref(1001, "01"),
          Ref(1002, "02"),
          Ref(1003, "03"),
          Ref(1004, "04"),
          Ref(1005, "05")
        ),
        removed = Seq[Ref](
          Ref(1002, "02")
        )
      ),
      RouteNodeDiff(
        title = "endNodes",
        added = Seq[Ref](
          Ref(1001, "01")
        ),
        removed = Seq[Ref](
          Ref(1002, "02")
        )
      ),
      RouteNodeDiff(
        title = "startTentacleNodes",
        added = Seq[Ref](
          Ref(1001, "01")
        ),
        removed = Seq[Ref](
          Ref(1002, "02")
        )
      ),
      RouteNodeDiff(
        title = "endTentacleNodes",
        added = Seq[Ref](
          Ref(1001, "01")
        ),
        removed = Seq[Ref](
          Ref(1002, "02")
        )
      )
    )
  }

  private def routeTagDiffs(): TagDiffs = {
    TagDiffs(
      mainTags = Seq(
        TagDetail(TagDetailType.Same, "same", Some("Same"), Some("Same"))
      ),
      extraTags = Seq(
        TagDetail(TagDetailType.Update, "tag", Some("before"), Some("after"))
      )
    )
  }

  private def removedWays(): Seq[WayInfo] = {
    Seq(
      WayInfo(
        id = 101,
        version = 1,
        changeSetId = 1,
        Timestamp(2020, 10, 11, 12, 34, 56),
        Tags.from(
          "key1" -> "value1",
          "key2" -> "value2"
        )
      )
    )
  }

  private def addedWays(): Seq[WayInfo] = {
    Seq(
      WayInfo(
        id = 101,
        version = 1,
        changeSetId = 1,
        Timestamp(2020, 10, 11, 12, 34, 56),
        Tags.from(
          "key1" -> "value1",
          "key2" -> "value2"
        )
      )
    )
  }

  private def updatedWays(): Seq[WayUpdate] = {
    Seq(
      WayUpdate(
        id = 101,
        before = MetaData(
          version = 1,
          timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
          changeSetId = 1
        ),
        after = MetaData(
          version = 2,
          timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
          changeSetId = 1
        ),
        removedNodes = Seq(
          RawNode(
            id = 1001,
            latitude = "",
            longitude = "",
            version = 1,
            timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
            changeSetId = 2,
            tags = Tags.empty
          )
        ),
        addedNodes = Seq(
          RawNode(
            id = 1001,
            latitude = "",
            longitude = "",
            version = 1,
            timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
            changeSetId = 2,
            tags = Tags.empty
          )
        ),
        updatedNodes = Seq(
          NodeUpdate(
            before = RawNode(
              id = 1001,
              latitude = "",
              longitude = "",
              version = 1,
              timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
              changeSetId = 2,
              tags = Tags.empty
            ),
            after = RawNode(
              id = 1001,
              latitude = "",
              longitude = "",
              version = 1,
              timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
              changeSetId = 2,
              tags = Tags.empty
            ),
            tagDiffs = None, //  Option[TagDiffs]
            nodeMoved = None // Option[NodeMoved]
          )
        ),
        directionReversed = true,
        tagDiffs = None // Option[TagDiffs]
      )
    )
  }
}
