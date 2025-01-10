package kpn.server.analyzer.engine.analysis.node.main.analyzers

import kpn.api.common.node.NodeIntegrity
import kpn.core.doc.Label

object NodeLabelsAnalyzer extends NodeAnalyzer {
  def analyze(context: NodeAnalysisContext): NodeAnalysisContext = {
    new NodeLabelsAnalyzer(context).analyze
  }
}

class NodeLabelsAnalyzer(context: NodeAnalysisContext) {

  def analyze: NodeAnalysisContext = {
    val basicLabels = buildBasicLabels()
    val factLabels = context.facts.map(fact => Label.fact(fact))
    val routeTypeLabels = context.node.names.map(name => Label.routeType(name.routeType)).distinct
    val integrityCheckLabels = buildIntegrityCheckLabels(context.integrity)
    val locationLabels = context.node.locations.map(location => Label.location(location))
    val labels = basicLabels ++ factLabels ++ routeTypeLabels ++ integrityCheckLabels ++ locationLabels
    context.copy(_labels = Some(labels))
  }

  private def buildBasicLabels(): Seq[String] = {
    Seq(
      if (context.active) Some(Label.active) else None,
      if (context.orphan) Some("orphan") else None,
      if (context.node.lastSurvey.isDefined) Some(Label.survey) else None,
      if (context.facts.nonEmpty) Some(Label.facts) else None,
    ).flatten
  }

  private def buildIntegrityCheckLabels(nodeIntegrityOption: Option[NodeIntegrity]): Seq[String] = {
    nodeIntegrityOption match {
      case None => Seq.empty
      case Some(nodeIntegrity) =>
        val routeTypes = nodeIntegrity.details.map(_.routeType).distinct
        routeTypes.flatMap { routeType =>
          val failed = nodeIntegrity.details.filter(_.routeType == routeType).exists(_.failed)
          Seq(
            Some(s"integrity-check-${routeType.entryName}"),
            if (failed) Some(s"integrity-check-failed-${routeType.entryName}") else None
          ).flatten
        }
    }
  }
}
