package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.NetworkType
import kpn.api.common.NetworkType.canoe
import kpn.api.common.NetworkType.cycling
import kpn.api.common.NetworkType.hiking
import kpn.api.common.NetworkType.horseRiding
import kpn.api.common.NetworkType.inlineSkating
import kpn.api.common.NetworkType.motorboat
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Fact.RouteTagMissing
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkScope.local
import kpn.api.custom.NetworkScope.national
import kpn.api.custom.NetworkScope.regional
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.util.Redesign
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class RouteTagAnalyzerTest extends UnitTest with SharedTestObjects {

  test("route tag missing") {
    val context = analyze(
      Tags.from(
        "type" -> "route",
        "network:type" -> "node_network",
        "network" -> "rwn"
      )
    )
    context.facts.shouldMatchTo(Seq(RouteTagMissing))
  }

  test("route tag valid") {
    testValid(local, hiking, "foot")
    testValid(regional, hiking, "hiking")
    testValid(national, hiking, "walking")
    testValid(local, cycling, "bicycle")
    testValid(regional, horseRiding, "horse")
    testValid(national, canoe, "canoe")
    testValid(local, motorboat, "motorboat")
    testValid(regional, inlineSkating, "inline_skates")
  }

  test("ignore additional values") {
    if (Redesign.enablePendingTests) {
      testValid(NetworkScope.regional, NetworkType.cycling, "bicycle;mtb")
    }
  }

  private def testValid(networkScope: NetworkScope, networkType: NetworkType, tagValue: String): Unit = {
    val scopedNetworkType = ScopedNetworkType(networkScope, networkType)
    val tags = Tags.from(
      "type" -> "route",
      "network:type" -> "node_network",
      "network" -> scopedNetworkType.key,
      "route" -> tagValue
    )
    val context = analyze(tags)
    context.facts shouldBe empty
  }

  private def analyze(tags: Seq[Tag]): RouteDetailAnalysisContext = {
    val relation = newRelation(1L, tags = tags)
    val context = RouteDetailAnalysisContext(
      relation,
      None
    )
    RouteTagAnalyzer.analyze(context)
  }
}
