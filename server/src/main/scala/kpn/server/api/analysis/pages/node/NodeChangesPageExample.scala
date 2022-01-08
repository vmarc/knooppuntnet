package kpn.server.api.analysis.pages.node

import kpn.api.common.LatLonImpl
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.common.node.NodeChangeInfo
import kpn.api.common.node.NodeChangesPage
import kpn.api.custom.ChangeType
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

object NodeChangesPageExample {

  val page: NodeChangesPage = {
    NodeChangesPage(
      1L,
      "01 / 02",
      Seq.empty,
      changes(),
      10,
      10
    )
  }

  private def changes(): Seq[NodeChangeInfo] = {
    Seq(
      NodeChangeInfo(
        0,
        1,
        Some(1),
        ChangeKey(
          replicationNumber = 1,
          timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
          changeSetId = 1,
          elementId = 1
        ),
        ChangeType.Update,
        changeTags = Tags.from(
          "created_by" -> "JOSM",
          "source" -> "survey"
        ),
        comment = Some("this is the comment"),
        before = Some(
          MetaData(
            version = 1,
            timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
            changeSetId = 111
          )
        ),
        after = Some(
          MetaData(
            version = 2,
            timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
            changeSetId = 222
          )
        ),
        connectionChanges = Seq(
          RefBooleanChange(
            ref = Ref(1, "network one"),
            after = true
          ),
          RefBooleanChange(
            ref = Ref(2, "network two"),
            after = false
          )
        ),
        roleConnectionChanges = Seq(
          RefBooleanChange(
            ref = Ref(1, "network one"),
            after = false
          ),
          RefBooleanChange(
            ref = Ref(2, "network two"),
            after = true
          )
        ),
        definedInNetworkChanges = Seq(
          RefBooleanChange(
            ref = Ref(2, "network two"),
            after = true
          )
        ),
        tagDiffs = Some(
          TagDiffs(
            mainTags = Seq(
              TagDetail(TagDetailType.Add, "add", None, Some("added")),
              TagDetail(TagDetailType.Update, "rwn_ref", Some("01"), Some("02")),
              TagDetail(TagDetailType.Delete, "delete", Some("deleted"), None),
              TagDetail(TagDetailType.Same, "same", Some("value"), Some("value"))
            ),
            extraTags = Seq(
              TagDetail(TagDetailType.Add, "add", None, Some("added")),
              TagDetail(TagDetailType.Update, "rwn_ref", Some("01"), Some("02")),
              TagDetail(TagDetailType.Delete, "delete", Some("deleted"), None),
              TagDetail(TagDetailType.Same, "same", Some("value"), Some("value"))
            )
          )
        ),
        nodeMoved = Some(
          NodeMoved(
            before = LatLonImpl("51.5291500", "4.297700"),
            after = LatLonImpl("51.5291600", "4.297800"),
            distance = 7
          )
        ),
        addedToRoute = Seq(
          Ref(1, "route one"),
          Ref(1, "route two")
        ),
        removedFromRoute = Seq(
          Ref(1, "route three"),
          Ref(1, "route four")
        ),
        addedToNetwork = Seq(
          Ref(1, "network one"),
          Ref(1, "network two")
        ),
        removedFromNetwork = Seq(
          Ref(1, "network three"),
          Ref(1, "network four")
        ),
        factDiffs = Some(
          FactDiffs(
            resolved = Seq(Fact.NetworkExtraMemberNode),
            introduced = Seq(Fact.IntegrityCheckFailed),
            remaining = Seq(Fact.IntegrityCheck)
          )
        ),
        facts = Seq.empty,
        initialTags = Some(
          Tags.from(
            "a" -> "1",
            "b" -> "2",
            "c" -> "3",
          )
        ),
        initialLatLon = Some(
          LatLonImpl(
            latitude = "51.5291600",
            longitude = "4.297800"
          )
        ),
        happy = true,
        investigate = true
      )
    )
  }
}
