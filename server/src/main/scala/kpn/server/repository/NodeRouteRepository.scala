package kpn.server.repository

import kpn.api.common.NodeRoute
import kpn.api.common.common.NodeRouteCount
import kpn.api.common.common.NodeRouteExpectedCount
import kpn.api.custom.NetworkType

trait NodeRouteRepository {

  def save(nodeRoute: NodeRoute): Unit

  def delete(nodeId: Long, networkType: NetworkType): Unit

  def nodeRoutes(networkType: NetworkType): Seq[NodeRoute]

  def actualNodeRouteCounts(networkType: NetworkType): Seq[NodeRouteCount]

  def expectedNodeRouteCounts(networkType: NetworkType): Seq[NodeRouteExpectedCount]

}
