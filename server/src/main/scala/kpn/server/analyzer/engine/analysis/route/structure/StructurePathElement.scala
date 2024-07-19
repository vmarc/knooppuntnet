package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.data.Node

case class StructurePathElement(
  element: RouteAnalysisElement,
  reversed: Boolean
) {

  def nodes: Seq[Node] = {
    if (reversed) {
      element.nodes.reverse
    }
    else {
      element.nodes
    }
  }

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
