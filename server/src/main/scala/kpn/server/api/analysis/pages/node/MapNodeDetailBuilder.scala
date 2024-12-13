package kpn.server.api.analysis.pages.node

import kpn.api.common.NetworkType
import kpn.api.common.node.MapNodeDetail

trait MapNodeDetailBuilder {
  def build(networkType: NetworkType, nodeId: Long): Option[MapNodeDetail]
}
