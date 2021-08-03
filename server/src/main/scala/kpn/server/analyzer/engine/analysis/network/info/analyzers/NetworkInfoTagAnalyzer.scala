package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import org.springframework.stereotype.Component

@Component
class NetworkInfoTagAnalyzer(
  analysisContext: AnalysisContext
) extends NetworkInfoAnalyzer {

  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {

    val scopedNetworkType = determineScopedNetworkType(context.networkDoc.tags)
    // TODO MONGO other tag related analysis
    // TODO MONGO determine network name here - should not rely on NetworkDoc
    context.copy(
      scopedNetworkTypeOption = Some(scopedNetworkType)
    )
  }

  private def determineScopedNetworkType(tags: Tags): ScopedNetworkType = {
    tags("network") match {
      case None => throw new IllegalStateException("xxx")
      case Some(key) =>
        ScopedNetworkType.withKey(key) match {
          case None => throw new IllegalStateException("xxx")
          case Some(value) => value
        }
    }
  }
}
