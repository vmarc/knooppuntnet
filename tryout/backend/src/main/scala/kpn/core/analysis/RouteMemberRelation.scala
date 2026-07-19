package kpn.core.analysis

import kpn.api.common.data.Element
import kpn.api.common.data.Node
import kpn.api.common.route.Link
import kpn.api.common.route.RouteNetworkNodeInfo

case class RouteMemberRelation(
  id: Long,
  role: Option[String],
) extends RouteMember {

  def memberType: String = "relation"

  override def endNodes: Seq[Node] = Seq.empty

  override def link: Option[Link] = None

  override def linkName: String = ""

  override def nodes: Seq[RouteNetworkNodeInfo] = Seq.empty

  override def element: Element = null

  override def linkDescription: String = ""

  override def length: String = ""

  override def nodeCount: String = ""

  override def name: String = ""

  override def description: String = ""

  override def from: String = ""

  override def to: String = ""

  override def fromNode: Node = null

  override def toNode: Node = null

  override def accessible: Boolean = false
}
