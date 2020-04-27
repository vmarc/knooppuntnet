package kpn.server.analyzer.engine.analysis.network

import kpn.api.common.NetworkExtraMemberNode
import kpn.api.common.NetworkExtraMemberRelation
import kpn.api.common.NetworkExtraMemberWay
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.analysis.Network
import kpn.core.test.TestData
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.location.NodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.node.analyzers.MainNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedNetwork
import org.scalamock.scalatest.MockFactory

class NetworkAnalyzerTest extends UnitTest with MockFactory {

  test("relation without members") {
    val d = new TestData() {
      relation(1)
    }
    val network = analyze(d)

    network.facts.networkExtraMemberNode should equal(None)
    network.facts.networkExtraMemberWay should equal(None)
    network.facts.networkExtraMemberRelation should equal(None)
  }

  test("networkExtraMemberNode") {
    val d = new TestData() {
      networkNode(1001, "01")
      node(1002)
      relation(1, Seq(newMember("node", 1001), newMember("node", 1002)))
    }
    val network = analyze(d)
    network.facts.networkExtraMemberNode should equal(Some(Seq(NetworkExtraMemberNode(1002))))
  }

  test("networkExtraMemberWay relation without members") {

    val d = new TestData() {
      way(1)
      relation(1, Seq(newMember("way", 1)))
    }

    val network = analyze(d)
    network.facts.networkExtraMemberWay should equal(Some(Seq(NetworkExtraMemberWay(1))))
  }

  test("networkExtraMemberRelation") {

    val d = new TestData() {
      relation(10, Seq(), newRouteTags("01-02")) // valid route relation
      relation(20, Seq(), Tags.empty) // not a route relation
      relation(
        1,
        Seq(
          newMember("relation", 10),
          newMember("relation", 20)
        )
      )
    }

    val network = analyze(d)
    network.facts.networkExtraMemberRelation should equal(Some(Seq(NetworkExtraMemberRelation(20))))
  }

  test("routes") {

    val d = new TestData() {
      relation(10, Seq(), newRouteTags("01-03"))
      relation(20, Seq(), newRouteTags("01-02"))
      relation(30, Seq(), newRouteTags("02-03"))
      relation(1, Seq(
        newMember("relation", 10, "forward"),
        newMember("relation", 20, "backward"),
        newMember("relation", 30)
      )
      )
    }

    val routes = analyze(d).routes
    routes(0).routeAnalysis.route.summary.name should equal("01-02")
    routes(1).routeAnalysis.route.summary.name should equal("01-03")
    routes(0).role should equal(Some("backward"))
    routes(1).role should equal(Some("forward"))
  }

  test("routes - old tagging") {

    val d = new TestData() {
      relation(10, Seq(), Tags.from("network" -> "rwn", "type" -> "route", "note" -> "01-03"))
      relation(20, Seq(), Tags.from("network" -> "rwn", "type" -> "route", "note" -> "01-02"))
      relation(30, Seq(), Tags.from("network" -> "rwn", "type" -> "route", "note" -> "02-03"))
      relation(1, Seq(
        newMember("relation", 10, "forward"),
        newMember("relation", 20, "backward"),
        newMember("relation", 30)
      )
      )
    }

    val routes = analyze(d, oldTagging = true).routes
    routes(0).routeAnalysis.route.summary.name should equal("01-02")
    routes(1).routeAnalysis.route.summary.name should equal("01-03")
    routes(0).role should equal(Some("backward"))
    routes(1).role should equal(Some("forward"))
  }

  private def analyze(d: TestData, oldTagging: Boolean = false): Network = {
    val data = d.data
    val networkRelation = data.relations(1)
    val loadedNetwork = LoadedNetwork(1, NetworkType.hiking, "name", data, networkRelation)
    val countryAnalyzer = stub[CountryAnalyzer]
    (countryAnalyzer.country _).when(*).returns(None)
    (countryAnalyzer.relationCountry _).when(*).returns(None)
    val analysisContext = new AnalysisContext(oldTagging = oldTagging)
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    val tileCalculator = new TileCalculatorImpl()
    val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val masterRouteAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeLocationAnalyzer,
      new AccessibilityAnalyzerImpl(),
      routeTileAnalyzer
    )
    val networkRelationAnalysis = new NetworkRelationAnalyzerImpl(relationAnalyzer, countryAnalyzer).analyze(networkRelation)

    val nodeLocationAnalyzer = stub[NodeLocationAnalyzer]
    (nodeLocationAnalyzer.locate _).when(*, *).returns(None)

    val mainNodeAnalyzer = new MainNodeAnalyzerImpl(
      countryAnalyzer,
      nodeLocationAnalyzer
    )

    val networkNodeAnalyzer = new NetworkNodeAnalyzerImpl(
      mainNodeAnalyzer,
      analysisContext
    )

    val networkRouteAnalyzer = new NetworkRouteAnalyzerImpl(
      analysisContext,
      countryAnalyzer,
      relationAnalyzer,
      masterRouteAnalyzer
    )

    val analyzer = new NetworkAnalyzerImpl(
      analysisContext,
      relationAnalyzer,
      countryAnalyzer,
      networkNodeAnalyzer,
      networkRouteAnalyzer
    )

    analyzer.analyze(networkRelationAnalysis, loadedNetwork)
  }
}
