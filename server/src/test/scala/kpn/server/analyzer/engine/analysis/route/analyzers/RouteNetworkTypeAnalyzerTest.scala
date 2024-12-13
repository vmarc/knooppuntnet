package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.NetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class RouteNetworkTypeAnalyzerTest extends UnitTest {

  test("analyze route networkType") {
    networkTypes("hiking").shouldMatchTo(Seq(NetworkType.hiking))
    networkTypes("walking").shouldMatchTo(Seq(NetworkType.hiking))
    networkTypes("foot").shouldMatchTo(Seq(NetworkType.hiking))
    networkTypes("bicycle").shouldMatchTo(Seq(NetworkType.cycling))
    networkTypes("horse").shouldMatchTo(Seq(NetworkType.horseRiding))
    networkTypes("canoe").shouldMatchTo(Seq(NetworkType.canoe))
    networkTypes("motorboat").shouldMatchTo(Seq(NetworkType.motorboat))
    networkTypes("inline_skates").shouldMatchTo(Seq(NetworkType.inlineSkating))
    networkTypes("hiking;bicycle").shouldMatchTo(Seq(NetworkType.hiking, NetworkType.cycling))
    networkTypes("hiking; horse ").shouldMatchTo(Seq(NetworkType.hiking, NetworkType.horseRiding))
    networkTypes("hiking;bla").shouldMatchTo(Seq(NetworkType.hiking))
    networkTypes("bla").shouldMatchTo(Seq.empty)
  }

  private def networkTypes(routeTagValue: String): Seq[NetworkType] = {
    val tags = Tags.from("route" -> routeTagValue)
    new RouteNetworkTypeAnalyzer(tags).analyze()
  }
}
