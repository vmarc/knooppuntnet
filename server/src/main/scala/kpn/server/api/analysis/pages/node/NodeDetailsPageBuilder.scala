package kpn.server.api.analysis.pages.node

import kpn.api.common.Language
import kpn.api.common.node.NodeDetailsPage

trait NodeDetailsPageBuilder {
  def build(language: Language, nodeId: Long): Option[NodeDetailsPage]
}
