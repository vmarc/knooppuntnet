package kpn.server.analyzer.engine.analysis.network

import kpn.api.common.NetworkExtraMemberNode
import kpn.api.common.NetworkExtraMemberRelation
import kpn.api.common.NetworkExtraMemberWay
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.analysis.Network
import kpn.core.analysis.NetworkNodeInfo
import kpn.core.test.TestData
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.analysis.location.OldNodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.node.OldNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.OldMainNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeInfoAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
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
      relation(
        1,
        Seq(
          newMember("node", 1001),
          newMember("node", 1002)
        )
      )
    }
    val network = analyze(d)
    network.facts.networkExtraMemberNode should equal(Some(Seq(NetworkExtraMemberNode(1002))))
  }

  test("networkExtraMemberNode - not generated when proposed node in non-proposed network") {
    val d = new TestData() {
      networkNode(1001, "01")
      node(1002, Tags.from("network:type" -> "node_network", "proposed:rwn_ref" -> "02"))
      node(1003, Tags.from("network:type" -> "node_network", "proposed:rwn_name" -> "03"))
      node(1004, Tags.from("network:type" -> "node_network", "rwn_ref" -> "04", "state" -> "proposed"))
      node(1005, Tags.from("network:type" -> "node_network", "rwn_name" -> "05", "state" -> "proposed"))
      relation(
        1,
        Seq(
          newMember("node", 1001),
          newMember("node", 1002),
          newMember("node", 1003),
          newMember("node", 1004),
          newMember("node", 1005)
        )
      )
    }
    val network = analyze(d)
    network.facts.networkExtraMemberNode should equal(None)
  }

  test("networkExtraMemberNode - not generated when non-proposed node in proposed network") {
    val d = new TestData() {
      networkNode(1001, "01")
      node(1002, Tags.from("network:type" -> "node_network", "proposed:rwn_ref" -> "02"))
      node(1003, Tags.from("network:type" -> "node_network", "proposed:rwn_name" -> "03"))
      node(1004, Tags.from("network:type" -> "node_network", "rwn_ref" -> "04", "state" -> "proposed"))
      node(1005, Tags.from("network:type" -> "node_network", "rwn_name" -> "05", "state" -> "proposed"))
      relation(
        1,
        Seq(
          newMember("node", 1001),
          newMember("node", 1002),
          newMember("node", 1003),
          newMember("node", 1004),
          newMember("node", 1005)
        ),
        tags = Tags.from("state" -> "proposed")
      )
    }
    val network = analyze(d)
    network.facts.networkExtraMemberNode should equal(None)
  }

  test("networkExtraMemberWay relation without members") {

    val d = new TestData() {
      way(1)
      relation(
        1,
        Seq(
          newMember("way", 1)
        )
      )
    }

    val network = analyze(d)
    network.facts.networkExtraMemberWay should equal(Some(Seq(NetworkExtraMemberWay(1))))
  }

  test("networkExtraMemberRelation") {

    val d = new TestData() {
      relation(10, Seq.empty, newRouteTags("01-02")) // valid route relation
      relation(20, Seq.empty, Tags.empty) // not a route relation
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
      relation(10, Seq.empty, newRouteTags("01-03"))
      relation(20, Seq.empty, newRouteTags("01-02"))
      relation(30, Seq.empty, newRouteTags("02-03"))
      relation(
        1,
        Seq(
          newMember("relation", 10, "forward"),
          newMember("relation", 20, "backward"),
          newMember("relation", 30)
        )
      )
    }

    val routes = analyze(d).routes
    routes.head.routeAnalysis.route.summary.name should equal("01-02")
    routes(1).routeAnalysis.route.summary.name should equal("01-03")
    routes.head.role should equal(Some("backward"))
    routes(1).role should equal(Some("forward"))
  }

  test("nodes") {

    val d = new TestData() {
      node(1001, tags = Tags.from("network:type" -> "node_network", "rwn_ref" -> "01"))
      node(1002, tags = Tags.from("network:type" -> "node_network", "proposed:rwn_ref" -> "02"))
      node(1003, tags = Tags.from("network:type" -> "node_network", "rwn_name" -> "Node3"))
      node(1004, tags = Tags.from("network:type" -> "node_network", "proposed:rwn_name" -> "Node4"))
      node(1005, tags = Tags.from("network:type" -> "node_network", "rwn_ref" -> "05", "state" -> "proposed"))
      node(1006, tags = Tags.from("network:type" -> "node_network", "rwn_name" -> "Node6", "state" -> "proposed"))
      relation(
        1,
        Seq(
          newMember("node", 1001),
          newMember("node", 1002),
          newMember("node", 1003),
          newMember("node", 1004),
          newMember("node", 1005),
          newMember("node", 1006)
        )
      )
    }

    val nodes = analyze(d).nodes

    def node(id: Long): NetworkNodeInfo = {
      nodes.find(_.id == id).get
    }

    node(1001).networkNode.name should equal("01")
    node(1002).networkNode.name should equal("02")
    node(1003).networkNode.name should equal("Node3")
    node(1004).networkNode.name should equal("Node4")
    node(1005).networkNode.name should equal("05")
    node(1006).networkNode.name should equal("Node6")

    node(1001).proposed should equal(false)
    node(1002).proposed should equal(true)
    node(1003).proposed should equal(false)
    node(1004).proposed should equal(true)
    node(1005).proposed should equal(true)
    node(1006).proposed should equal(true)
  }

  private def analyze(d: TestData, oldTagging: Boolean = false): Network = {
    val data = d.data
    val networkRelation = data.relations(1)
    val loadedNetwork = LoadedNetwork(1, ScopedNetworkType.rwn, "name", data, networkRelation)
    val countryAnalyzer = stub[CountryAnalyzer]
    (countryAnalyzer.country _).when(*).returns(None)
    (countryAnalyzer.relationCountry _).when(*).returns(None)
    val analysisContext = new AnalysisContext()
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    val tileCalculator = new TileCalculatorImpl()
    val routeTileCalculator = new RouteTileCalculatorImpl(tileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val routeCountryAnalyzer = new RouteCountryAnalyzer(countryAnalyzer)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val oldNodeAnalyzer = new OldNodeAnalyzerImpl()
    val routeNodeInfoAnalyzer = new RouteNodeInfoAnalyzerImpl(analysisContext, oldNodeAnalyzer)
    val masterRouteAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer,
      routeNodeInfoAnalyzer
    )
    val networkRelationAnalysis = new NetworkRelationAnalyzerImpl(relationAnalyzer, countryAnalyzer).analyze(networkRelation)

    val oldNodeLocationAnalyzer = stub[OldNodeLocationAnalyzer]
    (oldNodeLocationAnalyzer.locations _).when(*, *).returns(Seq.empty)
    (oldNodeLocationAnalyzer.oldLocate _).when(*, *).returns(None)

    val oldMainNodeAnalyzer = new OldMainNodeAnalyzerImpl(
      countryAnalyzer,
      oldNodeLocationAnalyzer
    )

    val networkNodeAnalyzer = new NetworkNodeAnalyzerImpl(analysisContext, oldMainNodeAnalyzer, oldNodeAnalyzer)

    val networkRouteAnalyzer = new NetworkRouteAnalyzerImpl(
      analysisContext,
      countryAnalyzer,
      relationAnalyzer,
      masterRouteAnalyzer
    )

    val analyzer = new NetworkAnalyzerImpl(
      relationAnalyzer,
      networkNodeAnalyzer,
      networkRouteAnalyzer
    )

    analyzer.analyze(networkRelationAnalysis, loadedNetwork)
  }
}
