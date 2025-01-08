package kpn.server.repository

import kpn.api.common.common.NodeRouteRefs
import kpn.api.custom.ScopedRouteType

trait NodeRouteRepository {
  def nodesRouteReferences(scopedRouteType: ScopedRouteType, nodeIds: Seq[Long]): Seq[NodeRouteRefs]
}
