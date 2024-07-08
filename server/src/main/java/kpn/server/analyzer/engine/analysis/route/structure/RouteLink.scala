package kpn.server.analyzer.engine.analysis.route.structure

trait RouteLink {

  def idString: String = ""

  def linkName: String

  def linkDetail: String

  def role: Option[String]
}
