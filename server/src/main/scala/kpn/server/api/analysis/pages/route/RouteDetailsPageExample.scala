package kpn.server.api.analysis.pages.route

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkScope
import kpn.api.common.NetworkType
import kpn.api.common.RouteSummary
import kpn.api.common.common.Reference
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteDetailsPageData
import kpn.api.common.route.RouteNodes
import kpn.api.common.route.RouteStructureRow
import kpn.api.common.route.WayDirection
import kpn.api.custom.Day
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

object RouteDetailsPageExample {
  val page: RouteDetailsPage = {
    RouteDetailsPage(
      route(),
      networkReferences = Seq(
        Reference(NetworkType.cycling, NetworkScope.regional, 1, "network one"),
        Reference(NetworkType.cycling, NetworkScope.regional, 2, "network two")
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
        networkTypes = Seq(NetworkType.hiking),
        scopes = Seq("regional"),
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
          memberType = "node",
          isWay = false,
          nodes = Seq.empty,
          linkName = "n",
          from = "01",
          fromNodeId = 1,
          to = "",
          toNodeId = 1,
          role = "connection",
          timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
          accessible = true,
          length = "",
          nodeCount = "",
          description = "",
          oneWay = WayDirection.Both,
          oneWayTags = Seq.empty,

          level = 1,
          physical = false,
          name = "",
          relationId = 1,
          subRelationIndex = None,
          // role: Option[String],
          survey = None,
          symbol = None,
          osmSegmentCount = None,
          osmDistance = 0,
          gaps = None,
          happy = false
        ),
        RouteStructureRow(
          id = 1L,
          memberType = "way",
          isWay = true,
          nodes = Seq.empty,
          linkName = "wb003",
          from = "01",
          fromNodeId = 1,
          to = "02",
          toNodeId = 2,
          role = "",
          timestamp = Timestamp(2020, 10, 11, 12, 34, 56),
          accessible = false,
          length = "",
          nodeCount = "3",
          description = "description",
          oneWay = WayDirection.Both,
          oneWayTags = Tags.from(
            "key1" -> "value1",
            "key2" -> "value2",
            "key3" -> "value3"
          ),

          level = 1,
          physical = false,
          name = "",
          relationId = 1,
          subRelationIndex = None,
          // role: Option[String],
          survey = None,
          symbol = None,
          osmSegmentCount = None,
          osmDistance = 0,
          gaps = None,
          happy = false
        )
      ),
      nameDerivedFromNodes = true,
      nodes,
      None,
      Seq(1L)
    )
  }
}
