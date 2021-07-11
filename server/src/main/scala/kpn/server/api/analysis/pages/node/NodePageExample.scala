package kpn.server.api.analysis.pages.node

import kpn.api.common.LatLonImpl
import kpn.api.common.NodeInfo
import kpn.api.common.NodeMapInfo
import kpn.api.common.NodeName
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.changes.filter.ChangesFilter
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.common.location.Location
import kpn.api.common.node.NodeChangeInfo
import kpn.api.common.node.NodeChangesPage
import kpn.api.common.node.NodeDetailsPage
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.common.node.NodeMapPage
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

object NodePageExample {

  private val nodeInfo: NodeInfo = {
    NodeInfo(
      _id = 1,
      id = 1,
      active = false,
      orphan = true,
      country = Some(Country.nl),
      name = "01 / 02",
      names = Seq(
        NodeName(NetworkType.cycling, NetworkScope.regional, "01", None, proposed = false),
        NodeName(NetworkType.hiking, NetworkScope.regional, "02", None, proposed = true)
      ),
      latitude = "51.5291600",
      longitude = "4.297800",
      lastUpdated = Timestamp(2020, 10, 11, 12, 34, 56),
      lastSurvey = Some(Day(2020, 11, Some(8))),
      tags = Tags.from(
        "rwn_ref" -> "01",
        "rcn_ref" -> "02",
        "expected_rwn_route_relations" -> "3",
        "note" -> "this is a test network node for trying out the node page"
      ),
      facts = Seq(
        Fact.NodeInvalidSurveyDate,
        Fact.WasOrphan,
        Fact.Deleted
      ),
      location = Some(Location(Seq("NL", "North Brabant", "Roosendaal"))),
      Seq.empty
    )
  }

  val nodeDetailsPage: NodeDetailsPage = {
    NodeDetailsPage(
      nodeInfo,
      mixedNetworkScopes = true,
      Seq(
        Reference(NetworkType.cycling, NetworkScope.regional, 101, "01-02"),
        Reference(NetworkType.cycling, NetworkScope.regional, 102, "02-03"),
        Reference(NetworkType.cycling, NetworkScope.local, 103, "03-04"),
        Reference(NetworkType.hiking, NetworkScope.regional, 104, "05-06")
      ),
      Seq(
        Reference(NetworkType.hiking, NetworkScope.regional, 1, "network one"),
        Reference(NetworkType.hiking, NetworkScope.regional, 2, "network two"),
        Reference(NetworkType.hiking, NetworkScope.local, 3, "network three")
      ),
      Some(
        NodeIntegrity(
          Seq(
            NodeIntegrityDetail(
              NetworkType.cycling,
              NetworkScope.regional,
              3,
              Seq(
                Ref(101L, "01-02"),
                Ref(102L, "02-03")
              )
            ),
            NodeIntegrityDetail(
              NetworkType.cycling,
              NetworkScope.local,
              2,
              Seq(
                Ref(103L, "03-04")
              )
            )
          )
        )
      ),
      123
    )
  }

  val nodeMapPage: NodeMapPage = {
    NodeMapPage(
      NodeMapInfo(
        id = nodeInfo._id,
        name = nodeInfo.name,
        networkTypes = Seq(
          NetworkType.cycling,
          NetworkType.hiking
        ),
        latitude = "51.5291600",
        longitude = "4.297800",
      ),
      123
    )
  }

  val nodeChangesPage: NodeChangesPage = {
    NodeChangesPage(
      nodeInfo.id,
      nodeInfo.name,
      ChangesFilter(Seq.empty),
      changes(),
      incompleteWarning = true,
      10,
      10
    )
  }


  private def changes(): Seq[NodeChangeInfo] = {
    Seq(
      NodeChangeInfo(
        1,
        Some(1),
        ChangeKey(
          replicationNumber = 1,
          timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
          changeSetId = 1,
          elementId = 1
        ),
        changeTags = Tags.from(
          "created_by" -> "JOSM",
          "source" -> "survey"
        ),
        comment = Some("this is the comment"),
        None, // TODO CHANGE provide some value
        None, // TODO CHANGE provide some value
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
        factDiffs = FactDiffs(
          resolved = Set(Fact.NetworkExtraMemberNode),
          introduced = Set(Fact.IntegrityCheckFailed),
          remaining = Set(Fact.IntegrityCheck)
        ),
        facts = Seq(
          Fact.WasOrphan
        ),
        happy = true,
        investigate = true
      )
    )
  }
}
