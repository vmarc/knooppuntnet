package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.data.Way
import kpn.core.analysis.Link

case class RouteLinkWay(link: Link, role: Option[String], way: Way) extends RouteLink {

  def linkName: String = link.name

  def linkDetail: String = link.reportString
}
