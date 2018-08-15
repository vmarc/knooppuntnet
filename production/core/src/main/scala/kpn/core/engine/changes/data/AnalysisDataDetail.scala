package kpn.core.engine.changes.data

import kpn.core.engine.changes.ElementIdMap

case class AnalysisDataDetail(
  watched: ElementIdMap = ElementIdMap(),
  ignored: ElementIdMap = ElementIdMap()
) {

  def contains(id: Long): Boolean = watched.contains(id) || ignored.contains(id)

  def isReferencingNode(nodeId: Long): Boolean = {
    watched.isReferencingNode(nodeId) || ignored.isReferencingNode(nodeId)
  }

  def isReferencingRelation(relationId: Long): Boolean = {
    watched.isReferencingRelation(relationId) || ignored.isReferencingRelation(relationId)
  }

  def isEmpty: Boolean = watched.isEmpty && ignored.isEmpty
}
