package kpn.server.repository

import kpn.api.common.NodeRoute
import kpn.api.common.common.NodeRouteCount
import kpn.api.common.common.NodeRouteExpectedCount
import kpn.api.common.common.NodeRouteRefs
import kpn.api.common.common.Ref
import kpn.api.custom.ScopedNetworkType

trait NodeRouteRepository {

  def save(nodeRoute: NodeRoute): Unit

  def delete(nodeId: Long, scopedNetworkType: ScopedNetworkType): Unit

  def nodeRoutes(scopedNetworkType: ScopedNetworkType): Seq[NodeRoute]

  def nodeRouteReferences(scopedNetworkType: ScopedNetworkType, nodeId: Long): Seq[Ref]

  def nodesRouteReferences(scopedNetworkType: ScopedNetworkType, nodeIds: Seq[Long]): Seq[NodeRouteRefs]

  def actualNodeRouteCounts(scopedNetworkType: ScopedNetworkType): Seq[NodeRouteCount]

  def expectedNodeRouteCounts(scopedNetworkType: ScopedNetworkType): Seq[NodeRouteExpectedCount]

}
