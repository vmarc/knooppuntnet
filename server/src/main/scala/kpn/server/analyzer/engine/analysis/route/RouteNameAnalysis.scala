package kpn.server.analyzer.engine.analysis.route

case class RouteNameAnalysis(
    name: Option[String] = None,
    startNodeName: Option[String] = None,
    endNodeName: Option[String] = None,
    reversed: Boolean = false) {

  def isStartNodeNameSameAsEndNodeName: Boolean = startNodeName.isDefined && endNodeName.isDefined && startNodeName == endNodeName
}
