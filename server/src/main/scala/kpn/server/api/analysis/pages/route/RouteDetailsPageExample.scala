package kpn.server.api.analysis.pages.route

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.RouteSummary
import kpn.api.common.common.Reference
import kpn.api.common.route.Both
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteDetailsPageData
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteMap
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.RouteMemberInfo
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
    val analysis = RouteInfoAnalysis(
      unexpectedNodeIds = Seq(
        1001
      ),
      members = Seq(

        RouteMemberInfo(
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
          oneWay = Both,
          oneWayTags = Tags.empty
        ),
        RouteMemberInfo(
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
          oneWay = Both,
          oneWayTags = Tags.from(
            "key1" -> "value1",
            "key2" -> "value2",
            "key3" -> "value3"
          )
        )
      ),
      expectedName = "01-02",
      nameDerivedFromNodes = true,
      map = RouteMap(
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
      ),
      "",
      locationAnalysis = RouteLocationAnalysis(
        None,
        Seq.empty,
        Seq.empty
      )
    )

    RouteDetailsPageData(
      id = 1,
      active = true,
      summary = RouteSummary(
        id = 1,
        country = Some(Country.nl),
        networkType = NetworkType.hiking,
        networkScope = NetworkScope.regional,
        name = "01-02",
        meters = 1234,
        broken = true,
        inaccessible = true,
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
      proposed = true,
      version = 1,
      changeSetId = 1,
      lastUpdated = Timestamp(2020, 10, 11, 12, 34, 56),
      lastSurvey = Some(Day(2020, 11, Some(8))),
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
        Fact.RouteIncomplete,
        Fact.RouteInvalidSurveyDate
      ),
      analysis,
      Seq.empty,
      analysis.map.nodeIds
    )
  }
}
