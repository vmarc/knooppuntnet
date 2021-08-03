package kpn.server.analyzer.engine.analysis.node.domain

import kpn.api.common.NodeName
import kpn.api.custom.Day
import kpn.api.custom.Fact

case class NodeTagAnalysis(
  name: String,
  nodeNames: Seq[NodeName],
  lastSurvey: Option[Day] = None,
  facts: Seq[Fact]
)
