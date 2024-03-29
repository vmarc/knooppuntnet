package kpn.server.analyzer.engine.context

case class ElementIds(
  nodeIds: Set[Long] = Set.empty,
  wayIds: Set[Long] = Set.empty,
  relationIds: Set[Long] = Set.empty
) {

  def isEmpty: Boolean = nodeIds.isEmpty && wayIds.isEmpty && relationIds.isEmpty

  def nonEmpty: Boolean = !isEmpty

  def size: Int = nodeIds.size + wayIds.size + relationIds.size

  def intersect(other: ElementIds): ElementIds = ElementIds(
    nodeIds.intersect(other.nodeIds),
    wayIds.intersect(other.wayIds),
    relationIds.intersect(other.relationIds)
  )

  def idString: String = {
    Seq(
      idString("nodeIds", nodeIds),
      idString("wayIds", wayIds),
      idString("relationIds", relationIds)
    ).flatten.mkString(", ")
  }

  private def idString(name: String, ids: Set[Long]): Option[String] = {
    if (ids.nonEmpty) {
      val sortedIds = ids.toSeq.sorted
      Some(name + "=" + sortedIds.mkString("+"))
    }
    else {
      None
    }
  }
}
