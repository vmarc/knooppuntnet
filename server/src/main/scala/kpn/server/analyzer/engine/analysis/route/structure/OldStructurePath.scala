package kpn.server.analyzer.engine.analysis.route.structure

case class OldStructurePath(
  startNodeId: Long,
  endNodeId: Long,
  elements: Seq[OldStructurePathElement] = Seq.empty,
) {
  def nodeIds: Seq[Long] = {
    elements.headOption match {
      case Some(firstElement) => firstElement.nodeIds.head +: elements.flatMap(_.nodeIds.tail)
      case None => Seq.empty
    }
  }
}
