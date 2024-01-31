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
}