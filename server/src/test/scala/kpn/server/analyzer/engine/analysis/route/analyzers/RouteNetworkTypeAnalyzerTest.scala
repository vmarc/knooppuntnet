package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.NetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class RouteNetworkTypeAnalyzerTest extends UnitTest {

  test("analyze route networkType") {
    assertEqual(networkTypes("hiking"), Seq(NetworkType.hiking))
    assertEqual(networkTypes("walking"), Seq(NetworkType.hiking))
    assertEqual(networkTypes("foot"), Seq(NetworkType.hiking))
    assertEqual(networkTypes("bicycle"), Seq(NetworkType.cycling))
    assertEqual(networkTypes("horse"), Seq(NetworkType.horseRiding))
    assertEqual(networkTypes("canoe"), Seq(NetworkType.canoe))
    assertEqual(networkTypes("motorboat"), Seq(NetworkType.motorboat))
    assertEqual(networkTypes("inline_skates"), Seq(NetworkType.inlineSkating))
    assertEqual(networkTypes("hiking;bicycle"), Seq(NetworkType.hiking, NetworkType.cycling))
    assertEqual(networkTypes("hiking; horse "), Seq(NetworkType.hiking, NetworkType.horseRiding))
    assertEqual(networkTypes("hiking;bla"), Seq(NetworkType.hiking))
    assertEqual(networkTypes("bla"), Seq.empty)
  }

  private def networkTypes(routeTagValue: String): Seq[NetworkType] = {
    val tags = Tags.from("route" -> routeTagValue)
    new RouteNetworkTypeAnalyzer(tags).analyze()
  }
}
