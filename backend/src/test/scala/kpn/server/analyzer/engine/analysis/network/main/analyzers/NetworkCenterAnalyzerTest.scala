package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.LatLonImpl
import kpn.api.custom.Timestamp
import kpn.core.test.TestObjects.newBaseNetworkDoc
import kpn.core.test.TestObjects.newNetworkInfoNodeDetail
import kpn.core.util.UnitTest

class NetworkCenterAnalyzerTest extends UnitTest {

  test("center") {
    val initialContext = NetworkAnalysisContext(
      analysisTimestamp = Timestamp.analysisStart,
      network = newBaseNetworkDoc(1L),
      _nodeDetails = Some(
        Seq(
          newNetworkInfoNodeDetail(1001L, latitude = "1", longitude = "1"),
          newNetworkInfoNodeDetail(1002L, latitude = "3", longitude = "3"),
        )
      )
    )
    val context = NetworkCenterAnalyzer.analyze(initialContext)
    context.center should equal(Some(LatLonImpl("2.0", "2.0")))
  }
}
