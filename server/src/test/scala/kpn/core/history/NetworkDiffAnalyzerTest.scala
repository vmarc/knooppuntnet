package kpn.core.history

import kpn.api.common.SharedTestObjects
import kpn.api.common.diff.NetworkNodeData
import kpn.api.common.diff.NetworkNodeUpdate
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.network.NetworkNodeDiff
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.TestData
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerNoop
import kpn.server.analyzer.engine.analysis.location.NodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.network.NetworkAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRelationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.network.NetworkRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.node.analyzers.MainNodeAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.analyzer.load.data.LoadedNetwork

class NetworkDiffAnalyzerTest extends UnitTest with SharedTestObjects {

  test("removed network node") {

    val before = new TestData() {
      networkNode(1001, "01")
      networkNode(1002, "02")
      networkRelation(1, "name", Seq(newMember("node", 1001), newMember("node", 1002)))
    }

    val after = new TestData() {
      networkNode(1001, "01")
      networkRelation(1, "name", Seq(newMember("node", 1001)))
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.networkNodes.removed should matchTo(
      Seq(
        NetworkNodeData(
          newRawNodeWithName(1002, "02"),
          "02"
        )
      )
    )
  }

  test("added network node") {

    val before = new TestData() {
      networkNode(1001, "01")
      node(1002)
      networkRelation(1, "name", Seq(newMember("node", 1001), newMember("node", 1002)))
    }

    val after = new TestData() {
      networkNode(1001, "01")
      networkNode(1002, "02")
      networkRelation(1, "name", Seq(newMember("node", 1001), newMember("node", 1002)))
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.networkNodes.added should matchTo(
      Seq(
        NetworkNodeData(
          newRawNodeWithName(1002, "02"),
          "02"
        )
      )
    )
  }

  test("updated network node") {

    val before = new TestData() {
      networkNode(1001, "01", Tags.from("key" -> "value1"))
      networkRelation(1, "name", Seq(newMember("node", 1001)))
    }

    val after = new TestData() {
      networkNode(1001, "01", Tags.from("key" -> "value2"))
      networkRelation(1, "name", Seq(newMember("node", 1001)))
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.networkNodes.updated should matchTo(
      Seq(
        NetworkNodeUpdate(
          before = NetworkNodeData(
            newNode(1001, tags = Tags.from("rwn_ref" -> "01", "network:type" -> "node_network", "key" -> "value1")).raw,
            "01"
          ),
          after = NetworkNodeData(
            newNode(1001, tags = Tags.from("rwn_ref" -> "01", "network:type" -> "node_network", "key" -> "value2")).raw,
            "01"
          ),
          diffs = NetworkNodeDiff(
            tagDiffs = Some(
              TagDiffs(
                mainTags = Seq(
                  TagDetail(TagDetailType.Same, "rwn_ref", Some("01"), Some("01")),
                  TagDetail(TagDetailType.Same, "network:type", Some("node_network"), Some("node_network"))
                ),
                extraTags = Seq(
                  TagDetail(TagDetailType.Update, "key", Some("value1"), Some("value2"))
                )
              )
            )
          )
        )
      )
    )
  }

  test("removed route") {

    val before = new TestData() {

      node(1001)
      node(1002)
      node(1003)
      node(1004)

      way(101, 1001, 1002)
      way(102, 1003, 1004)

      route(11, "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      route(12, "03-04",
        Seq(
          newMember("way", 102)
        )
      )
      networkRelation(1, "name", Seq(newMember("relation", 11), newMember("relation", 12)))
    }

    val after = new TestData() {
      node(1001)
      node(1002)

      way(101, 1001, 1002)

      route(11, "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      networkRelation(1, "name", Seq(newMember("relation", 11)))
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.routes.removed.map(_.name) should equal(Seq("03-04"))
  }

  test("added route") {

    val before = new TestData() {
      node(1001)
      node(1002)

      way(101, 1001, 1002)

      route(11, "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      networkRelation(1, "name", Seq(newMember("relation", 11)))
    }

    val after = new TestData() {
      node(1001)
      node(1002)
      node(1003)
      node(1004)

      way(101, 1001, 1002)
      way(102, 1003, 1004)

      route(11, "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      route(12, "03-04",
        Seq(
          newMember("way", 102)
        )
      )
      networkRelation(1, "name", Seq(newMember("relation", 11), newMember("relation", 12)))
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.routes.added.map(_.name) should equal(Seq("03-04"))
  }

  test("updated route") {

    val before = new TestData() {
      node(1001)
      node(1002)

      way(101, 1001, 1002)

      route(11, "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      networkRelation(1, "name", Seq(newMember("relation", 11)))
    }

    val after = new TestData() {
      node(1003)
      node(1004)

      way(102, 1003, 1004)

      route(11, "01-02",
        Seq(
          newMember("way", 102)
        )
      )
      networkRelation(1, "name", Seq(newMember("relation", 11)))
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.routes.updated.map(_.name) should equal(Seq("01-02"))
  }

  test("network relation invalid member change - non network node removed") {

    val before = new TestData() {
      networkNode(1001, "01")
      node(1002)
      networkRelation(1, "name", Seq(newMember("node", 1001), newMember("node", 1002)))
    }

    val after = new TestData() {
      networkNode(1001, "01")
      networkRelation(1, "name", Seq(newMember("node", 1001)))
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.nodes.removed should equal(Seq(1002L))
  }

  test("network relation invalid member change - non network node added") {

    val before = new TestData() {
      networkNode(1001, "01")
      networkRelation(1, "name", Seq(newMember("node", 1001)))
    }

    val after = new TestData() {
      networkNode(1001, "01")
      node(1002)
      networkRelation(1, "name", Seq(newMember("node", 1001), newMember("node", 1002)))
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.nodes.added should equal(Seq(1002L))
  }

  test("network relation invalid member change - non network node updated") {
    val before = new TestData() {
      networkNode(1001, "01", Tags.from("key" -> "value1"))
      node(1002, Tags.from("key" -> "value2"))
      networkRelation(1, "name", Seq(newMember("node", 1001), newMember("node", 1002)))
    }

    val after = new TestData() {
      networkNode(1001, "01", Tags.from("key" -> "value3"))
      node(1002, Tags.from("key" -> "value4"))
      networkRelation(1, "name", Seq(newMember("node", 1001), newMember("node", 1002)))
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.nodes.updated should equal(Seq(1002L))
  }

  test("network relation invalid member change - way removed") {
    val before = new TestData() {
      node(1001)
      node(1002)
      way(101, 1001, 1002)
      networkRelation(1, "name", Seq(newMember("way", 101)))
    }

    val after = new TestData() {
      networkRelation(1, "name", Seq())
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.ways.removed should equal(Seq(101L))
  }

  test("network relation invalid member change - way added") {
    val before = new TestData() {
      networkRelation(1, "name", Seq())
    }

    val after = new TestData() {
      node(1001)
      node(1002)
      way(101, 1001, 1002)
      networkRelation(1, "name", Seq(newMember("way", 101)))
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.ways.added should equal(Seq(101L))
  }

  test("network relation invalid member change - way updated") {
    val before = new TestData() {
      node(1001)
      node(1002)
      way(101, 1001, 1002)
      networkRelation(1, "name", Seq(newMember("way", 101)))
    }

    val after = new TestData() {
      node(1001)
      node(1002)
      node(1003)
      way(101, 1001, 1002, 1003)
      networkRelation(1, "name", Seq(newMember("way", 101)))
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.ways.updated should equal(Seq(101L))
  }

  test("network relation invalid member change - non route relation removed") {
    val before = new TestData() {
      relation(2)
      networkRelation(1, "name", Seq(newMember("relation", 2)))
    }

    val after = new TestData() {
      networkRelation(1, "name", Seq())
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.relations.removed should equal(Seq(2L))
  }

  test("network relation invalid member change - non route relation added") {
    val before = new TestData() {
      networkRelation(1, "name", Seq())
    }

    val after = new TestData() {
      relation(2)
      networkRelation(1, "name", Seq(newMember("relation", 2)))
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.relations.added should equal(Seq(2L))
  }

  test("network relation invalid member change - non route relation updated") {
    val before = new TestData() {
      relation(2, tags = Tags.from("a" -> "1"))
      networkRelation(1, "name", Seq(newMember("relation", 2)))
    }

    val after = new TestData() {
      relation(2, tags = Tags.from("a" -> "2"))
      networkRelation(1, "name", Seq(newMember("relation", 2)))
    }

    val analysis = new NetworkDiffAnalyzer(snapshot(before), snapshot(after)).diff

    analysis.relations.updated should equal(Seq(2L))
  }

  private def snapshot(d: TestData): NetworkSnapshot = {
    val data = d.data
    val countryAnalyzer = new CountryAnalyzerNoop()
    val analysisContext = new AnalysisContext()
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    val tileCalculator = new TileCalculatorImpl()
    val routeTileAnalyzer = new RouteTileAnalyzerImpl(tileCalculator)
    val routeLocationAnalyzer = new RouteLocationAnalyzerMock()
    val nodeAnalyzer = new NodeAnalyzerImpl()
    val masterRouteAnalyzer = new MasterRouteAnalyzerImpl(
      analysisContext,
      routeLocationAnalyzer,
      routeTileAnalyzer,
      nodeAnalyzer
    )
    val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(relationAnalyzer, countryAnalyzer)
    val nodeLocationAnalyzer = stub[NodeLocationAnalyzer]
    (nodeLocationAnalyzer.locate _).when(*, *).returns(None)

    val mainNodeAnalyzer = new MainNodeAnalyzerImpl(
      countryAnalyzer,
      nodeLocationAnalyzer
    )

    val networkNodeAnalyzer = new NetworkNodeAnalyzerImpl(analysisContext, mainNodeAnalyzer, nodeAnalyzer)

    val networkRouteAnalyzer = new NetworkRouteAnalyzerImpl(
      analysisContext,
      countryAnalyzer,
      relationAnalyzer,
      masterRouteAnalyzer
    )

    val networkAnalyzer = new NetworkAnalyzerImpl(
      relationAnalyzer,
      networkNodeAnalyzer,
      networkRouteAnalyzer
    )

    val networkRelationAnalysis = networkRelationAnalyzer.analyze(data.relations(1))
    val loadedNetwork = LoadedNetwork(1, ScopedNetworkType.rwn, "name", data, data.relations(1))
    val network = networkAnalyzer.analyze(networkRelationAnalysis, loadedNetwork)
    NetworkSnapshot(Timestamp(2015, 1, 1, 0, 0, 0), data, network)
  }
}
