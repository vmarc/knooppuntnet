package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisElement
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisFragment
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisSegment
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RoutePathDirection
import kpn.server.analyzer.engine.analysis.route.domain.StructurePath

case class RouteDetailAnalysisTestContext(context: RouteDetailAnalysisContext) {
  def links: Seq[String] = {
    context.links.links.zipWithIndex.map { case (link, index) =>
      s"${index + 1}    ${link.linkDetail}"
    }
  }

  def nodes: Seq[String] = {
    Seq(
      networkNodeStrings("start", context.routeNodesAnalysis.startNode.toSeq),
      networkNodeStrings("end", context.routeNodesAnalysis.endNode.toSeq),
      networkNodeStrings("start-tentacle", context.routeNodesAnalysis.startTentacleNodes),
      networkNodeStrings("end-tentacle", context.routeNodesAnalysis.endTentacleNodes),
      networkNodeStrings("redundant", context.routeNodesAnalysis.redundantNodes)
    ).flatten
  }

  def facts: Set[Fact] = context.facts.toSet

  def paths: Seq[String] = {
    Seq(
      context.structure.forwardPath.map(path => "forward=" + pathToString(path)).toSeq,
      context.structure.backwardPath.map(path => "backward=" + pathToString(path)).toSeq,
      context.structure.startTentaclePaths.map(path => "start-tentacle=" + pathToString(path)).toSeq,
      context.structure.endTentaclePaths.map(path => "end-tentacle=" + pathToString(path)).toSeq,
      context.structure.otherPaths.map(path => "other=" + pathToString(path)),
    ).flatten
  }

  def segments: Seq[String] = {
    context.segments.flatMap { segment =>
      val elements = segment.elements.flatMap { element =>
        elementToString(element) +: element.fragments.map(fragmentToString)
      }
      segmentToString(segment) +: elements
    }
  }

  private def segmentToString(segment: RouteAnalysisSegment): String = {
    s"""segment-${segment.id} ${segment.fromNodeId}>${segment.toNodeId}"""
  }

  private def elementToString(element: RouteAnalysisElement): String = {
    val direction = if (element.direction == RoutePathDirection.Bidirectional) {
      "↔"
    } else if (element.direction == RoutePathDirection.Forward) {
      "→"
    }
    else {
      "←"
    }
    val from = element.fromNetworkNode.map(n => s"  ${n.node.id}(${n.name})").getOrElse("")
    val to = element.toNetworkNode.map(n => s"  ${n.node.id}(${n.name})").getOrElse("")
    val nodes = element.nodeIds.mkString(", ")
    s"""  element-${element.id} ${element.fromNodeId}>${element.toNodeId}$from$to  $direction  nodes=$nodes"""
  }

  private def fragmentToString(link: RouteAnalysisFragment): String = {
    s"""    way-${link.way.id}  ${link.link.reportString}"""
  }

  private def pathToString(path: StructurePath): String = {
    val nodeString = path.nodeIds.mkString(", ")
    s"${path.startNodeId}>${path.endNodeId} nodes=$nodeString"
  }

  private def networkNodeStrings(nodeType: String, nodeDatas: Seq[RouteNodeAnalysis]): Seq[String] = {
    nodeDatas.map(routeNodeData => s"$nodeType=${routeNodeData.node.id}(${routeNodeData.name})")
  }
}
