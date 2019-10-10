package kpn.core.engine.analysis.route

import kpn.core.data.DataBuilder
import kpn.core.engine.analysis.NetworkNodeBuilder
import kpn.core.engine.analysis.RouteTestData
import kpn.core.engine.analysis.country.CountryAnalyzer
import kpn.core.engine.analysis.country.CountryAnalyzerNoop
import kpn.core.engine.analysis.route.analyzers.AccessibilityAnalyzerImpl
import kpn.core.load.data.LoadedRoute
import kpn.core.tools.analyzer.AnalysisContext
import kpn.shared.Fact
import kpn.shared.SharedTestObjects
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawData
import kpn.shared.data.raw.RawRelation
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
    val tags = Tags.from(
      "type" -> "route",
      "network" -> d.networkType.name,
      "route" -> d.networkType.routeTagValues.head,
      "note" -> d.routeName
    ) ++ d.tags
    val rr: RawRelation = newRawRelation(10, members = d.members, tags = tags)
    val rawData = RawData(None, d.nodes, d.ways, Seq(rr))
    val data = new DataBuilder(rawData).data
    val relation = data.relations(rr.id)

    val countryAnalyzer: CountryAnalyzer = new CountryAnalyzerNoop()

    val analysisContext = new AnalysisContext()
    val networkNodes = new NetworkNodeBuilder(analysisContext, data, d.networkType, countryAnalyzer).networkNodes
    val routeAnalyzer = new MasterRouteAnalyzerImpl(analysisContext, new AccessibilityAnalyzerImpl())
    val analysis = routeAnalyzer.analyze(networkNodes, LoadedRoute(None, d.networkType, "", data, relation), orphan = false)

    val report = new RouteAnalysisReport(analysis).report
    if (report.nonEmpty) {
      Assertions.fail("Route analysis failed!\n" + report)
    }
  }

  private class RouteAnalysisReport(analysis: RouteAnalysis) {

    def report: String = {
      val ra = analysis.route.analysis.get

      Seq(
        evaluateMissingFacts,
        evaluateUnexpectedFacts,
        evaluate("Start node", startNodeIdBuffer, ra.startNodes.map(_.id)),
        evaluate("End node", endNodeIdBuffer, analysis.route.analysis.get.endNodes.map(_.id)),
        evaluate("Start tentacle node", startTentacleNodeIdBuffer, analysis.route.analysis.get.startTentacleNodes.map(_.id)),
        evaluate("End tentacle node", endTentacleNodeIdBuffer, analysis.route.analysis.get.endTentacleNodes.map(_.id)),
        evaluate("Unexpected node", unexpectedNodeIdBuffer, analysis.route.analysis.get.unexpectedNodeIds),
        evaluate("Forward nodes", forwardNodeIdBuffer, analysis.structure.forwardNodeIds),
        evaluate("Backward nodes", backwardNodeIdBuffer, analysis.structure.backwardNodeIds),
        evaluateTentacles,
        evaluateLong("Structure", structureBuffer, analysis.route.analysis.get.structureStrings)
        // TODO add tests for breakpoints (forward and backward) ?

      ).flatten.map(s => "  - " + s).mkString("\n")
    }

    private def evaluateMissingFacts: Option[String] = {
      val missingFacts = factsBuffer.toSet -- analysis.route.facts
      if (missingFacts.nonEmpty) {
        Some("Missing fact(s): " + missingFacts)
      }
      else {
        None
      }
    }

    private def evaluateUnexpectedFacts: Option[String] = {
      val unexpectedFacts = analysis.route.facts.toSet -- factsBuffer.toSet
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
