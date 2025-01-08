package kpn.server.api.analysis.pages.node

import kpn.api.common.RouteType
import kpn.api.common.node.MapNodeDetail

trait MapNodeDetailBuilder {
  def build(routeType: RouteType, nodeId: Long): Option[MapNodeDetail]
}
