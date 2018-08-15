package kpn.shared.diff.network

import kpn.shared.common.Ref

case class NodeRouteReferenceDiffs(
  removed: Seq[Ref],
  added: Seq[Ref],
  remaining: Seq[Ref]
) {
  def nonEmpty: Boolean = removed.nonEmpty || added.nonEmpty || remaining.nonEmpty
}

