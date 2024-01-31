package kpn.server.analyzer.engine.monitor.structure

case class StructurePath(
  startNodeId: Long,
  endNodeId: Long,
  elements: Seq[StructurePathElement] = Seq.empty,
) {
  def nodeIds: Seq[Long] = {
    elements.headOption match {
      case Some(firstElement) => firstElement.nodeIds.head +: elements.flatMap(_.nodeIds.tail)
      case None => Seq.empty
    }
  }
}