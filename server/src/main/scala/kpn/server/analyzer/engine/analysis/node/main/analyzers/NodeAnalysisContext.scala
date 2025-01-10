package kpn.server.analyzer.engine.analysis.node.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.node.NodeIntegrity
import kpn.core.doc.BaseNodeDoc
import kpn.server.analyzer.engine.context.PreconditionMissingException

case class NodeAnalysisContext(
  node: BaseNodeDoc,
  active: Boolean = true,
  orphan: Boolean = false,
  facts: Seq[Fact] = Seq.empty,
  _integrity: Option[Option[NodeIntegrity]] = None,
  _labels: Option[Seq[String]] = None,
  _routeReferences: Option[Seq[Reference]] = None,
  _networkReferences: Option[Seq[Reference]] = None,
  abort: Boolean = false
) {

  def routeTypes: Seq[RouteType] = {
    node.names.map(_.routeType).distinct
  }

  def integrity: Option[NodeIntegrity] = _integrity.getOrElse(throw new PreconditionMissingException)

  def labels: Seq[String] = _labels.getOrElse(throw new PreconditionMissingException)

  def routeReferences: Seq[Reference] = _routeReferences.getOrElse(throw new PreconditionMissingException)

  def networkReferences: Seq[Reference] = _networkReferences.getOrElse(throw new PreconditionMissingException)
}
