package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.node.NodeIntegrity
import kpn.core.mongo.doc.Label
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis


object NodeLabelsAnalyzer extends NodeAspectAnalyzer {
  def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    new NodeLabelsAnalyzer(analysis).analyze
  }
}

class NodeLabelsAnalyzer(analysis: NodeAnalysis) {

  def analyze: NodeAnalysis = {
    val basicLabels = buildBasicLabels()
    val factLabels = analysis.facts.map(fact => Label.fact(fact))
    val networkTypeLabels = analysis.nodeNames.map(name => Label.networkType(name.networkType))
    val integrityCheckLabels = buildIntegrityCheckLabels(analysis.integrity)
    val locationLabels = analysis.locations.map(location => Label.location(location))
    val labels = basicLabels ++ factLabels ++ networkTypeLabels ++ integrityCheckLabels ++ locationLabels
    analysis.copy(labels = labels)
  }

  private def buildBasicLabels(): Seq[String] = {
    Seq(
      if (analysis.active) Some(Label.active) else None,
      if (analysis.orphan) Some("orphan") else None,
      if (analysis.lastSurvey.isDefined) Some(Label.survey) else None,
      if (analysis.facts.nonEmpty) Some(Label.facts) else None,
    ).flatten
  }

  private def buildIntegrityCheckLabels(nodeIntegrityOption: Option[NodeIntegrity]): Seq[String] = {
    nodeIntegrityOption match {
      case None => Seq.empty
      case Some(nodeIntegrity) =>
        val networkTypes = nodeIntegrity.details.map(_.networkType).distinct
        networkTypes.flatMap { networkType =>
          val failed = nodeIntegrity.details.filter(_.networkType == networkType).exists(_.failed)
          Seq(
            Some(s"integrity-check-${networkType.name}"),
            if (failed) Some(s"integrity-check-failed-${networkType.name}") else None
          ).flatten
        }
    }
  }
}
