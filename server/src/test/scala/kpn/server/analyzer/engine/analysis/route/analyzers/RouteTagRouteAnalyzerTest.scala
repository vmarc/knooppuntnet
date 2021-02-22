package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawData
import kpn.api.custom.Fact.RouteTagInvalid
import kpn.api.custom.Fact.RouteTagMissing
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute

class RouteTagRouteAnalyzerTest extends UnitTest with SharedTestObjects {

  test("route tag missing") {
    val context = analyze(ScopedNetworkType.rwn, Tags.empty)
    context.facts should equal(Seq(RouteTagMissing))
  }

  test("route tag invalid") {
    val context = analyze(ScopedNetworkType.rwn, Tags.from("route" -> "invalid"))
    context.facts should equal(Seq(RouteTagInvalid))
  }

  test("route tag valid") {
    testValid(NetworkScope.local, NetworkType.hiking, "foot")
    testValid(NetworkScope.regional, NetworkType.hiking, "hiking")
    testValid(NetworkScope.national, NetworkType.hiking, "walking")
    testValid(NetworkScope.local, NetworkType.cycling, "bicycle")
    testValid(NetworkScope.regional, NetworkType.horseRiding, "horse")
    testValid(NetworkScope.national, NetworkType.canoe, "canoe")
    testValid(NetworkScope.local, NetworkType.motorboat, "motorboat")
    testValid(NetworkScope.regional, NetworkType.inlineSkating, "inline_skates")
  }

  def testValid(networkScope: NetworkScope, networkType: NetworkType, tagValue: String) {
    val scopedNetworkType = ScopedNetworkType(networkScope, networkType)
    val context = analyze(scopedNetworkType, Tags.from("route" -> tagValue))
    context.facts should equal(Seq())
  }

  private def analyze(scopedNetworkType: ScopedNetworkType, tags: Tags): RouteAnalysisContext = {

    val relation = newRawRelation(1L, tags = tags)
    val rawData = RawData(None, Seq.empty, Seq.empty, Seq(relation))
    val data = new DataBuilder(rawData).data

    val loadedRoute = LoadedRoute(
      country = None,
      scopedNetworkType,
      "",
      data,
      data.relations(1L)
    )

    val context = RouteAnalysisContext(
      new AnalysisContext(),
      loadedRoute,
      orphan = false,
      Map.empty
    )

    RouteTagRouteAnalyzer.analyze(context)
  }

}