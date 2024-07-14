package kpn.server.analyzer.engine.analysis.route.report

import kpn.core.analysis.LinkDirection
import kpn.server.analyzer.engine.analysis.route.RouteNodeData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.NewRouteSegment
import kpn.server.analyzer.engine.analysis.route.structure.NewRouteSegmentElement
import kpn.server.analyzer.engine.analysis.route.structure.NewRouteSegmentElementFragment

object RouteSegmentReport {
  def report(context: RouteDetailAnalysisContext): String = {
    new RouteSegmentReport(context).report()
  }
}

class RouteSegmentReport(context: RouteDetailAnalysisContext) {
  def report(): String = {
    s"""<table>
       |  <tr class="header">
       |    <td>nr</td>
       |    <td></td>
       |    <td>link</td>
       |    <td>id</td>
       |    <td>role</td>
       |    <td>from</td>
       |    <td>to</td>
       |    <td>network nodes</td>
       |  </tr>
       |${segments()}
       |</table>
       |""".stripMargin
  }

  private def segments(): String = {
    context.segments.map { segment =>
      s"""<tr>
         |  <td colspan="5">
         |    Segment ${segment.id}
         |  </td>
         |  <td>
         |    ${ReportUtil.osmNodeLink(segment.fromNodeId)}
         |  </td>
         |  <td>
         |    ${ReportUtil.osmNodeLink(segment.toNodeId)}
         |  </td>
         |  <td>
         |  </td>
         |</tr>
         |${segmentElements(segment)}
         |""".stripMargin
    }.mkString
  }

  private def segmentElements(segment: NewRouteSegment): String = {
    segment.elements.map { element =>
      val from = element.fromNetworkNode.map(node => s"from=${ReportUtil.osmNodeLink(node.node.id)}(${node.name})").getOrElse("")
      val to = element.toNetworkNode.map(node => s"to=${ReportUtil.osmNodeLink(node.node.id)}(${node.name})").getOrElse("")
      s"""<tr>
         |  <td colspan="5">
         |    Segment element ${element.id} ${element.direction.toString.toLowerCase} $from $to
         |  </td>
         |  <td>
         |    ${ReportUtil.osmNodeLink(element.fromNodeId)}
         |  </td>
         |  <td>
         |    ${ReportUtil.osmNodeLink(element.toNodeId)}
         |  </td>
         |  <td>
         |  </td>
         |</tr>
         |${elementFragments(element)}
         |""".stripMargin
    }.mkString
  }

  private def elementFragments(element: NewRouteSegmentElement): String = {
    element.fragments.map(elementFragment).mkString
  }

  private def elementFragment(fragment: NewRouteSegmentElementFragment): String = {

    val head = ReportUtil.osmNodeLink(fragment.nodeIds.head)
    val last = ReportUtil.osmNodeLink(fragment.nodeIds.last)
    val (from, to) = if (fragment.link.direction == LinkDirection.Backward) {
      (last, head)
    }
    else {
      (head, last)
    }
    s"""<tr>
       |  <td>
       |    ${fragment.id}
       |  </td>
       |  <td style="padding:0">
       |    <img src="images/${fragment.link.name}.png"/>
       |  </td>
       |  <td>
       |    <pre>${fragment.link.reportString}</pre>
       |  </td>
       |  <td>
       |    ${ReportUtil.osmLink("way", fragment.wayId)}
       |  </td>
       |  <td>
       |    ${fragment.role.getOrElse("")}
       |  </td>
       |  <td>
       |    $from
       |  </td>
       |  <td>
       |    $to
       |  </td>
       |  <td>${allNodes(fragment.nodeIds)}</td>
       |</tr>
       |""".stripMargin
  }

  private def allNodes(nodeIds: Seq[Long]): String = {
    Seq(
      nodes(nodeIds, "start", context.nodeAnalysis.startNode.toSeq),
      nodes(nodeIds, "end", context.nodeAnalysis.endNode.toSeq),
      nodes(nodeIds, "start tentacle", context.nodeAnalysis.startTentacleNodes),
      nodes(nodeIds, "end tentacle", context.nodeAnalysis.endTentacleNodes),
      nodes(nodeIds, "redundant", context.nodeAnalysis.redundantNodes)
    ).flatten.mkString(", ")
  }

  private def nodes(nodeIds: Seq[Long], nodeType: String, nodeDatas: Seq[RouteNodeData]): Seq[String] = {
    val filteredNodeDatas = nodeDatas.filter(n => nodeIds.contains(n.node.id))
    filteredNodeDatas.map(nodeData => s"$nodeType=${nodeData.node.id}(${nodeData.name})")
  }
}
