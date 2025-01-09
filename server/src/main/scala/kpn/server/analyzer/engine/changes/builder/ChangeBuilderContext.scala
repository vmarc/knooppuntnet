package kpn.server.analyzer.engine.changes.builder

import kpn.api.common.common.Ref
import kpn.core.analysis.Network
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.changes.ChangeSetContext

case class ChangeBuilderContext(
  changeSetContext: ChangeSetContext,
  routeAnalysisBefore: Seq[BaseRouteAnalysisContext],
  routeAnalysisAfter: Seq[BaseRouteAnalysisContext],
  networkBefore: Option[Network],
  networkAfter: Option[Network]
) {

  def networkRef: Option[Ref] = {
    networkAfter match {
      case Some(network) => Some(network.toRef)
      case None =>
        networkBefore match {
          case Some(network) => Some(network.toRef)
          case None => None
        }
    }
  }
}
