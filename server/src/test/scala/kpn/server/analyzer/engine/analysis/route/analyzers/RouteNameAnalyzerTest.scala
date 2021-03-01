package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawData
import kpn.api.custom.Fact.RouteNameMissing
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedRoute

class RouteNameAnalyzerTest extends UnitTest with SharedTestObjects {

  test("route name base on 'ref' tag") {
    val name = analyzeName(Tags.from("ref" -> "01-02"))
    name should equal(Some("01-02"))
  }

  test("route name base on 'name' tag") {
    val name = analyzeName(Tags.from("name" -> "01-02"))
    name should equal(Some("01-02"))
  }

  test("route name base on 'note' tag") {
    val name = analyzeName(Tags.from("note" -> "01-02"))
    name should equal(Some("01-02"))
  }

  test("route name base on 'note' tag with ignored comment") {
    val name = analyzeName(Tags.from("note" -> "01-02;ignored comment"))
    name should equal(Some("01-02"))
  }

  test("route name missing") {
    val context = analyze(Tags.empty)
    context.routeNameAnalysis.get.name should equal(None)
    context.facts should equal(Seq(RouteNameMissing))
  }

  private def analyzeName(tags: Tags): Option[String] = {
    val newContext = analyze(tags)
    newContext.routeNameAnalysis.get.name
  }

  private def analyze(tags: Tags): RouteAnalysisContext = {

    val standardRouteTags = Tags.from(
      "network" -> "rwn",
      "type" -> "route",
      "route" -> "foot",
      "network:type" -> "node_network"
    )

    val allTags = standardRouteTags ++ tags
    val relation = newRawRelation(11L, members = Seq.empty, tags = allTags)
    val rawData = RawData(None, Seq.empty, Seq.empty, Seq(relation))
    val data = new DataBuilder(rawData).data

    val loadedRoute = LoadedRoute(
      country = None,
      ScopedNetworkType.rwn,
      data,
      data.relations(11L)
    )

    val analysisContext = new AnalysisContext()

    val context = RouteAnalysisContext(
      analysisContext,
      loadedRoute,
      orphan = false,
      Map.empty
    )

    RouteNameAnalyzer.analyze(context)
  }

}
