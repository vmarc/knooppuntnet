package kpn.server.analyzer.engine.analysis.network.info.analyzers

import kpn.api.common.LatLonImpl
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Timestamp
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

class NetworkCenterAnalyzerTest extends UnitTest with SharedTestObjects {

  test("center") {
    val initialContext = NetworkInfoAnalysisContext(
      analysisTimestamp = Timestamp.analysisStart,
      networkDoc = newNetwork(1L),
      nodeDetails = Seq(
        newNetworkInfoNodeDetail(1001L, latitude = "1", longitude = "1"),
        newNetworkInfoNodeDetail(1002L, latitude = "3", longitude = "3"),
      )
    )
    val context = NetworkCenterAnalyzer.analyze(initialContext)
    context.center should equal(Some(LatLonImpl("2.0", "2.0")))
  }
}
