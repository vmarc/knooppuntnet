package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeIntegrityCheck
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.analysis.NetworkMemberRoute
import kpn.core.analysis.NetworkNode
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.network.NetworkAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteNodeType

class NodeIntegrityAnalyzerTest extends UnitTest with SharedTestObjects {

  test("integrity check success") {

    val node = newNetworkNode()

    val networkAnalysis = NetworkAnalysis(
      networkNodesInRelation = Set(node),
      routes = Seq(
        networkMemberRoute(node, 11),
        networkMemberRoute(node, 12),
        networkMemberRoute(node, 13)
      )
    )

    analysis(networkAnalysis, node) should equal(Some(NodeIntegrityCheck("01", 1001, 3, 3, failed = false)))
  }

  test("integrity check - do count route with role 'connection' in network relation (see Issue #32)") {

    val node = newNetworkNode()

    val networkAnalysis = NetworkAnalysis(
      networkNodesInRelation = Set(node),
      routes = Seq(
        networkMemberRoute(node, 11, Some("connection")),
        networkMemberRoute(node, 12),
        networkMemberRoute(node, 13)
      )
    )

    analysis(networkAnalysis, node) should equal(Some(NodeIntegrityCheck("01", 1001, 3, 3, failed = false)))
  }

  test("integrity check - do not count route with state 'connection'") {

    val node = newNetworkNode()

    val networkAnalysis = NetworkAnalysis(
      networkNodesInRelation = Set(node),
      routes = Seq(
        networkMemberRoute(node, 11, routeTags = Tags.from("state" -> "connection")),
        networkMemberRoute(node, 12),
        networkMemberRoute(node, 13)
      )
    )

    analysis(networkAnalysis, node) should equal(Some(NodeIntegrityCheck("01", 1001, 2, 3, failed = true)))
  }

  test("integrity check - do not count route with state 'alternate'") {

    val node = newNetworkNode()

    val networkAnalysis = NetworkAnalysis(
      networkNodesInRelation = Set(node),
      routes = Seq(
        networkMemberRoute(node, 11, routeTags = Tags.from("state" -> "alternate")),
        networkMemberRoute(node, 12),
        networkMemberRoute(node, 13)
      )
    )

    analysis(networkAnalysis, node) should equal(Some(NodeIntegrityCheck("01", 1001, 2, 3, failed = true)))
  }

  test("no integrity check when no integrity check tag on node") {

    val node = newNetworkNodeWithTags(Tags.empty)

    val networkAnalysis = NetworkAnalysis(
      networkNodesInRelation = Set(node),
      routes = Seq(
        networkMemberRoute(node, 11),
        networkMemberRoute(node, 12),
        networkMemberRoute(node, 13)
      )
    )

    analysis(networkAnalysis, node) should equal(None)
  }

  test("no integrity check when node is not member in network relation") {

    val node = newNetworkNode()

    val networkAnalysis = NetworkAnalysis(
      networkNodesInRelation = Set.empty, // <== !!
      routes = Seq(
        networkMemberRoute(node, 11),
        networkMemberRoute(node, 12),
        networkMemberRoute(node, 13)
      )
    )

    analysis(networkAnalysis, node) should equal(None)
  }

  test("integrity check - use expected = 0 when tag value is not numeric") {

    val node = newNetworkNodeWithTags(Tags.from("expected_rwn_route_relations" -> "bla"))

    val networkAnalysis = NetworkAnalysis(
      networkNodesInRelation = Set(node),
      routes = Seq(
        networkMemberRoute(node, 11),
        networkMemberRoute(node, 12),
        networkMemberRoute(node, 13)
      )
    )

    analysis(networkAnalysis, node) should equal(Some(NodeIntegrityCheck("01", 1001, 3, 0, failed = true)))
  }


  private def networkMemberRoute(networkNode: NetworkNode, routeId: Long, role: Option[String] = None, routeTags: Tags = Tags.empty): NetworkMemberRoute = {

    NetworkMemberRoute(
      routeAnalysis = RouteAnalysis(
        relation = null,
        route = newRouteInfo(
          newRouteSummary(
            routeId
          ),
          tags = routeTags
        ),
        routeNodeAnalysis = RouteNodeAnalysis(
          startNodes = Seq(
            RouteNode(
              nodeType = RouteNodeType.Start,
              node = networkNode.node,
              definedInRelation = true,
              definedInWay = true
            )
          )
        )
      ),
      role = role
    )
  }

  private def newNetworkNode(): NetworkNode = {
    newNetworkNodeWithTags(Tags.from("expected_rwn_route_relations" -> "3"))
  }

  private def newNetworkNodeWithTags(tags: Tags): NetworkNode = {
    val node = newNode(1001, tags = tags)
    NetworkNode(
      node = node,
      name = "01",
      longName = None,
      country = Some(Country.nl),
      None
    )
  }

  private def analysis(networkAnalysis: NetworkAnalysis, node: NetworkNode): Option[NodeIntegrityCheck] = {
    new NodeIntegrityAnalyzer(ScopedNetworkType.rwn, networkAnalysis, node).analysis
  }

}
