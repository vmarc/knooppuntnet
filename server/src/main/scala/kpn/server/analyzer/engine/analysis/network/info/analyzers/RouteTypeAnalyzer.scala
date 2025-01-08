package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.custom.ScopedRouteType
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.info.analyzers.RouteTypeAnalyzer.log
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

object RouteTypeAnalyzer extends NetworkInfoAnalyzer {
  private val log = Log(classOf[RouteTypeAnalyzer])

  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    new RouteTypeAnalyzer(context).analyze
  }
}

class RouteTypeAnalyzer(context: NetworkInfoAnalysisContext) {

  def analyze: NetworkInfoAnalysisContext = {
    context.networkDoc.tagValue("network") match {
      case None =>
        log.info(s"Network ${context.networkDoc._id} does not have 'network' type")
        context.copy(abort = true)
      case Some(key) =>
        ScopedRouteType.withKey(key) match {
          case None =>
            log.info(s"Network ${context.networkDoc._id} has unsupported type '$key'")
            context.copy(abort = true)
          case scopedRouteTypeOption =>
            context.copy(
              scopedRouteTypeOption = scopedRouteTypeOption
            )
        }
    }
  }
}
