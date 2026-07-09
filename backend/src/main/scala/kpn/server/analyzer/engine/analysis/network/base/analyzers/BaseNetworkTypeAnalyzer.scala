package kpn.server.analyzer.engine.analysis.network.base.analyzers

import kpn.api.custom.ScopedRouteType
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.base.analyzers.BaseNetworkTypeAnalyzer.log

object BaseNetworkTypeAnalyzer extends BaseNetworkAnalyzer {
  private val log = Log(classOf[BaseNetworkTypeAnalyzer])

  override def analyze(context: BaseNetworkAnalysisContext): BaseNetworkAnalysisContext = {
    new BaseNetworkTypeAnalyzer(context).analyze()
  }
}

class BaseNetworkTypeAnalyzer(context: BaseNetworkAnalysisContext) {
  def analyze(): BaseNetworkAnalysisContext = {
    context.relation.tagValue("network") match {
      case None =>
        log.info(s"Network ${context.relation.id} does not have 'network' type")
        context.copy(abort = true)
      case Some(key) =>
        ScopedRouteType.withKey(key) match {
          case None =>
            log.info(s"Network ${context.relation.id} has unsupported type '$key'")
            context.copy(abort = true)
          case Some(scopedRouteType) =>
            context.copy(
              _routeType = Some(scopedRouteType.routeType),
              _routeScope = Some(scopedRouteType.routeScope),
            )
        }
    }
  }
}
