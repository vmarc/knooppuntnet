package kpn.server.api.analysis.pages.node

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.LocationInfo
import kpn.api.common.NodeInfo
import kpn.api.common.NodeName
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.node.NodeDetailsPage
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.custom.Day
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
        NodeName(RouteType.cycling, RouteScope.regional, "01", None, proposed = false),
        NodeName(RouteType.hiking, RouteScope.regional, "02", None, proposed = true)
      ),
      latitude = "51.5291600",
      longitude = "4.297800",
      lastUpdated = Timestamp(2020, 10, 11, 12, 34, 56),
      lastSurvey = Some(Day(2020, 11, 8)),
      tags = Tags.from(
        "rwn_ref" -> "01",
        "rcn_ref" -> "02",
        "expected_rwn_route_relations" -> "3",
        "note" -> "this is a test network node for trying out the node page"
      ),
      facts = Seq(
        Fact.NodeInvalidSurveyDate,
        Fact.Deleted
      ),
      locations = Seq(
        LocationInfo("NL", "TODO"),
        LocationInfo("North Brabant", "TODO"),
        LocationInfo("Roosendaal", "TODO")
      ),
      tiles = Seq.empty,
      integrity = None, // TODO MONGO move setup from nodeDetailsPage method to here
      routeReferences = Seq.empty // TODO MONGO move setup from nodeDetailsPage method to here
    )
  }
  val page: NodeDetailsPage = {
    NodeDetailsPage(
      nodeInfo,
      mixedRouteScopes = true,
      Seq(
        Reference(RouteType.cycling, RouteScope.regional, 101, "01-02", None),
        Reference(RouteType.cycling, RouteScope.regional, 102, "02-03", None),
        Reference(RouteType.cycling, RouteScope.local, 103, "03-04", None),
        Reference(RouteType.hiking, RouteScope.regional, 104, "05-06", None)
      ),
      Seq(
        Reference(RouteType.hiking, RouteScope.regional, 1, "network one", None),
        Reference(RouteType.hiking, RouteScope.regional, 2, "network two", None),
        Reference(RouteType.hiking, RouteScope.local, 3, "network three", None)
      ),
      Some(
        NodeIntegrity(
          Seq(
            NodeIntegrityDetail(
              RouteType.cycling,
              RouteScope.regional,
              3,
              Seq(
                Ref(101L, "01-02"),
                Ref(102L, "02-03")
              )
            ),
            NodeIntegrityDetail(
              RouteType.cycling,
              RouteScope.local,
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
