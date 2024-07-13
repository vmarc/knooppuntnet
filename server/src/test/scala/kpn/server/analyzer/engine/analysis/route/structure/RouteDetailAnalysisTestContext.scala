package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.route.RouteNodeData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

case class RouteDetailAnalysisTestContext(context: RouteDetailAnalysisContext) {
  def links: Seq[String] = {
    context.links.links.zipWithIndex.map { case (link, index) =>
      s"${index + 1}    ${link.linkDetail}"
    }
  }

  def nodes: Seq[String] = {
    Seq(
      networkNodeStrings("start", context.routeNodeAnalysis.startNode.toSeq),
      networkNodeStrings("end", context.routeNodeAnalysis.endNode.toSeq),
      networkNodeStrings("start-tentacle", context.routeNodeAnalysis.startTentacleFromNodes),
      networkNodeStrings("end-tentacle", context.routeNodeAnalysis.endTentacleToNodes),
      networkNodeStrings("free", context.routeNodeAnalysis.freeNodes),
      networkNodeStrings("redundant", context.routeNodeAnalysis.redundantNodes)
    ).flatten
  }

  def paths: Seq[String] = {
    context.paths.map { path =>
      val id = s"path-${path.id}"
      val direction = if (path.direction == RoutePathDirection.Bidirectional) {
        "↔"
      } else if (path.direction == RoutePathDirection.Forward) {
        "→"
      }
      else {
        "←"
      }
      val elements = path.elements.map(_.id).mkString(", ")
      val nodes = path.nodeIds.mkString(", ")
      s"$id $direction elements=$elements, nodes=$nodes"
    }
  }

  def facts: Set[Fact] = context.facts.toSet

  def pathDetails: Seq[String] = {
    Seq(
      context.newStructure.forwardPath.map(path => "forward=" + pathToString(path)).toSeq,
      context.newStructure.backwardPath.map(path => "backward=" + pathToString(path)).toSeq,
      context.newStructure.startTentaclePaths.map(path => "start-tentacle=" + pathToString(path)).toSeq,
      context.newStructure.endTentaclePaths.map(path => "end-tentacle=" + pathToString(path)).toSeq,
      context.newStructure.otherPaths.map(path => "other=" + pathToString(path)),
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

  private def segmentToString(segment: NewRouteSegment): String = {
    s"""segment-${segment.id} ${segment.fromNodeId}>${segment.toNodeId}"""
  }

  private def elementToString(element: NewRouteSegmentElement): String = {
    val direction = element.direction.entryName.toLowerCase
    val from = element.fromNetworkNode.map(n => s"  ${n.node.id}(${n.name})").getOrElse("")
    val to = element.toNetworkNode.map(n => s"  ${n.node.id}(${n.name})").getOrElse("")
    s"""  element-${element.id} $direction ${element.fromNodeId}>${element.toNodeId}$from$to"""
  }

  private def fragmentToString(link: NewRouteSegmentElementFragment): String = {
    s"""    way-${link.wayId}  ${link.link.reportString}  paths=${link.pathIds.mkString(", ")}"""
  }

  private def pathToString(path: StructurePath): String = {
    val nodeString = path.nodeIds.mkString(", ")
    s"${path.startNodeId}>${path.endNodeId} nodes=$nodeString"
  }

  private def networkNodeStrings(nodeType: String, routeNodeDatas: Seq[RouteNodeData]): Seq[String] = {
    routeNodeDatas.map(routeNodeData => s"$nodeType=${routeNodeData.node.id}(${routeNodeData.name})")
  }
}
