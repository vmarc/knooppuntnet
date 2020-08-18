package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.common.ToStringBuilder
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.analysis.route.RouteNode
import org.apache.commons.lang.builder.HashCodeBuilder

object Fragment {
  def create(
    start: Option[RouteNode] = None,
    end: Option[RouteNode] = None,
    way: Way,
    nodeSubset: Seq[Node] = Seq.empty,
    role: Option[String] = None
  ): Fragment = {
    val hashCodeBuilder = new HashCodeBuilder()
    hashCodeBuilder.append(start)
    hashCodeBuilder.append(end)
    hashCodeBuilder.append(way)
    hashCodeBuilder.append(nodeSubset)
    hashCodeBuilder.append(role)
    val id = hashCodeBuilder.toHashCode
    Fragment(id, start, end, way, nodeSubset, role)
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

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("id", id).
    field("start", start).
    field("end", end).
    field("way", way).
    field("nodeSubset", nodeSubset).
    field("role", role).
    build
}
