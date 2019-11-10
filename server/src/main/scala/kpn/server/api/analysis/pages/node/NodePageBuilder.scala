package kpn.server.api.analysis.pages.node

import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.node.NodeChangesPage
import kpn.api.common.node.NodeDetailsPage
import kpn.api.common.node.NodeMapPage

trait NodePageBuilder {

  def buildDetailsPage(user: Option[String], nodeId: Long): Option[NodeDetailsPage]

  def buildMapPage(user: Option[String], nodeId: Long): Option[NodeMapPage]

  def buildChangesPage(user: Option[String], nodeId: Long, parameters: ChangesParameters): Option[NodeChangesPage]

}
