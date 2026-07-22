package kpn.server.analyzer.engine.context

import kpn.api.id.Storable

case class ChangeElementIds(
  nodeIds: Set[Long] = Set.empty,
  wayIds: Set[Long] = Set.empty,
  relationIds: Set[Long] = Set.empty
) extends Storable {
  def size: Int = nodeIds.size + wayIds.size + relationIds.size
}
