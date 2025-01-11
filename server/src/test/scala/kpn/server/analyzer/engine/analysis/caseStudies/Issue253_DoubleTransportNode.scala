package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Country
import kpn.api.common.NodeName
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.RouteType.hiking
import kpn.api.common.SurveyDateInfo
import kpn.api.common.location.LocationNodeInfo
import kpn.api.common.location.LocationNodesParameters
import kpn.api.custom.Subset
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.Label
import kpn.core.doc.OrphanNodeDoc
import kpn.core.test.OverpassData
import kpn.database.actions.locations.MongoQueryLocationNodes
import kpn.database.actions.subsets.MongoQuerySubsetOrphanNodes
import kpn.server.analyzer.engine.analysis.location.LocationSubset
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeAnalysisContext
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeNameAnalyzer
import kpn.server.analyzer.engine.changes.integration.IntegrationTest
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder

import java.time.ZoneId
import java.time.ZonedDateTime

class Issue253_DoubleTransportNode extends IntegrationTest {

  test("orphan node list, location node list") {
    val data = OverpassData().node(
      620168928L,
      Tags.from(
        "hiking" -> "yes",
        "information" -> "guidepost",
        "lwn_name" -> "Teumelet",
        "name" -> "Teumelet",
        "network:type" -> "node_network",
        "operator" -> "Communauté de Communes Maurienne Galibier",
        "ref" -> "o",
        "rwn_ref" -> "o",
        "tourism" -> "information",
      ),
      latitude = "45.1703505",
      longitude = "6.4883568",
      version = 5,
      timestamp = Timestamp(2021, 11, 6, 21, 23, 13)
    )

    simulate(data, data) {
      assertEqual(
        new MongoQuerySubsetOrphanNodes(context.database).execute(Subset.frHiking),
        Seq(
          OrphanNodeDoc(
            _id = "fr:hiking:620168928",
            country = Country.fr,
            routeType = RouteType.hiking,
            nodeId = 620168928L,
            name = "Teumelet",
            longName = None,
            proposed = false,
            lastUpdated = Timestamp(2021, 11, 6, 21, 23, 13),
            lastSurvey = None,
            facts = Seq.empty
          )
        )
      )

      val subset = LocationSubset("", hiking, Seq("fr"))
      val surveyDateInfo: SurveyDateInfo = {
        val local = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Brussels"))
        SurveyDateInfoBuilder.dateInfoAt(local)
      }

      assertEqual(
        new MongoQueryLocationNodes(database, surveyDateInfo).find(subset, LocationNodesParameters()),
        Seq(
          LocationNodeInfo(
            rowIndex = 0,
            id = 620168928L,
            name = "Teumelet / o",
            longName = "-",
            latitude = "45.1703505",
            longitude = "6.4883568",
            lastUpdated = Timestamp(2021, 11, 6, 21, 23, 13),
            lastSurvey = None,
            facts = Seq.empty,
            expectedRouteCount = "-",
            routeReferences = Seq.empty
          )
        )
      )

      assertEqual(
        database.nodes.findById(620168928L),
        Some(
          newNodeDoc(
            id = 620168928L,
            labels = Seq(
              Label.active,
              Label.routeType(RouteType.hiking),
              Label.location("fr"),
              Label.location("fr-1-73"),
              Label.location("fr-2-247300452"),
              Label.location("fr-3-73307")
            ),
            country = Some(Country.fr),
            name = Some("Teumelet / o"),
            names = Seq(
              newNodeName(RouteType.hiking, RouteScope.local, "Teumelet"),
              newNodeName(RouteType.hiking, RouteScope.regional, "o")
            ),
            version = 5,
            latitude = "45.1703505",
            longitude = "6.4883568",
            lastUpdated = Timestamp(2021, 11, 6, 21, 23, 13),
            tags = Tags.from(
              "hiking" -> "yes",
              "information" -> "guidepost",
              "lwn_name" -> "Teumelet",
              "name" -> "Teumelet",
              "network:type" -> "node_network",
              "operator" -> "Communauté de Communes Maurienne Galibier",
              "ref" -> "o",
              "rwn_ref" -> "o",
              "tourism" -> "information"
            ),
            locations = Seq(
              "fr",
              "fr-1-73",
              "fr-2-247300452",
              "fr-3-73307"
            )
          )
        )
      )
    }
  }

  test("step1: node that is both regional and local") {
    val context = analyze(
      Tags.from(
        "hiking" -> "yes",
        "information" -> "guidepost",
        "lwn_name" -> "Teumelet",
        "name" -> "Teumelet",
        "network:type" -> "node_network",
        "operator" -> "Communauté de Communes Maurienne Galibier",
        "ref" -> "o",
        "rwn_ref" -> "o",
        "tourism" -> "information",
      )
    )
    context.name should equal("Teumelet / o")
    assertEqual(
      context.names,
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.local,
          name = "Teumelet",
          longName = None,
          proposed = false
        ),
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.regional,
          name = "o",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("step2: node only local, regional tag removed, changeset 113461712 004/790/397") {
    val context = analyze(
      Tags.from(
        "hiking" -> "yes",
        "information" -> "guidepost",
        "lwn_name" -> "Teumelet",
        "name" -> "Teumelet",
        "network:type" -> "node_network",
        "operator" -> "Communauté de Communes Maurienne Galibier",
        "ref" -> "o",
        // "rwn_ref" -> "o", <-- removed
        "tourism" -> "information",
      )
    )
    context.name should equal("Teumelet")
    assertEqual(
      context.names,
      Seq(
        NodeName(
          routeType = RouteType.hiking,
          routeScope = RouteScope.local,
          name = "Teumelet",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("step3: lost network node tags, changeset 113584885 004/794/696") {
    val context = analyze(
      Tags.from(
        "hiking" -> "yes",
        "information" -> "guidepost",
        "name" -> "Teumelet",
        "operator" -> "Communauté de Communes Maurienne Galibier",
        "tourism" -> "information",
      )
    )
    context.name should equal("")
    context.names should equal(Seq.empty)
  }

  private def analyze(tags: Seq[Tag]): BaseNodeAnalysisContext = {
    val context = BaseNodeAnalysisContext(newRawNode(tags = tags))
    BaseNodeNameAnalyzer.analyze(context)
  }
}
