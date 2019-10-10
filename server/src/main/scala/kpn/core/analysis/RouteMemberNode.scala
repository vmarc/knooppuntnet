package kpn.core.analysis

import kpn.shared.data.Element
import kpn.shared.data.Node
import kpn.shared.route.RouteNetworkNodeInfo

case class RouteMemberNode(name: String, alternateName: String, number: String, role: Option[String], node: Node) extends RouteMember {
  val endNodes: Seq[Node] = Seq(node)
  def memberType: String = "node"
  def linkType: LinkType.Value = LinkType.NONE
  def linkName: String = "n"
  def nodes: Seq[RouteNetworkNodeInfo] = Seq(RouteNetworkNodeInfo(node.id, name, alternateName, node.latitude.toString, node.longitude.toString))
  def id: Long = node.id
  def element: Element = node
  def linkDescription: String = ""
  def length: String = ""
  def nodeCount: String = ""
  def description: String = ""
  def from: String = number
  def to: String = ""

  def fromNode: Node = node
  def toNode: Node = node

  def accessible: Boolean = true
}
