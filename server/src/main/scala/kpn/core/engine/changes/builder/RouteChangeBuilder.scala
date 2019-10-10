package kpn.core.engine.changes.builder

import kpn.shared.changes.details.RouteChange

trait RouteChangeBuilder {
  def build(context: ChangeBuilderContext): Seq[RouteChange]
}
