package kpn.server.analyzer.engine.analysis.network.base.analyzers

import kpn.api.common.NetworkScope
import kpn.api.common.RouteType
import kpn.api.common.data.raw.RawRelation
import kpn.server.analyzer.engine.context.PreconditionMissingException

case class BaseNetworkAnalysisContext(
  relation: RawRelation,
  _name: Option[Option[String]] = None,
  _routeType: Option[RouteType] = None,
  _networkScope: Option[NetworkScope] = None,
  abort: Boolean = false
) {

  def name: Option[String] = _name.getOrElse(throw new PreconditionMissingException)

  def routeType: RouteType = _routeType.getOrElse(throw new PreconditionMissingException)

  def networkScope: NetworkScope = _networkScope.getOrElse(throw new PreconditionMissingException)
}
