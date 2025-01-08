package kpn.server.analyzer.engine.changes.changes

import kpn.api.common.NetworkScope
import kpn.api.common.RouteType
import kpn.api.common.SharedTestObjects
import kpn.api.common.data.MemberType
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tags
import kpn.core.test.TestData
import kpn.core.util.UnitTest

class RelationAnalyzerTest extends UnitTest with SharedTestObjects {

  test("scopedRouteType") {
    testscopedRouteType("rwn", NetworkScope.regional, RouteType.hiking)
    testscopedRouteType("lcn", NetworkScope.local, RouteType.cycling)
    testscopedRouteType("iin", NetworkScope.international, RouteType.inlineSkating)
  }

  test("referenced nodes, ways and relations") {

    val network = new TestData() {
      node(1001)
      node(1002)
      node(1003)
      way(101, 1001, 1002)
      route(11, "01-02",
        Seq(
          newMember(MemberType.Node, 1003),
          newMember(MemberType.Way, 101)
        )
      )
      networkRelation(1, "name", Seq(newMember(MemberType.Relation, 11)))
    }.data.relations(1)

    RelationAnalyzer.referencedNodes(network).map(_.id) should equal(Set(1001L, 1002L, 1003L))
    RelationAnalyzer.referencedWays(network).map(_.id) should equal(Set(101L))
    RelationAnalyzer.referencedRelations(network).map(_.id) should equal(Set(11L))
  }

  test("node reference in route way") {

    val network = new TestData() {
      node(1001)
      way(101, 1001)
      route(11, "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
    }.data.relations(11)

    RelationAnalyzer.referencedNodes(network).map(_.id) should equal(Set(1001L))
  }

  private def testscopedRouteType(networkTagValue: String, expectedNetworkScope: NetworkScope, expectedrouteType: RouteType): Unit = {
    val relation = newRelation(tags = Tags.from("network" -> networkTagValue, "type" -> "network", "name" -> "name", "network:type" -> "node_network"))
    assertEqual(
      RelationAnalyzer.scopedRouteType(relation),
      Some(
        ScopedRouteType(expectedNetworkScope, expectedrouteType)
      )
    )
  }
}
