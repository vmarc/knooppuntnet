package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.analysis.Link

case class NewRouteSegmentElementFragment(
  id: Long,
  wayId: Long,
  link: Link,
  role: Option[String],
  nodeIds: Seq[Long],
  pathIds: Seq[Long]
) {
  def fromNodeId: Long = nodeIds.head

  def toNodeId: Long = nodeIds.last
}
