package kpn.server.repository

import kpn.api.common.common.NodeRouteRefs
import kpn.api.custom.ScopedNetworkType

trait NodeRouteRepository {
  def nodesRouteReferences(scopedNetworkType: ScopedNetworkType, nodeIds: Seq[Long]): Seq[NodeRouteRefs]
}
