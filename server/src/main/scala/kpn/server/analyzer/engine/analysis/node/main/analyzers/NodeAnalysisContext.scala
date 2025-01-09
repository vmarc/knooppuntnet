package kpn.server.analyzer.engine.analysis.node.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.node.NodeIntegrity
import kpn.api.custom.Day
import kpn.core.doc.BaseNodeDoc

case class NodeAnalysisContext(
  node: BaseNodeDoc,
  active: Boolean = true,
  orphan: Boolean = false,
  lastSurvey: Option[Day] = None,
  facts: Seq[Fact] = Seq.empty,
  integrity: Option[NodeIntegrity] = None,
  labels: Seq[String] = Seq.empty,
  routeReferences: Seq[Reference] = Seq.empty,
  abort: Boolean = false
) {
  def routeTypes: Seq[RouteType] = {
    node.names.map(_.routeType).distinct
  }
}
