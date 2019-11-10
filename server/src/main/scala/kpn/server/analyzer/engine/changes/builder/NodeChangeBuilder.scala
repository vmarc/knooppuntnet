package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.changes.details.NodeChange

trait NodeChangeBuilder {
  def build(context: ChangeBuilderContext): Seq[NodeChange]
}
