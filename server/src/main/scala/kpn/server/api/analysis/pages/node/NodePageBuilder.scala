package kpn.server.api.analysis.pages.node

import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.node.NodeChangesPage
import kpn.shared.node.NodeDetailsPage
import kpn.shared.node.NodeMapPage

trait NodePageBuilder {

  def buildDetailsPage(user: Option[String], nodeId: Long): Option[NodeDetailsPage]

  def buildMapPage(user: Option[String], nodeId: Long): Option[NodeMapPage]

  def buildChangesPage(user: Option[String], nodeId: Long, parameters: ChangesParameters): Option[NodeChangesPage]

}
