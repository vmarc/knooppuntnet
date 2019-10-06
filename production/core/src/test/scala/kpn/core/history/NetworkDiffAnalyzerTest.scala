package kpn.core.history

import kpn.core.changes.RelationAnalyzerImpl
import kpn.core.engine.analysis.NetworkAnalyzerImpl
import kpn.core.engine.analysis.NetworkRelationAnalyzerImpl
import kpn.core.engine.analysis.country.CountryAnalyzerNoop
import kpn.core.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.core.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.core.test.TestData
import kpn.core.tools.analyzer.AnalysisContext
import kpn.shared.NetworkType
import kpn.shared.SharedTestObjects
import kpn.shared.Timestamp
import kpn.shared.data.Tags
import kpn.shared.diff.NetworkNodeData
import kpn.shared.diff.NetworkNodeUpdate
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.network.NetworkNodeDiff
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkDiffAnalyzerTest extends FunSuite with Matchers with SharedTestObjects {

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

    analysis.networkNodes.removed should equal(
      Seq(
        NetworkNodeData(
          newRawNodeWithName(1002, "02"),
          "02",
          None
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

    analysis.networkNodes.added should equal(
      Seq(
        NetworkNodeData(
          newRawNodeWithName(1002, "02"),
          "02",
          None
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

    analysis.networkNodes.updated should equal(
      Seq(
        NetworkNodeUpdate(
          before = NetworkNodeData(
            newNode(1001, tags = Tags.from("rwn_ref" -> "01", "network:type" -> "node_network", "key" -> "value1")).raw,
            "01",
            None
          ),
          after = NetworkNodeData(
            newNode(1001, tags = Tags.from("rwn_ref" -> "01", "network:type" -> "node_network", "key" -> "value2")).raw,
            "01",
            None
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
    val routeAnalyzer = new MasterRouteAnalyzerImpl(analysisContext, new AccessibilityAnalyzerImpl())
    val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(relationAnalyzer, countryAnalyzer)
    val networkAnalyzer = new NetworkAnalyzerImpl(analysisContext, relationAnalyzer, countryAnalyzer, routeAnalyzer)
    val networkRelationAnalysis = networkRelationAnalyzer.analyze(data.relations(1))
    val network = networkAnalyzer.analyze(networkRelationAnalysis, data, NetworkType.hiking, 1)
    NetworkSnapshot(Timestamp(2015, 1, 1, 0, 0, 0), data, network)
  }
}
