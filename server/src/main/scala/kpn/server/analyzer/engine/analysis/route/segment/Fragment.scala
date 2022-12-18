package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.analysis.route.RouteNode

object Fragment {
  def create(
    start: Option[RouteNode] = None,
    end: Option[RouteNode] = None,
    way: Way,
    nodeSubset: Vector[Node] = Vector.empty,
    role: Option[String] = None
  ): Fragment = {
    val nodes = if (nodeSubset.isEmpty) way.nodes else nodeSubset
    val meters = if (nodeSubset.isEmpty) way.length else Haversine.meters(nodeSubset.map(_.raw))
    val startNodeId = nodes.head.id
    val endNodeId = nodes.last.id
    Fragment(-1, start, end, way, nodeSubset, role, startNodeId, endNodeId, nodes, meters)
  }
}

/**
 * A route fragment is the smallest possible element from which the route path from the start to the end node is
 * composed.
 *
 * @param start      network node at the start of the route (if present in this fragment)
 * @param end        network node at the end of the route (if present in this fragment)
 * @param way        the way on which this fragment is based
 * @param nodeSubset the nodes in the way that form this fragment (empty collection if fragment is entire way)
 * @param role       the role of the way as member of the route relation
 */
case class Fragment(
  id: Int,
  start: Option[RouteNode] = None,
  end: Option[RouteNode] = None,
  way: Way,
  nodeSubset: Vector[Node] = Vector.empty,
  role: Option[String] = None,
  startNodeId: Long,
  endNodeId: Long,
  nodes: Seq[Node],
  meters: Long
)
