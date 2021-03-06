package kpn.server.analyzer.engine.changes.data

import kpn.server.analyzer.engine.changes.ElementIdMap

case class AnalysisDataDetail(
  watched: ElementIdMap = ElementIdMap()
) {

  def contains(id: Long): Boolean = watched.contains(id)

  def isReferencingNode(nodeId: Long): Boolean = {
    watched.isReferencingNode(nodeId)
  }

  def isReferencingRelation(relationId: Long): Boolean = {
    watched.isReferencingRelation(relationId)
  }

  def isEmpty: Boolean = watched.isEmpty
}
