package kpn.server.api.analysis.pages.node

import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.node.NodeChangesPage

trait NodeChangesPageBuilder {
  def build(nodeId: Long, parameters: ChangesParameters): Option[NodeChangesPage]
}
