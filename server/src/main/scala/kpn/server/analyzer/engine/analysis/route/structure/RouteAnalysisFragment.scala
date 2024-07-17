package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.core.analysis.Link

object RouteAnalysisFragment {
  def apply(
    id: Long,
    way: Way,
    link: Link,
    role: Option[String],
    surface: String,
    nodeIds: Seq[Long]
  ): RouteAnalysisFragment = {
    val nodes = if (nodeIds.size == way.nodes.size) {
      way.nodes
    } else {
      nodeIds.flatMap { nodeId => way.nodes.find(_.id == nodeId) }
    }
    RouteAnalysisFragment(
      id,
      way,
      nodes,
      link,
      role,
      surface,
      nodeIds
    )
  }
}

case class RouteAnalysisFragment(
  id: Long,
  way: Way,
  nodes: Seq[Node],
  link: Link,
  role: Option[String],
  surface: String,
  nodeIds: Seq[Long]
) {
  def fromNodeId: Long = nodeIds.head

  def toNodeId: Long = nodeIds.last
}
