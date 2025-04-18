package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.NetworkFact
import kpn.api.common.SharedTestObjects
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.TestData
import kpn.core.util.UnitTest
import kpn.server.overpass.OverpassRepository
import org.scalamock.scalatest.MockFactory

class NetworkExtraAnalyzerTest extends UnitTest with MockFactory with SharedTestObjects {

  test("network relation without members") {
    pendingRedesignPrio2()

    val overpassRepository: OverpassRepository = null
    val analysisTimestamp: Timestamp = Timestamp(2020, 11, 8)
    val contextBefore = NetworkAnalysisContext(
      newBaseNetworkDoc(1),
      analysisTimestamp,
    )

    val contextAfter = new NetworkExtraAnalyzer(overpassRepository).analyze(contextBefore)

    assertEqual(contextAfter.extraNodeIds, Seq.empty)
    assertEqual(contextAfter.extraWayIds, Seq.empty)
    assertEqual(contextAfter.extraRelationIds, Seq.empty)
    assertEqual(contextAfter.networkFacts, Seq.empty)
  }

  test("networkExtraMemberNode") {
    pendingRedesignPrio2()

    val overpassRepository = stub[OverpassRepository]
    (overpassRepository.nodes _).when(*, *).returns(Seq(newRawNode(1001)))

    val analysisTimestamp: Timestamp = Timestamp(2020, 11, 8)
    val network = newBaseNetworkDoc(
      1,
      members = Seq(
        RawMember(MemberType.Node, 1001, None)
      )
    )
    val contextBefore = NetworkAnalysisContext(
      network,
      analysisTimestamp,
    )

    val contextAfter = new NetworkExtraAnalyzer(overpassRepository).analyze(contextBefore)

    assertEqual(contextAfter.extraNodeIds, Seq(1001))
    assertEqual(contextAfter.extraWayIds, Seq.empty)
    assertEqual(contextAfter.extraRelationIds, Seq.empty)
    assertEqual(contextAfter.facts, Seq.empty)

    assertEqual(
      contextAfter.networkFacts,
      Seq(
        NetworkFact(
          Fact.NetworkExtraMemberNode,
          Some("node"),
          Some(List(1001)
          ),
          None,
          None
        )
      )
    )
  }

  test("no fact networkExtraMemberNode when map, guidepost, board or route_marker") {
    pendingRedesignPrio2()

    val node = newRawNode(
      1001
    )

    val mapNode = newRawNode(
      1002,
      tags = Tags.from(
        "tourism" -> "information",
        "information" -> "map"
      )
    )

    val guidepostNode = newRawNode(
      1003,
      tags = Tags.from(
        "tourism" -> "information",
        "information" -> "guidepost"
      )
    )

    val boardNode = newRawNode(
      1004,
      tags = Tags.from(
        "tourism" -> "information",
        "information" -> "board"
      )
    )

    val routeMarkerNode = newRawNode(
      1005,
      tags = Tags.from(
        "tourism" -> "information",
        "information" -> "route_marker"
      )
    )

    val overpassRepository = stub[OverpassRepository]
    (overpassRepository.nodes _).when(*, *).returns(Seq(node, mapNode, guidepostNode, boardNode, routeMarkerNode))

    val analysisTimestamp: Timestamp = Timestamp(2020, 11, 8)
    val network = newBaseNetworkDoc(
      1,
      members = Seq(
        RawMember(MemberType.Node, 1001, None),
        RawMember(MemberType.Node, 1002, None),
        RawMember(MemberType.Node, 1003, None),
        RawMember(MemberType.Node, 1004, None),
        RawMember(MemberType.Node, 1005, None)
      )
    )
    val contextBefore = NetworkAnalysisContext(
      network,
      analysisTimestamp,
    )

    val contextAfter = new NetworkExtraAnalyzer(overpassRepository).analyze(contextBefore)

    assertEqual(contextAfter.extraNodeIds, Seq(1001))
    assertEqual(contextAfter.extraWayIds, Seq.empty)
    assertEqual(contextAfter.extraRelationIds, Seq.empty)
    assertEqual(contextAfter.facts, Seq.empty)

    assertEqual(
      contextAfter.networkFacts,
      Seq(
        NetworkFact(
          Fact.NetworkExtraMemberNode,
          Some("node"),
          Some(List(1001)
          ),
          None,
          None
        )
      )
    )
  }

  test("networkExtraMemberNode - not generated when proposed node in non-proposed network") {

    pendingRedesign()

    val node1001 = newRawNode(1002, tags = Tags.from("network:type" -> "node_network", "rwn_ref" -> "01"))
    val node1002 = newRawNode(1002, tags = Tags.from("network:type" -> "node_network", "proposed:rwn_ref" -> "02"))
    val node1003 = newRawNode(1003, tags = Tags.from("network:type" -> "node_network", "proposed:rwn_name" -> "03"))
    val node1004 = newRawNode(1004, tags = Tags.from("network:type" -> "node_network", "rwn_ref" -> "04", "state" -> "proposed"))
    val node1005 = newRawNode(1005, tags = Tags.from("network:type" -> "node_network", "rwn_name" -> "05", "state" -> "proposed"))

    //    val d = new TestData() {
    //      networkNode(1001, "01")
    //      node(1002, Tags.from("network:type" -> "node_network", "proposed:rwn_ref" -> "02"))
    //      node(1003, Tags.from("network:type" -> "node_network", "proposed:rwn_name" -> "03"))
    //      node(1004, Tags.from("network:type" -> "node_network", "rwn_ref" -> "04", "state" -> "proposed"))
    //      node(1005, Tags.from("network:type" -> "node_network", "rwn_name" -> "05", "state" -> "proposed"))
    //      relation(
    //        1,
    //        Seq(
    //          newMember(MemberType.node, 1001),
    //          newMember(MemberType.node, 1002),
    //          newMember(MemberType.node, 1003),
    //          newMember(MemberType.node, 1004),
    //          newMember(MemberType.node, 1005)
    //        )
    //      )
    //    }

    val analysisTimestamp: Timestamp = Timestamp(2020, 11, 8)
    val network = newBaseNetworkDoc(
      1,
      members = Seq(
        RawMember(MemberType.Node, 1001, None),
        RawMember(MemberType.Node, 1002, None),
        RawMember(MemberType.Node, 1003, None),
        RawMember(MemberType.Node, 1004, None),
        RawMember(MemberType.Node, 1005, None)
      )
    )
    val contextBefore = NetworkAnalysisContext(
      network,
      analysisTimestamp,
    )

    val rawNodes = Seq(
      node1002,
      node1003,
      node1004,
      node1005
    )

    val overpassRepository = stub[OverpassRepository]
    (overpassRepository.nodes _).when(*, *).returns(rawNodes)

    val contextAfter = new NetworkExtraAnalyzer(overpassRepository).analyze(contextBefore)

    assertEqual(contextAfter.extraNodeIds, Seq.empty)
    assertEqual(contextAfter.extraWayIds, Seq.empty)
    assertEqual(contextAfter.extraRelationIds, Seq.empty)
    assertEqual(contextAfter.facts, Seq.empty)
    assertEqual(contextAfter.networkFacts, Seq.empty)

    //    val network = analyze(d)
    //    network.facts.networkExtraMemberNode should equal(None)
  }

  test("networkExtraMemberNode - not generated when non-proposed node in proposed network") {

    pendingRedesign()

    val d = new TestData() {
      networkNode(1001, "01")
      node(1002, Tags.from("network:type" -> "node_network", "proposed:rwn_ref" -> "02"))
      node(1003, Tags.from("network:type" -> "node_network", "proposed:rwn_name" -> "03"))
      node(1004, Tags.from("network:type" -> "node_network", "rwn_ref" -> "04", "state" -> "proposed"))
      node(1005, Tags.from("network:type" -> "node_network", "rwn_name" -> "05", "state" -> "proposed"))
      relation(
        1,
        Seq(
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002),
          newMember(MemberType.Node, 1003),
          newMember(MemberType.Node, 1004),
          newMember(MemberType.Node, 1005)
        ),
        tags = Tags.from("state" -> "proposed")
      )
    }
    //  val network = analyze(d)
    //  network.facts.networkExtraMemberNode should equal(None)
  }

  test("networkExtraMemberWay relation without members") {

    pendingRedesign()

    val d = new TestData() {
      way(1)
      relation(
        1,
        Seq(
          newMember(MemberType.Way, 1)
        )
      )
    }

    //  val network = analyze(d)
    //  network.facts.networkExtraMemberWay should equal(Some(Seq(NetworkExtraMemberWay(1))))
  }

  test("networkExtraMemberRelation") {

    pendingRedesign()

    val d = new TestData() {
      relation(10, Seq.empty, newRouteTags("01-02")) // valid route relation
      relation(20, Seq.empty, Seq.empty) // not a route relation
      relation(
        1,
        Seq(
          newMember(MemberType.Relation, 10),
          newMember(MemberType.Relation, 20)
        )
      )
    }

    //  val network = analyze(d)
    //  network.facts.networkExtraMemberRelation should equal(Some(Seq(NetworkExtraMemberRelation(20))))
  }

  test("routes") {

    pendingRedesign()

    val d = new TestData() {
      relation(10, Seq.empty, newRouteTags("01-03"))
      relation(20, Seq.empty, newRouteTags("01-02"))
      relation(30, Seq.empty, newRouteTags("02-03"))
      relation(
        1,
        Seq(
          newMember(MemberType.Relation, 10, "forward"),
          newMember(MemberType.Relation, 20, "backward"),
          newMember(MemberType.Relation, 30)
        )
      )
    }

    //  val routes = analyze(d).routes
    //  routes.head.routeAnalysis.route.summary.name should equal("01-02")
    //  routes(1).routeAnalysis.route.summary.name should equal("01-03")
    //  routes.head.role should equal(Some("backward"))
    //  routes(1).role should equal(Some("forward"))
  }

  test("nodes") {

    pendingRedesign()

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
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002),
          newMember(MemberType.Node, 1003),
          newMember(MemberType.Node, 1004),
          newMember(MemberType.Node, 1005),
          newMember(MemberType.Node, 1006)
        )
      )
    }

    //  val nodes = analyze(d).nodes
    //
    //  def node(id: Long): NetworkNodeInfo = {
    //    nodes.find(_.id == id).get
    //  }
    //
    //  node(1001).networkNode.name should equal("01")
    //  node(1002).networkNode.name should equal("02")
    //  node(1003).networkNode.name should equal("Node3")
    //  node(1004).networkNode.name should equal("Node4")
    //  node(1005).networkNode.name should equal("05")
    //  node(1006).networkNode.name should equal("Node6")
    //
    //  node(1001).proposed should equal(false)
    //  node(1002).proposed should equal(true)
    //  node(1003).proposed should equal(false)
    //  node(1004).proposed should equal(true)
    //  node(1005).proposed should equal(true)
    //  node(1006).proposed should equal(true)
  }
}
