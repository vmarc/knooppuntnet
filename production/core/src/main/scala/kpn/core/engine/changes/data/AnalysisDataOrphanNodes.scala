package kpn.core.engine.changes.data

case class AnalysisDataOrphanNodes(
  watched: OrphanNodesData = OrphanNodesData(),
  ignored: OrphanNodesData = OrphanNodesData()
) {

  def isEmpty: Boolean = watched.isEmpty && ignored.isEmpty

}
