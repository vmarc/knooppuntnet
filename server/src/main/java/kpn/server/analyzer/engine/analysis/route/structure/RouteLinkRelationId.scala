package kpn.server.analyzer.engine.analysis.route.structure

case class RouteLinkRelationId(role: Option[String], relationId: Long) extends RouteLink {

  def linkName: String = "n"

  def linkDetail: String = ""
}
