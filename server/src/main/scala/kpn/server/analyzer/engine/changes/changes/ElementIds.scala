package kpn.server.analyzer.engine.changes.changes

case class ElementIds(
  nodeIds: Set[Long] = Set.empty,
  wayIds: Set[Long] = Set.empty,
  relationIds: Set[Long] = Set.empty
) {

  def isEmpty: Boolean = nodeIds.isEmpty && wayIds.isEmpty && relationIds.isEmpty

  def nonEmpty: Boolean = !isEmpty

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
