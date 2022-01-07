package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.common.NetworkFact
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NetworkNodeMember
import kpn.core.test.TestData
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import kpn.server.overpass.OverpassRepository
import org.scalamock.scalatest.MockFactory

class NetworkInfoExtraAnalyzerTest extends UnitTest with MockFactory with SharedTestObjects {

  test("network relation without members") {

    val overpassRepository: OverpassRepository = null
    val analysisTimestamp: Timestamp = Timestamp(2020, 11, 8)
    val networkDoc: NetworkDoc = newNetwork(
      1
    )
    val contextBefore = NetworkInfoAnalysisContext(
      analysisTimestamp,
      networkDoc
    )

    val contextAfter = new NetworkInfoExtraAnalyzer(overpassRepository).analyze(contextBefore)

    assert(contextAfter.extraNodeIds.isEmpty)
    assert(contextAfter.extraWayIds.isEmpty)
    assert(contextAfter.extraRelationIds.isEmpty)
    assert(contextAfter.networkFacts.isEmpty)
  }

  test("networkExtraMemberNode") {

    val overpassRepository = stub[OverpassRepository]
    (overpassRepository.nodes _).when(*, *).returns(Seq(newRawNode(1001)))

    val analysisTimestamp: Timestamp = Timestamp(2020, 11, 8)
    val networkDoc: NetworkDoc = newNetwork(
      1,
      nodeMembers = Seq(
        NetworkNodeMember(1001, None)
      )
    )
    val contextBefore = NetworkInfoAnalysisContext(
      analysisTimestamp,
      networkDoc
    )

    val contextAfter = new NetworkInfoExtraAnalyzer(overpassRepository).analyze(contextBefore)

    contextAfter.extraNodeIds should equal(Seq(1001))
    assert(contextAfter.extraWayIds.isEmpty)
    assert(contextAfter.extraRelationIds.isEmpty)
    assert(contextAfter.facts.isEmpty)

    contextAfter.networkFacts should matchTo(
      Seq(
        NetworkFact(
          "NetworkExtraMemberNode",
          Some("node"),
          Some(List(1001)
          ),
          None,
          None
        )
      )
    )
  }

  test("no fact networkExtraMemberNode when map or guidepost") {

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

    val overpassRepository = stub[OverpassRepository]
    (overpassRepository.nodes _).when(*, *).returns(Seq(node, mapNode, guidepostNode, boardNode))

    val analysisTimestamp: Timestamp = Timestamp(2020, 11, 8)
    val networkDoc: NetworkDoc = newNetwork(
      1,
      nodeMembers = Seq(
        NetworkNodeMember(1001, None),
        NetworkNodeMember(1002, None),
        NetworkNodeMember(1003, None),
        NetworkNodeMember(1004, None)
      )
    )
    val contextBefore = NetworkInfoAnalysisContext(
      analysisTimestamp,
      networkDoc
    )

    val contextAfter = new NetworkInfoExtraAnalyzer(overpassRepository).analyze(contextBefore)

    contextAfter.extraNodeIds should equal(Seq(1001))
    assert(contextAfter.extraWayIds.isEmpty)
    assert(contextAfter.extraRelationIds.isEmpty)
    assert(contextAfter.facts.isEmpty)

    contextAfter.networkFacts should matchTo(
      Seq(
        NetworkFact(
          "NetworkExtraMemberNode",
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

    pending

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
    //          newMember("node", 1001),
    //          newMember("node", 1002),
    //          newMember("node", 1003),
    //          newMember("node", 1004),
    //          newMember("node", 1005)
    //        )
    //      )
    //    }


    val analysisTimestamp: Timestamp = Timestamp(2020, 11, 8)
    val networkDoc: NetworkDoc = newNetwork(
      1,
      nodeMembers = Seq(
        NetworkNodeMember(1001, None),
        NetworkNodeMember(1002, None),
        NetworkNodeMember(1003, None),
        NetworkNodeMember(1004, None),
        NetworkNodeMember(1005, None)
      )
    )
    val contextBefore = NetworkInfoAnalysisContext(
      analysisTimestamp,
      networkDoc
    )

    val rawNodes = Seq(
      node1002,
      node1003,
      node1004,
      node1005
    )

    val overpassRepository = stub[OverpassRepository]
    (overpassRepository.nodes _).when(*, *).returns(rawNodes)

    val contextAfter = new NetworkInfoExtraAnalyzer(overpassRepository).analyze(contextBefore)

    assert(contextAfter.extraNodeIds.isEmpty)
    assert(contextAfter.extraWayIds.isEmpty)
    assert(contextAfter.extraRelationIds.isEmpty)
    assert(contextAfter.facts.isEmpty)
    assert(contextAfter.networkFacts.isEmpty)

    //    val network = analyze(d)
    //    network.facts.networkExtraMemberNode should equal(None)
  }

  test("networkExtraMemberNode - not generated when non-proposed node in proposed network") {

    pending

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
    //  val network = analyze(d)
    //  network.facts.networkExtraMemberNode should equal(None)
  }

  test("networkExtraMemberWay relation without members") {

    pending

    val d = new TestData() {
      way(1)
      relation(
        1,
        Seq(
          newMember("way", 1)
        )
      )
    }

    //  val network = analyze(d)
    //  network.facts.networkExtraMemberWay should equal(Some(Seq(NetworkExtraMemberWay(1))))
  }

  test("networkExtraMemberRelation") {

    pending

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

    //  val network = analyze(d)
    //  network.facts.networkExtraMemberRelation should equal(Some(Seq(NetworkExtraMemberRelation(20))))
  }

  test("routes") {

    pending

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

    //  val routes = analyze(d).routes
    //  routes.head.routeAnalysis.route.summary.name should equal("01-02")
    //  routes(1).routeAnalysis.route.summary.name should equal("01-03")
    //  routes.head.role should equal(Some("backward"))
    //  routes(1).role should equal(Some("forward"))
  }

  test("nodes") {

    pending

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
