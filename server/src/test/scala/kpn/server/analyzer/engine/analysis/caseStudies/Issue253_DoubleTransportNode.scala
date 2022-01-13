package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.NodeName
import kpn.api.common.location.LocationNodeInfo
import kpn.api.custom.Country
import kpn.api.custom.LocationNodesType
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.OrphanNodeDoc
import kpn.core.test.OverpassData
import kpn.database.actions.locations.MongoQueryLocationNodes
import kpn.database.actions.subsets.MongoQuerySubsetOrphanNodes
import kpn.server.analyzer.engine.analysis.node.analyzers.NodeNameAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import kpn.server.analyzer.engine.changes.integration.IntegrationTest

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
      new MongoQuerySubsetOrphanNodes(context.database).execute(Subset.frHiking).shouldMatchTo(
        Seq(
          OrphanNodeDoc(
            _id = "fr:hiking:620168928",
            country = Country.fr,
            networkType = NetworkType.hiking,
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

      new MongoQueryLocationNodes(database).find(NetworkType.hiking, "fr", LocationNodesType.all, 10, 0).shouldMatchTo(
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

      database.nodes.findById(620168928L).shouldMatchTo(
        Some(
          newNodeDoc(
            id = 620168928L,
            labels = Seq(
              "active",
              "network-type-hiking",
              "location-fr",
              "location-fr-1-73",
              "location-fr-2-247300452",
              "location-fr-3-73307"
            ),
            country = Some(Country.fr),
            name = "Teumelet / o",
            names = Seq(
              newNodeName(NetworkType.hiking, NetworkScope.local, "Teumelet"),
              newNodeName(NetworkType.hiking, NetworkScope.regional, "o")
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
    val nodeAnalysis = analyze(
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
    nodeAnalysis.name should equal("Teumelet / o")
    nodeAnalysis.nodeNames.shouldMatchTo(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.local,
          name = "Teumelet",
          longName = None,
          proposed = false
        ),
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.regional,
          name = "o",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("step2: node only local, regional tag removed, changeset 113461712 004/790/397") {
    val nodeAnalysis = analyze(
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
    nodeAnalysis.name should equal("Teumelet")
    nodeAnalysis.nodeNames.shouldMatchTo(
      Seq(
        NodeName(
          networkType = NetworkType.hiking,
          networkScope = NetworkScope.local,
          name = "Teumelet",
          longName = None,
          proposed = false
        )
      )
    )
  }

  test("step3: lost network node tags, changeset 113584885 004/794/696") {
    val nodeAnalysis = analyze(
      Tags.from(
        "hiking" -> "yes",
        "information" -> "guidepost",
        "name" -> "Teumelet",
        "operator" -> "Communauté de Communes Maurienne Galibier",
        "tourism" -> "information",
      )
    )
    nodeAnalysis.name should equal("")
    nodeAnalysis.nodeNames should equal(Seq.empty)
  }

  private def analyze(tags: Tags): NodeAnalysis = {
    val nodeAnalysis = NodeAnalysis(newRawNode(tags = tags))
    NodeNameAnalyzer.analyze(nodeAnalysis)
  }
}
