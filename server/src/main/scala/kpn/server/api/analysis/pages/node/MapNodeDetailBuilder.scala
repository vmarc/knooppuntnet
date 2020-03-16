package kpn.server.api.analysis.pages.node

import kpn.api.common.node.MapNodeDetail
import kpn.api.custom.NetworkType

trait MapNodeDetailBuilder {
  def build(user: Option[String], networkType: NetworkType, nodeId: Long): Option[MapNodeDetail]
}
