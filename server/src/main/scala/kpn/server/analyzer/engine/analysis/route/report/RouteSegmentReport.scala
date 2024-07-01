package kpn.server.analyzer.engine.analysis.route.report

import kpn.core.analysis.LinkDirection
import kpn.server.analyzer.engine.analysis.route.RouteNodeData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.NewRouteSegment
import kpn.server.analyzer.engine.analysis.route.structure.NewRouteSegmentElement
import kpn.server.analyzer.engine.analysis.route.structure.RouteLink
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinkNode
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinkRelationId
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinkWay

object RouteSegmentReport {
  def report(context: RouteAnalysisContext): String = {
    new RouteSegmentReport(context).report()
  }
}

class RouteSegmentReport(context: RouteAnalysisContext) {
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
         |${paths(segment)}
         |""".stripMargin
    }.mkString
  }

  private def paths(segment: NewRouteSegment): String = {
    segment.elements.map { element =>
      s"""<tr>
         |  <td colspan="5">
         |    Segment element ${element.id} ${element.direction.toString.toLowerCase}
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
         |${elementLinks(element)}
         |""".stripMargin
    }.mkString
  }

  private def elementLinks(element: NewRouteSegmentElement): String = {
    element.links.map(elementLink).mkString
  }

  private def elementLink(link: RouteLink): String = {

    val (elementType, elementId, name, from, to, networkNodes) = link match {
      case routeLinkNode: RouteLinkNode =>
        ("node", routeLinkNode.node.id, "TODO", "", "", "")
      case routeLinkWay: RouteLinkWay =>
        val head = ReportUtil.osmNodeLink(routeLinkWay.way.nodes.head.id)
        val last = ReportUtil.osmNodeLink(routeLinkWay.way.nodes.last.id)
        val (from, to) = if (routeLinkWay.link.direction == LinkDirection.Backward) {
          (last, head)
        }
        else {
          (head, last)
        }
        ("node", routeLinkWay.way.id, "TODO", from, to, networkNodeString(routeLinkWay.way.nodeIds))
      case routeLinkRelationId: RouteLinkRelationId =>
        ("node", routeLinkRelationId.relationId, "TODO", "", "", "")
    }

    s"""<tr>
       |  <td>
       |    ${link.idString}
       |  </td>
       |  <td style="padding:0">
       |    <img src="images/${link.linkName}.png"/>
       |  </td>
       |  <td>
       |    <pre>${link.linkDetail}</pre>
       |  </td>
       |  <td>
       |    ${ReportUtil.osmLink(elementType, elementId)}
       |  </td>
       |  <td>
       |    ${link.role.getOrElse("")}
       |  </td>
       |  <td>
       |    $from
       |  </td>
       |  <td>
       |    $to
       |  </td>
       |  <td>$networkNodes</td>
       |</tr>
       |""".stripMargin
  }

  private def networkNodeString(nodeIds: Seq[Long]): String = {
    Seq(
      networkNodeStrings(nodeIds, "start", context.routeNodeAnalysis.startNodes),
      networkNodeStrings(nodeIds, "end", context.routeNodeAnalysis.endNodes),
      networkNodeStrings(nodeIds, "free", context.routeNodeAnalysis.freeNodes),
      networkNodeStrings(nodeIds, "redundant", context.routeNodeAnalysis.redundantNodes)
    ).flatten.mkString(", ")
  }

  private def networkNodeStrings(nodeIds: Seq[Long], nodeType: String, routeNodeDatas: Seq[RouteNodeData]): Seq[String] = {
    val routeNodeDatasInFragment = routeNodeDatas.filter(n => nodeIds.contains(n.node.id))
    routeNodeDatasInFragment.map(routeNodeData => s"$nodeType=${routeNodeData.name}(${routeNodeData.node.id})")
  }
}
