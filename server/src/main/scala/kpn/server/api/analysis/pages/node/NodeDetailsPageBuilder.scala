package kpn.server.api.analysis.pages.node

import kpn.api.common.node.NodeDetailsPage

trait NodeDetailsPageBuilder {
  def build(user: Option[String], nodeId: Long): Option[NodeDetailsPage]
}
