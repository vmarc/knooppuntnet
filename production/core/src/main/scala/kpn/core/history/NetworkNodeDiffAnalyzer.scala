package kpn.core.history

import kpn.core.analysis.NetworkNodeInfo
import kpn.shared.NetworkType
import kpn.shared.common.Ref
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.network.NetworkNodeDiff
import kpn.shared.diff.network.NodeIntegrityCheckDiff
import kpn.shared.diff.network.NodeRouteReferenceDiffs
import kpn.shared.route.RouteInfo

class NetworkNodeDiffAnalyzer(networkType: NetworkType, before: NetworkNodeInfo, after: NetworkNodeInfo) {

  def diffs: NetworkNodeDiff = NetworkNodeDiff(
    connection,
    definedInNetworkRelation,
    routeReferenceDiffs,
    nodeIntegrityCheckDiff,
    tagDiffs
  )

  private val beforeNode = before.networkNode.node
  private val afterNode = after.networkNode.node

  private def connection: Option[Boolean] = {
    if (before.connection != after.connection) {
      Some(after.connection)
    }
    else {
      None
    }
  }

  private def definedInNetworkRelation: Option[Boolean] = {
    if (before.definedInRelation != after.definedInRelation) {
      Some(after.definedInRelation)
    }
    else {
      None
    }
  }

  private def routeReferenceDiffs: Option[NodeRouteReferenceDiffs] = {
    val beforeRouteIds = before.referencedInRoutes.map(_.id).toSet
    val afterRouteIds = after.referencedInRoutes.map(_.id).toSet

    if (beforeRouteIds != afterRouteIds) {
      val removedIds = beforeRouteIds -- afterRouteIds
      val addedIds = afterRouteIds -- beforeRouteIds
      val remainingIds = afterRouteIds intersect beforeRouteIds

      val removedRouteRefs = routeRefs(before.referencedInRoutes, removedIds)
      val addedRouteRefs = routeRefs(after.referencedInRoutes, addedIds)
      val remainingRouteRefs = routeRefs(after.referencedInRoutes, remainingIds)

      Some(NodeRouteReferenceDiffs(removedRouteRefs, addedRouteRefs, remainingRouteRefs))
    }
    else {
      None
    }
  }

  private def routeRefs(routes: Seq[RouteInfo], ids: Set[Long]): Seq[Ref] = {
    routes.filter(route => ids.contains(route.id)).map(route => Ref(route.id, route.summary.name))
  }

  private def tagDiffs: Option[TagDiffs] = {
    val mainTagKeys = networkType match {
      case NetworkType.bicycle => Seq("rcn_ref", "expected_rcn_route_relations", "fixme", "fixmetodo")
      case NetworkType.hiking => Seq("rwn_ref", "expected_rwn_route_relations", "fixme", "fixmetodo")
      case NetworkType.horse => Seq("rhn_ref", "expected_rhn_route_relations", "fixme", "fixmetodo")
      case NetworkType.motorboat => Seq("rmn_ref", "expected_rmn_route_relations", "fixme", "fixmetodo")
      case NetworkType.canoe => Seq("rpn_ref", "expected_rpn_route_relations", "fixme", "fixmetodo")
      case NetworkType.inlineSkates => Seq("rin_ref", "expected_rin_route_relations", "fixme", "fixmetodo")
    }
    // name:rcn_ref -> Het Tolhuis
    // tourism -> information, description -> Informatiepaneel knooppunt 32, information -> map,
    new TagDiffAnalyzer(beforeNode, afterNode, mainTagKeys).diffs
  }

  private def nodeIntegrityCheckDiff: Option[NodeIntegrityCheckDiff] = {
    if (before.integrityCheck != after.integrityCheck) {
      Some(NodeIntegrityCheckDiff(before.integrityCheck, after.integrityCheck))
    }
    else {
      None
    }
  }
}
