package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerFixed
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzerMock
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.tile.OldLinesTileCalculatorImpl
import kpn.server.analyzer.engine.tile.OldTileCalculatorImpl
import kpn.server.analyzer.engine.tile.RouteTileCalculatorImpl
import kpn.server.repository.RouteRepository
import org.scalamock.scalatest.MockFactory
import org.scalatest.Assertions

import scala.collection.mutable.ListBuffer

class RouteAnalysisInspector extends MockFactory with SharedTestObjects {

  private val factsBuffer = ListBuffer[Fact]()
  private val startNodeIdBuffer = ListBuffer[Long]()
  private val endNodeIdBuffer = ListBuffer[Long]()
  private val startTentacleNodeIdBuffer = ListBuffer[Long]()
  private val endTentacleNodeIdBuffer = ListBuffer[Long]()
  private val unexpectedNodeIdBuffer = ListBuffer[Long]()
  private val forwardNodeIdBuffer = ListBuffer[Long]()
  private val backwardNodeIdBuffer = ListBuffer[Long]()
  private val tentacleBuffer = ListBuffer[Seq[Long]]()
  private val structureBuffer = ListBuffer[String]()

  def fact(fact: Fact): Unit = factsBuffer.append(fact)

  def startNode(nodeId: Long): Unit = startNodeIdBuffer.append(nodeId)

  def endNode(nodeId: Long): Unit = endNodeIdBuffer.append(nodeId)

  def startTentacleNode(nodeId: Long): Unit = startTentacleNodeIdBuffer.append(nodeId)

  def endTentacleNode(nodeId: Long): Unit = endTentacleNodeIdBuffer.append(nodeId)

  def unexpectedNode(nodeId: Long): Unit = unexpectedNodeIdBuffer.append(nodeId)

  def forward(nodeIds: Long*): Unit = forwardNodeIdBuffer.appendAll(nodeIds)

  def backward(nodeIds: Long*): Unit = backwardNodeIdBuffer.appendAll(nodeIds)

  def tentacle(nodeIds: Long*): Unit = tentacleBuffer.append(nodeIds)

  def structure(segmentString: String): Unit = structureBuffer.append(segmentString)

  def analyze(d: RouteTestData): Unit = {

    val tagValues = Seq(
      Some("type" -> "route"),
      Some("network:type" -> "node_network"),
      Some("network" -> d.scopedNetworkType.key),
      Some("route" -> d.scopedNetworkType.networkType.routeTagValues.head),
      if (d.routeName.nonEmpty) Some("ref" -> d.routeName) else None
    ).flatten

    val tags = Tags.from(tagValues *) ++ d.routeTags

    val rr: RawRelation = newRawRelation(10, members = d.members, tags = tags)
    val rawData = RawData(None, d.nodes, d.ways, Seq(rr))
    val data = new DataBuilder(rawData).data
    val relation = data.relations(rr.id)

    val tileCalculator = new OldTileCalculatorImpl()
    val linesTileCalculator = new OldLinesTileCalculatorImpl(tileCalculator)
    val routeTileCalculator = new RouteTileCalculatorImpl(linesTileCalculator)
    val routeTileAnalyzer = new RouteTileAnalyzer(routeTileCalculator)
    val locationAnalyzer = new LocationAnalyzerFixed()
    val routeRepository = stub[RouteRepository]
    val routeCountryAnalyzer = new RouteCountryAnalyzerImpl(locationAnalyzer, routeRepository)
    val routeLocationAnalyzer: RouteLocationAnalyzer = new RouteLocationAnalyzerMock()
    val routeAnalyzer = new RouteDetailMainAnalyzerImpl(
      routeCountryAnalyzer,
      routeLocationAnalyzer,
      routeTileAnalyzer
    )
    val analysis = routeAnalyzer.analyze(relation, None).get

    val report = new RouteAnalysisReport(analysis.oldRouteDetailAnalysis).report
    if (report.nonEmpty) {
      Assertions.fail("Route analysis failed!\n" + report)
    }
  }

  private class RouteAnalysisReport(analysis: RouteDetailAnalysis) {

    def report: String = {
      val ra = analysis.routeDetail.analysis

      Seq(
        evaluateMissingFacts,
        evaluateUnexpectedFacts,
        evaluate("Start node", startNodeIdBuffer.toSeq, ra.map.startNodes.map(_.id)),
        evaluate("End node", endNodeIdBuffer.toSeq, analysis.routeDetail.analysis.map.endNodes.map(_.id)),
        evaluate("Start tentacle node", startTentacleNodeIdBuffer.toSeq, analysis.routeDetail.analysis.map.startTentacleNodes.map(_.id)),
        evaluate("End tentacle node", endTentacleNodeIdBuffer.toSeq, analysis.routeDetail.analysis.map.endTentacleNodes.map(_.id)),
        evaluate("Unexpected node", unexpectedNodeIdBuffer.toSeq, analysis.routeDetail.analysis.unexpectedNodeIds),
        evaluate("Forward nodes", forwardNodeIdBuffer.toSeq, analysis.structure.forwardNodeIds),
        evaluate("Backward nodes", backwardNodeIdBuffer.toSeq, analysis.structure.backwardNodeIds),
        evaluateTentacles,
        evaluateLong("Structure", structureBuffer.toSeq, analysis.routeDetail.analysis.structureStrings)
        // TODO add tests for breakpoints (forward and backward) ?

      ).flatten.map(s => "  - " + s).mkString("\n")
    }

    private def evaluateMissingFacts: Option[String] = {
      val missingFacts = factsBuffer.toSet -- analysis.routeDetail.oldFacts // TODO redesign - switch from oldFacts to facts
      if (missingFacts.nonEmpty) {
        Some("Missing fact(s): " + missingFacts)
      }
      else {
        None
      }
    }

    private def evaluateUnexpectedFacts: Option[String] = {
      val unexpectedFacts = analysis.routeDetail.oldFacts.toSet -- factsBuffer.toSet // TODO redesign - switch from oldFacts to facts
      if (unexpectedFacts.nonEmpty) {
        Some("Unexpected fact(s): " + unexpectedFacts.mkString(", "))
      }
      else {
        None
      }
    }

    private def evaluate(title: String, expected: Seq[Long], actual: Seq[Long]): Option[String] = {
      if (expected != actual) {
        Some(title + " mismatch, expected: " + expected.mkString("+") + ", but found: " + actual.mkString("+"))
      }
      else {
        None
      }
    }

    private def evaluateLong(title: String, expected: Seq[String], actual: Seq[String]): Option[String] = {
      if (expected != actual) {
        Some(title + " mismatch,\n    expected: " + expected.mkString("+") + "\n       found: " + actual.mkString("+"))
      }
      else {
        None
      }
    }

    private def evaluate2(expected: Seq[Seq[String]], actual: Seq[Seq[String]]): Option[String] = {
      if (expected != actual) {
        Some("Start node mismatch, expected: " + expected.mkString("+") + ", but found: " + actual.mkString("+"))
      }
      else {
        None
      }
    }

    private def evaluateTentacles: Option[String] = {
      val startTentacleNodeIds = analysis.structure.startTentaclePaths.flatMap(_.segments).map(_.nodes.map(_.id))
      val endTentacleNodeIds = analysis.structure.endTentaclePaths.flatMap(_.segments).map(_.nodes.map(_.id))
      val tentacleNodeIds = startTentacleNodeIds ++ endTentacleNodeIds
      if (tentacleBuffer != tentacleNodeIds) {
        Some("Tentacle mismatch, found: " + tentacleNodeIds.mkString("+") + ", but expected: " + tentacleBuffer.mkString("+") + ".\n")
      }
      else {
        None
      }
    }
  }
}
