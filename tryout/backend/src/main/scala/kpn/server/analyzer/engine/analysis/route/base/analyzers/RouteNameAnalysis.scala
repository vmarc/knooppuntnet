package kpn.server.analyzer.engine.analysis.route.base.analyzers

case class RouteNameAnalysis(
  name: Option[String] = None,
  startNodeName: Option[String] = None,
  endNodeName: Option[String] = None,
  reversed: Boolean = false,
  derivedFromNodes: Boolean = false,
  derivedFromDeprecatedNoteTag: Boolean = false
) {

  def hasStandardNodeNames: Boolean = {
    startNodeName match {
      case None => false
      case Some(startNodeNameValue) =>
        endNodeName match {
          case None => false
          case Some(endNodeNameValue) =>
            startNodeNameValue.forall(_.isDigit) && endNodeNameValue.forall(_.isDigit)
        }
    }
  }
}
