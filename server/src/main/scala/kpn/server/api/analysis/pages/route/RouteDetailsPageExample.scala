package kpn.server.api.analysis.pages.route

import kpn.api.common.RouteSummary
import kpn.api.common.common.Reference
import kpn.api.common.route.Both
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteMap
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.common.route.RouteReferences
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

object RouteDetailsPageExample {

  val page: RouteDetailsPage = {
    RouteDetailsPage(
      route(),
      routeReferences(),
      123
    )
  }

  private def route(): RouteInfo = {
    RouteInfo(
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

            kpn.api.custom.RouteMemberInfo(
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
            kpn.api.custom.RouteMemberInfo(
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
          ),
          locationAnalysis = None
        )
      )
    )
  }

  private def routeReferences(): RouteReferences = {
    RouteReferences(
      networkReferences = Seq(
        Reference(1, "network one", NetworkType.bicycle),
        Reference(2, "network two", NetworkType.hiking, connection = true)
      )
    )
  }

}
