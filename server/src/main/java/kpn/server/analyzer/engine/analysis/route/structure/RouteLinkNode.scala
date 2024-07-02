package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.data.Node

case class RouteLinkNode(role: Option[String], node: Node) extends RouteLink {

  def linkName: String = "n"

  def linkDetail: String = ""

  def pathIds: Seq[Long] = Seq.empty
}
