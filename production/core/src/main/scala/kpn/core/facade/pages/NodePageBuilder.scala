package kpn.core.facade.pages

import kpn.shared.node.NodeChangesPage
import kpn.shared.node.NodeDetailsPage
import kpn.shared.node.NodeMapPage
import kpn.shared.node.NodePage

trait NodePageBuilder {

  def build(user: Option[String], nodeId: Long): Option[NodePage]

  def buildDetailsPage(user: Option[String], nodeId: Long): Option[NodeDetailsPage]

  def buildMapPage(user: Option[String], nodeId: Long): Option[NodeMapPage]

  def buildChangesPage(user: Option[String], nodeId: Long, itemsPerPage: Int, pageIndex: Int): Option[NodeChangesPage]

}
