package kpn.server.api.analysis.pages.route

import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.RouteScope
import kpn.api.common.RouteSummary
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.data.MemberType
import kpn.api.common.route.RouteDetails
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteNodes
import kpn.api.common.route.RouteStructureWay
import kpn.api.common.route.StructureRow
import kpn.api.common.route.WayDirection
import kpn.api.custom.Day
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

object RouteDetailsPageExample {
  val page: RouteDetailsPage = {
    RouteDetailsPage(
      RouteInfo(
        routeId = 1,
        routeName = "01-02",
        routeTypes = Seq(RouteType.hiking),
        changeCount = 5,
        segmentCount = 3,
        None
      ),
      data(),
    )
  }

  private def data(): RouteDetails = {

    val nodes = RouteNodes()

    RouteDetails(
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
      segments = Seq.empty,
      paths = Seq.empty,
      structureRows = Seq(
        StructureRow(
          rowNumber = "1",
          level = 0,
          id = 1L,
          memberType = MemberType.Node,
          role = Some("connection"),
          link = None,
          distance = 0,
          name = None,
          poi = None,
          way = None,
          relation = None,
          segmentIds = Seq.empty,
          pathIds = Seq.empty,
          physical = false,
          relationId = 0,
          subRelationIndex = None,
          survey = None,
          symbol = None,
          referenceTimestamp = None,
          referenceFilename = None,
          referenceDistance = 0,
          deviationDistance = None,
          deviationCount = None,
          osmSegmentCount = None,
          osmDistance = 0,
          osmDistanceSubRelations = 0,
          gaps = None,
          showMap = false,
          happy = true
        ),
        StructureRow(
          rowNumber = "2",
          level = 0,
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
              surface = "unknown",
              accessible = false,
              nodeCount = "3",
              oneWay = WayDirection.Both,
              oneWayTags = Tags.from(
                "key1" -> "value1",
                "key2" -> "value2",
                "key3" -> "value3"
              ),
            )
          ),
          relation = None,
          segmentIds = Seq.empty,
          pathIds = Seq.empty,
          physical = false,
          relationId = 0,
          subRelationIndex = None,
          survey = None,
          symbol = None,
          referenceTimestamp = None,
          referenceFilename = None,
          referenceDistance = 0,
          deviationDistance = None,
          deviationCount = None,
          osmSegmentCount = None,
          osmDistance = 0,
          osmDistanceSubRelations = 0,
          gaps = None,
          showMap = false,
          happy = true
        )
      ),
      nameDerivedFromNodes = true,
      nodes,
      None,
      Seq(1L),
      Seq.empty,
      networkReferences = Seq(
        Reference(RouteType.cycling, RouteScope.regional, 1, "network one", None),
        Reference(RouteType.cycling, RouteScope.regional, 2, "network two", None)
      ),
    )
  }
}
