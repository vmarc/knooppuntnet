package kpn.server.analyzer.engine.changes.changes

import kpn.api.common.SharedTestObjects
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.test.TestData
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.context.AnalysisContext

class RelationAnalyzerTest extends UnitTest with SharedTestObjects {

  test("scopedNetworkType") {
    testScopedNetworkType("rwn", NetworkScope.regional, NetworkType.hiking)
    testScopedNetworkType("lcn", NetworkScope.local, NetworkType.cycling)
    testScopedNetworkType("iin", NetworkScope.international, NetworkType.inlineSkating)
  }

  test("referenced nodes, ways and relations") {

    val network = new TestData() {
      node(1001)
      node(1002)
      node(1003)
      way(101, 1001, 1002)
      route(11, "01-02",
        Seq(
          newMember("node", 1003),
          newMember("way", 101)
        )
      )
      networkRelation(1, "name", Seq(newMember("relation", 11)))
    }.data.relations(1)

    relationAnalyzer().referencedNodes(network).map(_.id) should equal(Set(1001L, 1002L, 1003L))
    relationAnalyzer().referencedWays(network).map(_.id) should equal(Set(101L))
    relationAnalyzer().referencedRelations(network).map(_.id) should equal(Set(11L))
  }

  test("node reference in route way") {

    val network = new TestData() {
      node(1001)
      way(101, 1001)
      route(11, "01-02",
        Seq(
          newMember("way", 101)
        )
      )
    }.data.relations(11)

    relationAnalyzer().referencedNodes(network).map(_.id) should equal(Set(1001L))
  }

  private def testScopedNetworkType(networkTagValue: String, expectedNetworkScope: NetworkScope, expectedNetworkType: NetworkType): Unit = {
    val relation = newRawRelation(tags = Tags.from("network" -> networkTagValue, "type" -> "network", "name" -> "name", "network:type" -> "node_network"))
    RelationAnalyzer.scopedNetworkType(relation).value should matchTo(
      ScopedNetworkType(expectedNetworkScope, expectedNetworkType)
    )
  }

  private def relationAnalyzer(): RelationAnalyzer = {
    val analysisContext = new AnalysisContext()
    new RelationAnalyzerImpl(analysisContext)
  }

}
