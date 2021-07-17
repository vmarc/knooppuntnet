package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.node.NodeIntegrity
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis


object NodeLabelsAnalyzer extends NodeAspectAnalyzer {
  def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    new NodeLabelsAnalyzer(analysis).analyze
  }
}

class NodeLabelsAnalyzer(analysis: NodeAnalysis) {

  def analyze: NodeAnalysis = {
    val basicLabels = buildBasicLabels()
    val factLabels = analysis.facts.map(fact => s"fact-${fact.name}")
    val networkTypeLabels = analysis.nodeNames.map(name => s"network-type-${name.networkType.name}")
    val integrityCheckLabels = buildIntegrityCheckLabels(analysis.integrity)
    val locationLabels = analysis.locations.map(location => s"location-$location")
    val labels = basicLabels ++ factLabels ++ networkTypeLabels ++ integrityCheckLabels ++ locationLabels
    analysis.copy(labels = labels)
  }

  private def buildBasicLabels(): Seq[String] = {
    Seq(
      if (analysis.active) Some("active") else None,
      if (analysis.orphan) Some("orphan") else None,
      if (analysis.lastSurvey.isDefined) Some("survey") else None,
      if (analysis.facts.nonEmpty) Some("facts") else None,
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
