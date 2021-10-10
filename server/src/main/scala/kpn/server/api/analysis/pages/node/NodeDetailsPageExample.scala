package kpn.server.api.analysis.pages.node

import kpn.api.common.NodeInfo
import kpn.api.common.NodeName
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.node.NodeDetailsPage
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

object NodeDetailsPageExample {

  private val nodeInfo: NodeInfo = {
    NodeInfo(
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
      locations = Seq("NL", "North Brabant", "Roosendaal"),
      tiles = Seq.empty,
      integrity = None, // TODO MONGO move setup from nodeDetailsPage method to here
      routeReferences = Seq.empty // TODO MONGO move setup from nodeDetailsPage method to here
    )
  }

  val page: NodeDetailsPage = {
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
}
