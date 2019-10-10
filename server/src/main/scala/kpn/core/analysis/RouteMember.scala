package kpn.core.analysis

import kpn.shared.data.Element
import kpn.shared.data.Node
import kpn.shared.route.RouteNetworkNodeInfo

trait RouteMember {
  def endNodes: Seq[Node]
  def memberType: String
  def linkName: String
  def nodes: Seq[RouteNetworkNodeInfo]
  def id: Long
  def role: Option[String]
  def element: Element
  def linkDescription: String
  def length: String
  def nodeCount: String
  def name: String
  def description: String

  def from: String
  def to: String

  def fromNode: Node
  def toNode: Node

  def accessible: Boolean
}
