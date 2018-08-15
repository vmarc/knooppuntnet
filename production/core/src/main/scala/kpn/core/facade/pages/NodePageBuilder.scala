package kpn.core.facade.pages

import kpn.shared.node.NodePage

trait NodePageBuilder {

  def build(user: Option[String], nodeId: Long): Option[NodePage]

}
