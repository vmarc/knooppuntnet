package kpn.server.analyzer.engine.analysis.route.domain

case class RouteLinkRelationId(role: Option[String], relationId: Long) extends RouteLink {

  def linkName: String = "n"

  def linkDetail: String = ""
}
