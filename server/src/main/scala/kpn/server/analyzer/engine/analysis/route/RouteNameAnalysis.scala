package kpn.server.analyzer.engine.analysis.route

case class RouteNameAnalysis(
  name: Option[String] = None,
  startNodeName: Option[String] = None,
  endNodeName: Option[String] = None,
  reversed: Boolean = false
) {

  def isStartNodeNameSameAsEndNodeName: Boolean = startNodeName.isDefined && endNodeName.isDefined && startNodeName == endNodeName

  def hasStandardNodeNames: Boolean = {
    startNodeName match {
      case None => false
      case Some(startNodeName) =>
        endNodeName match {
          case None => false
          case Some(endNodeName) =>
            startNodeName.forall(_.isDigit) && endNodeName.forall(_.isDigit)
        }
    }
  }
}
