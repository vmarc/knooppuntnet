package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteNameMissing
import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawData
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeInfo

class RouteNameAnalyzerTest extends UnitTest with SharedTestObjects {

  test("route name based on 'ref' tag") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("ref" -> "01-02"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-02"),
        Some("01"),
        Some("02")
      )
    )
  }

  test("route name based on 'ref' tag - not normalized") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("ref" -> "1-2"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-02"),
        Some("01"),
        Some("02")
      )
    )
  }

  test("route name based on 'name' tag") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("name" -> "01-02"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-02"),
        Some("01"),
        Some("02")
      )
    )
  }

  test("route name based on 'name' tag - not normalized") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("name" -> "1-2"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-02"),
        Some("01"),
        Some("02")
      )
    )
  }

  test("route name based on 'note' tag") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("note" -> "01-02"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-02"),
        Some("01"),
        Some("02"),
        derivedFromDeprecatedNoteTag = true
      )
    )
  }

  test("route name based on 'note' tag - not normalized") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("note" -> "1-2"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-02"),
        Some("01"),
        Some("02"),
        derivedFromDeprecatedNoteTag = true
      )
    )
  }

  test("route name based on 'note' tag with ignored comment") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("note" -> "01-02;ignored comment"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-02"),
        Some("01"),
        Some("02"),
        derivedFromDeprecatedNoteTag = true
      )
    )
  }

  test("route name based on 'note' tag without dash") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("note" -> "bla"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis()
    )
  }

  test("route name based on 'from' and 'to' tag") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("from" -> "01", "to" -> "02"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-02"),
        Some("01"),
        Some("02")
      )
    )
  }

  test("route name based on 'from' and 'to' tag - not normalized") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("from" -> "1", "to" -> "2"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-02"),
        Some("01"),
        Some("02")
      )
    )
  }

  test("route name based on 'from' tag only") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("from" -> "01"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-"),
        Some("01"),
        None
      )
    )
  }

  test("route name based on 'from' tag only - not normalized") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("from" -> "1"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-"),
        Some("01"),
        None
      )
    )
  }

  test("route name based on 'to' tag only") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("to" -> "02"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("-02"),
        None,
        Some("02")
      )
    )
  }

  test("route name based on 'to' tag only - not normalized") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("to" -> "2"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("-02"),
        None,
        Some("02")
      )
    )
  }

  test("route name with non-numeric start- and end-node names") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("ref" -> "A2-A1"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("A2-A1"),
        Some("A1"),
        Some("A2"),
        reversed = true
      )
    )
  }

  test("route name with start- and end-node names reversed") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("ref" -> "02-01"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("02-01"),
        Some("01"),
        Some("02"),
        reversed = true
      )
    )
  }

  test("route name without dash to separate start- and end-node names") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("ref" -> "01/02"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01/02"),
        None,
        None
      )
    )
  }

  test("route name containing spaces") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("ref" -> " 01 - 02 "))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-02"),
        Some("01"),
        Some("02")
      )
    )
  }

  test("route name with start node name only") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("ref" -> "01-"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-"),
        Some("01"),
        None
      )
    )
  }

  test("route name with end node name only") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("ref" -> "-02"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("-02"),
        None,
        Some("02")
      )
    )
  }

  test("route name missing") {
    pendingRedesign()
    val context = analyze(Seq.empty)
    context._routeNameAnalysis should equal(Some(RouteNameAnalysis()))
    assertEqual(context.facts.toSet, Set(RouteNameMissing))
  }

  test("route name based on 'note' tag if route name from 'name' tag is 'non-standard'") {
    val routeNameAnalysis = analyzeRouteName(
      Tags.from(
        "name" -> "one-two",
        "note" -> "01-02",
      )
    )
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-02"),
        Some("01"),
        Some("02"),
        derivedFromDeprecatedNoteTag = true
      )
    )
  }

  test("route name from 'name' tag if route name from 'note' tag is not better") {
    val routeNameAnalysis = analyzeRouteName(
      Tags.from(
        "name" -> "one-two",
        "note" -> "three-four",
      )
    )
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("one-two"),
        Some("one"),
        Some("two")
      )
    )
  }

  test("route name based with node name containing dash") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("name" -> "start - end-node"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("start - end-node"),
        Some("start"),
        Some("end-node")
      )
    )
  }

  test("route name based nodes in ways and/or relation") {
    val routeNameAnalysis = analyzeRouteName(Tags.from("name" -> "start - end-node"))
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("start - end-node"),
        Some("start"),
        Some("end-node")
      )
    )
  }

  test("route name based on single node in ways and/or relation") {
    val routeNodeInfos: Map[Long, RouteNodeInfo] = Map(
      1001L -> RouteNodeInfo(
        newNode(1001),
        "01",
        None
      )
    )
    val routeNameAnalysis = analyzeRouteName(Seq.empty, routeNodeInfos)
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-01"),
        Some("01"),
        Some("01"),
        derivedFromNodes = true
      )
    )
  }

  test("route name based on single node (with dash in name) in ways and/or relation") {
    val routeNodeInfos: Map[Long, RouteNodeInfo] = Map(
      1001L -> RouteNodeInfo(
        newNode(1001),
        "node-name",
        None
      )
    )
    val routeNameAnalysis = analyzeRouteName(Seq.empty, routeNodeInfos)
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("node-name - node-name"),
        Some("node-name"),
        Some("node-name"),
        derivedFromNodes = true
      )
    )
  }

  test("route name based on two nodes in ways and/or relation") {
    val routeNodeInfos: Map[Long, RouteNodeInfo] = Map(
      1001L -> RouteNodeInfo(
        newNode(1001),
        "01",
        None
      ),
      1002L -> RouteNodeInfo(
        newNode(1002),
        "02",
        None
      )
    )
    val routeNameAnalysis = analyzeRouteName(Seq.empty, routeNodeInfos)
    assertEqual(
      routeNameAnalysis,
      RouteNameAnalysis(
        Some("01-02"),
        Some("01"),
        Some("02"),
        derivedFromNodes = true
      )
    )
  }

  private def analyzeRouteName(tags: Seq[Tag], routeNodeInfos: Map[Long, RouteNodeInfo] = Map.empty): RouteNameAnalysis = {
    pendingRedesignPrio2()
    val newContext = analyze(tags, routeNodeInfos)
    newContext.routeNameAnalysis
  }

  private def analyze(tags: Seq[Tag], routeNodeInfos: Map[Long, RouteNodeInfo] = Map.empty): BaseRouteAnalysisContext = {

    val standardRouteTags = Tags.from(
      "network" -> "rwn",
      "type" -> "route",
      "route" -> "foot",
      "network:type" -> "node_network"
    )

    val allTags = standardRouteTags ++ tags
    val rawRelation = newRawRelation(11L, members = Seq.empty, tags = allTags)
    val rawData = RawData(None, Seq.empty, Seq.empty, Seq(rawRelation))
    val relation = new DataBuilder(rawData).data.relations(11L)
    val context = BaseRouteAnalysisContext(
      relation,
      None,
      routeNodeInfos = routeNodeInfos
    )

    BaseRouteNameAnalyzer.analyze(context)
  }
}
