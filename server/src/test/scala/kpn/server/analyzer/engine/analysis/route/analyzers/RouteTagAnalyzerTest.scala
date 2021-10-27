package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Fact.RouteTagInvalid
import kpn.api.custom.Fact.RouteTagMissing
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkScope.local
import kpn.api.custom.NetworkScope.national
import kpn.api.custom.NetworkScope.regional
import kpn.api.custom.NetworkType
import kpn.api.custom.NetworkType.canoe
import kpn.api.custom.NetworkType.cycling
import kpn.api.custom.NetworkType.hiking
import kpn.api.custom.NetworkType.horseRiding
import kpn.api.custom.NetworkType.inlineSkating
import kpn.api.custom.NetworkType.motorboat
import kpn.api.custom.Relation
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext

class RouteTagAnalyzerTest extends UnitTest with SharedTestObjects {

  test("route tag missing") {
    val context = analyze(
      Tags.from(
        "type" -> "route",
        "network:type" -> "node_network",
        "network" -> "rwn"
      )
    )
    context.facts should equal(Seq(RouteTagMissing))
  }

  test("route tag invalid") {
    val context = analyze(
      Tags.from(
        "type" -> "route",
        "network:type" -> "node_network",
        "network" -> "rwn",
        "route" -> "invalid"
      )
    )
    context.facts should equal(Seq(RouteTagInvalid))
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
    testValid(NetworkScope.regional, NetworkType.cycling, "bicycle;mtb")
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

  private def analyze(tags: Tags): RouteAnalysisContext = {

    val relation = Relation(newRawRelation(1L, tags = tags), Seq.empty)
    val context = RouteAnalysisContext(
      new AnalysisContext(),
      relation
    )

    RouteTagAnalyzer.analyze(context)
  }

}
