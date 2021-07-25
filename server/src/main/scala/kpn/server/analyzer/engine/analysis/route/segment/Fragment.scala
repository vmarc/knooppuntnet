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
    nodeSubset: Seq[Node] = Seq.empty,
    role: Option[String] = None
  ): Fragment = {
    Fragment(-1, start, end, way, nodeSubset, role)
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
  nodeSubset: Seq[Node] = Seq.empty,
  role: Option[String] = None
) {

  def nodes: Seq[Node] = if (nodeSubset.isEmpty) way.nodes else nodeSubset

  def meters: Long = if (nodeSubset.isEmpty) way.length else Haversine.meters(nodeSubset.map(_.raw))

}
