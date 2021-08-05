package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.custom.ScopedNetworkType
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.info.analyzers.NetworkTypeAnalyzer.log
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

object NetworkTypeAnalyzer extends NetworkInfoAnalyzer {
  private val log = Log(classOf[NetworkTypeAnalyzer])

  def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {
    new NetworkTypeAnalyzer(context).analyze
  }
}

class NetworkTypeAnalyzer(context: NetworkInfoAnalysisContext) {

  def analyze: NetworkInfoAnalysisContext = {
    context.networkDoc.tags("network") match {
      case None =>
        log.info(s"Network ${context.networkDoc._id} does not have 'network' type")
        context.copy(abort = true)
      case Some(key) =>
        ScopedNetworkType.withKey(key) match {
          case None =>
            log.info(s"Network ${context.networkDoc._id} has unsupported type '$key'")
            context.copy(abort = true)
          case scopedNetworkTypeOption =>
            context.copy(
              scopedNetworkTypeOption = scopedNetworkTypeOption
            )
        }
    }
  }
}
