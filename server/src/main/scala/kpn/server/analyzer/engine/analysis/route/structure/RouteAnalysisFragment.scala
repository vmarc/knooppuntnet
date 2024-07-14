package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.analysis.Link

case class RouteAnalysisFragment(
  id: Long,
  wayId: Long,
  link: Link,
  role: Option[String],
  surface: String,
  nodeIds: Seq[Long]
) {
  def fromNodeId: Long = nodeIds.head

  def toNodeId: Long = nodeIds.last
}
