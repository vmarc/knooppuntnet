package kpn.server.api.analysis.pages.route

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteScope
import kpn.api.common.RouteSummary
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.data.MemberType
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteDetailsPageData
import kpn.api.common.route.RouteNodes
import kpn.api.common.route.RouteStructureRow
import kpn.api.common.route.RouteStructureWay
import kpn.api.common.route.WayDirection
import kpn.api.custom.Day
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

object RouteDetailsPageExample {
  val page: RouteDetailsPage = {
    RouteDetailsPage(
      route(),
      networkReferences = Seq(
        Reference(RouteType.cycling, RouteScope.regional, 1, "network one"),
        Reference(RouteType.cycling, RouteScope.regional, 2, "network two")
      ),
      123
    )
  }

  private def route(): RouteDetailsPageData = {

    val nodes = RouteNodes()

    RouteDetailsPageData(
      id = 1,
      active = true,
      summary = RouteSummary(
        id = 1,
        countries = Seq(Country.nl),
        nodeNetwork = true,
        routeTypes = Seq(RouteType.hiking),
        scopes = Seq(RouteScope.regional),
        name = "01-02",
        meters = 1234,
        broken = true,
        inaccessible = true,
        wayCount = 10,
        timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
        tags = Tags.from(
          "key1" -> "value1",
          "key2" -> "value2",
          "key3" -> "value3",
          "key4" -> "value4",
          "key5" -> "value5"
        )
      ),
      proposed = true,
      version = 1,
      changeSetId = 1,
      lastUpdated = Timestamp(2020, 10, 11, 12, 34, 56),
      lastSurvey = Some(Day(2020, 11, 8)),
      facts = Seq(
        Fact.RouteNotContinious,
        Fact.RouteNotForward,
        Fact.RouteNotBackward,
        Fact.RouteUnusedSegments,
        Fact.RouteBroken,
        Fact.RouteIncomplete,
        Fact.RouteInvalidSurveyDate,
        Fact.RouteUnexpectedNode,
        Fact.RouteUnexpectedRelation,
      ),
      locationCandidateInfos = Seq(),
      unexpectedNodeIds = Seq(1001),
      unexpectedRelationIds = Seq(1, 2, 3),
      structureRows = Seq(
        RouteStructureRow(
          id = 1L,
          memberType = MemberType.Node,
          role = Some("connection"),
          link = None,
          distance = 0,
          name = None,
          poi = None,
        ),
        RouteStructureRow(
          id = 1L,
          memberType = MemberType.Way,
          role = None,
          link = None,
          distance = 100,
          name = Some("description"),
          poi = None,
          way = Some(
            RouteStructureWay(
              wayType = Some("path"),
              nodes = Seq.empty,
              from = "01",
              fromNodeId = 1,
              to = "02",
              toNodeId = 2,
              accessible = false,
              nodeCount = "3",
              oneWay = WayDirection.Both,
              oneWayTags = Tags.from(
                "key1" -> "value1",
                "key2" -> "value2",
                "key3" -> "value3"
              ),
            )
          )
        )
      ),
      nameDerivedFromNodes = true,
      nodes,
      None,
      Seq(1L),
      Seq.empty
    )
  }
}
