package kpn.server.api.analysis.pages.node

import kpn.api.common.node.NodeMapPage

trait NodeMapPageBuilder {
  def build(user: Option[String], nodeId: Long): Option[NodeMapPage]
}
