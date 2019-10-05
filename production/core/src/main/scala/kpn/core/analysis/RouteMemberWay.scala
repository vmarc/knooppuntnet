package kpn.core.analysis

import kpn.core.engine.analysis.WayAnalyzer
import kpn.shared.data.Element
import kpn.shared.data.Node
import kpn.shared.data.Way
import kpn.core.engine.analysis.route.RouteNode
import kpn.shared.route.RouteNetworkNodeInfo

case class RouteMemberWay(
  name: String,
  link: Link,
  role: Option[String],
  way: Way,
  fromNode: Node,
  toNode: Node,
  from: String,
  to:String,
  accessible: Boolean,
  routeNodes: Seq[RouteNode]
) extends RouteMember {

  def memberType: String = "way"

  val endNodes: Seq[Node] = {
    if (way.nodes.isEmpty) {
      Seq()
    }
    else if (WayAnalyzer.isRoundabout(way) || WayAnalyzer.isClosedLoop(way)) {
      way.nodes
    }
    else {
      Seq(way.nodes.head, way.nodes.last)
    }
  }

  def linkName: String = link.name
  def nodes: Seq[RouteNetworkNodeInfo] = routeNodes.map { rn =>
    RouteNetworkNodeInfo(
      rn.id,
      rn.name,
      rn.alternateName,
      rn.node.latitude.toString,
      rn.node.longitude.toString
    )
  }
  def id: Long = way.id
  def element: Element = way
  def linkDescription: String = link.description
  def length: String = way.length.toString + " m"
  def nodeCount: String = way.nodes.size.toString
  def description: String = name
}
