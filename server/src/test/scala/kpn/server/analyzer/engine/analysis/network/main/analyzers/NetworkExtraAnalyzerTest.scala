package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.NetworkFact
import kpn.api.common.SharedTestObjects
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.util.UnitTest
import kpn.server.overpass.OverpassRepository
import org.scalamock.scalatest.MockFactory

class NetworkExtraAnalyzerTest extends UnitTest with MockFactory with SharedTestObjects {

  test("network relation without members") {

    val overpassRepository: OverpassRepository = null

    val contextBefore = NetworkAnalysisContext(
      newBaseNetworkDoc(1),
      defaultTimestamp,
      _nodeDetails = Some(Seq.empty),
      _routeDetails = Some(Seq.empty),
      _networkFacts = Some(Seq.empty),
    )

    val contextAfter = new NetworkExtraAnalyzer(overpassRepository).analyze(contextBefore)

    assertEqual(contextAfter.extraNodeIds, Seq.empty)
    assertEqual(contextAfter.extraWayIds, Seq.empty)
    assertEqual(contextAfter.extraRelationIds, Seq.empty)
    assertEqual(contextAfter.networkFacts, Seq.empty)
  }

  test("networkExtraMemberNode") {

    val overpassRepository = stub[OverpassRepository]
    (overpassRepository.nodes _).when(*, *).returns(Seq(newRawNode(1001)))

    val contextBefore = NetworkAnalysisContext(
      newBaseNetworkDoc(
        1,
        members = Seq(
          RawMember(MemberType.Node, 1001, None)
        )
      ),
      defaultTimestamp,
      _nodeDetails = Some(Seq.empty),
      _routeDetails = Some(Seq.empty),
      _networkFacts = Some(Seq.empty),
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
    (overpassRepository.nodes _).when(*, *).returns(
      Seq(
        node,
        mapNode,
        guidepostNode,
        boardNode,
        routeMarkerNode
      )
    )

    val contextBefore = NetworkAnalysisContext(
      newBaseNetworkDoc(
        1,
        members = Seq(
          RawMember(MemberType.Node, 1001, None),
          RawMember(MemberType.Node, 1002, None),
          RawMember(MemberType.Node, 1003, None),
          RawMember(MemberType.Node, 1004, None),
          RawMember(MemberType.Node, 1005, None)
        )
      ),
      defaultTimestamp,
      _nodeDetails = Some(Seq.empty),
      _routeDetails = Some(Seq.empty),
      _networkFacts = Some(Seq.empty),
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

  test("networkExtraMemberWay relation without members") {

    val overpassRepository = stub[OverpassRepository]
    (overpassRepository.nodes _).when(*, *).returns(Seq.empty)

    val contextBefore = NetworkAnalysisContext(
      newBaseNetworkDoc(
        1,
        members = Seq(
          RawMember(MemberType.Way, 11, None),
        )
      ),
      Timestamp(2020, 11, 8),
      _nodeDetails = Some(Seq.empty),
      _routeDetails = Some(Seq.empty),
      _networkFacts = Some(Seq.empty),
    )

    val contextAfter = new NetworkExtraAnalyzer(overpassRepository).analyze(contextBefore)

    assertEqual(contextAfter.extraWayIds, Seq(11))

    assertEqual(
      contextAfter.networkFacts,
      Seq(
        NetworkFact(
          Fact.NetworkExtraMemberWay,
          Some("way"),
          Some(List(11)
          ),
          None,
          None
        )
      )
    )
  }

  test("networkExtraMemberRelation") {

    val overpassRepository = stub[OverpassRepository]
    (overpassRepository.nodes _).when(*, *).returns(Seq.empty)

    val contextBefore = NetworkAnalysisContext(
      newBaseNetworkDoc(
        1,
        members = Seq(
          RawMember(MemberType.Relation, 2, None),
        )
      ),
      defaultTimestamp,
      _nodeDetails = Some(Seq.empty),
      _routeDetails = Some(Seq.empty),
      _networkFacts = Some(Seq.empty),
    )

    val contextAfter = new NetworkExtraAnalyzer(overpassRepository).analyze(contextBefore)

    assertEqual(contextAfter.extraRelationIds, Seq(2))

    assertEqual(
      contextAfter.networkFacts,
      Seq(
        NetworkFact(
          Fact.NetworkExtraMemberRelation,
          Some("relation"),
          Some(Seq(2)),
          None,
          None
        )
      )
    )
  }
}
