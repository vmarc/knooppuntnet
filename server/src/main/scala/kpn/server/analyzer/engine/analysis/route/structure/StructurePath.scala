package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.data.Node
import kpn.core.util.Haversine

case class StructurePath(
  id: Long,
  startNodeId: Long,
  endNodeId: Long,
  elements: Seq[StructurePathElement] = Seq.empty,
) {

  def nodes: Seq[Node] = {
    elements.headOption match {
      case Some(firstElement) => firstElement.nodes.head +: elements.flatMap(_.nodes.tail)
      case None => Seq.empty
    }
  }

  def nodeIds: Seq[Long] = {
    elements.headOption match {
      case Some(firstElement) => firstElement.nodeIds.head +: elements.flatMap(_.nodeIds.tail)
      case None => Seq.empty
    }
  }

  def elementIds: Seq[Long] = {
    elements.map(_.element.id)
  }

  def meters: Long = {
    Haversine.meters(nodes)
  }
}
