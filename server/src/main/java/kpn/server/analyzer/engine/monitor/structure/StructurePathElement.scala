package kpn.server.analyzer.engine.monitor.structure

case class StructurePathElement(
  element: StructureElement,
  reversed: Boolean
) {

  def nodeIds: Seq[Long] = {
    if (reversed) {
      element.nodeIds.reverse
    }
    else {
      element.nodeIds
    }
  }

  def startNodeId: Long = {
    if (reversed) {
      element.nodeIds.last
    }
    else {
      element.nodeIds.head
    }
  }

  def endNodeId: Long = {
    if (reversed) {
      element.nodeIds.head
    }
    else {
      element.nodeIds.last
    }
  }
}