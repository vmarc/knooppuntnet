package kpn.server.analyzer.engine.context

case class ChangeElementIds(
  nodeIds: Set[Long] = Set.empty,
  wayIds: Set[Long] = Set.empty,
  relationIds: Set[Long] = Set.empty
) {
  def size: Int = nodeIds.size + wayIds.size + relationIds.size
}
