package kpn.server.analyzer.engine.analysis.network.base.analyzers

import kpn.api.common.data.raw.RawRelation
import kpn.server.analyzer.engine.context.PreconditionMissingException

case class BaseNetworkAnalysisContext(
  relation: RawRelation,
  _name: Option[Option[String]] = None,
) {

  def name: Option[String] = _name.getOrElse(throw new PreconditionMissingException)
}
