package kpn.core.analysis

import kpn.api.common.data.Element
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.common.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.WayAnalyzer

case class RouteMemberWay(
  name: String,
  link: Option[Link],
  role: Option[String],
  way: Way,
  fromNode: Node,
  toNode: Node,
  from: String,
  to: String,
  accessible: Boolean,
  routeNodes: Seq[RouteNode]
) extends RouteMember {

  def memberType: String = "way"

  val endNodes: Seq[Node] = {
    if (way.nodes.isEmpty) {
      Seq.empty
    }
    else if (WayAnalyzer.isRoundabout(way) || WayAnalyzer.isClosedLoop(way)) {
      way.nodes
    }
    else {
      Seq(way.nodes.head, way.nodes.last)
    }
  }

  def linkName: String = link.map(_.name).getOrElse("")

  def nodes: Seq[RouteNetworkNodeInfo] = routeNodes.map { rn =>
    RouteNetworkNodeInfo(
      rn.nodeId,
      rn.name,
      rn.alternateName,
      None, // TODO redesign
      "TODO rn.latitude",
      "TODO rn.longitude"
    )
  }

  def id: Long = way.id

  def element: Element = way

  def linkDescription: String = link.map(_.description).getOrElse("")

  def length: String = s"${way.length.toString} m"

  def nodeCount: String = way.nodes.size.toString

  def description: String = name
}
