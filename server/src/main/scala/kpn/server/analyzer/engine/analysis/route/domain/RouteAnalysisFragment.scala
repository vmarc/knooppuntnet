package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.core.analysis.Link

case class RouteAnalysisFragment(
  id: Long,
  way: Way,
  link: Link,
  role: Option[String],
  surface: String,
  nodeIds: Seq[Long]
) {
  def fromNodeId: Long = nodeIds.head

  def toNodeId: Long = nodeIds.last

  def nodes: Seq[Node] = {
    nodeIds.flatMap(nodeId => way.nodes.find(_.id == nodeId))
  }
}
