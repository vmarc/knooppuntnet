package kpn.server.analyzer.engine.changes.data

case class AnalysisDataNodes(
  watched: NodesData = NodesData()
) {

  def isEmpty: Boolean = watched.isEmpty

}
