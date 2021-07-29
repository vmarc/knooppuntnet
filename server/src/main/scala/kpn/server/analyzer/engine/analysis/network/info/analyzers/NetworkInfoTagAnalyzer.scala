package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.custom.NetworkScope
import kpn.api.custom.ScopedNetworkType
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import org.springframework.stereotype.Component

@Component
class NetworkInfoTagAnalyzer(
  analysisContext: AnalysisContext
) extends NetworkInfoAnalyzer {

  override def analyze(context: NetworkInfoAnalysisContext): NetworkInfoAnalysisContext = {

    // TODO MONGO determine scopedNetworkType from tags instead! What to do if not found? abort analysis?
    val networkScope = if (context.networkDoc.networkScope == null) {
      NetworkScope.regional
    }
    else {
      context.networkDoc.networkScope
    }
    val scopedNetworkType = ScopedNetworkType(networkScope, context.networkDoc.networkType)

    // TODO MONGO other tag related analysis
    // TODO MONGO determine network name here - should not rely on NetworkDoc

    context.copy(
      scopedNetworkType = scopedNetworkType
    )
  }
}
