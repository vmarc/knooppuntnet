package kpn.server.analyzer.engine.analysis.route.report

import kpn.core.analysis.LinkDirection
import kpn.server.analyzer.engine.analysis.route.RouteNodeData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.NewSegment
import kpn.server.analyzer.engine.analysis.route.structure.RouteLink
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinkNode
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinkRelationId
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinkWay
import kpn.server.analyzer.engine.analysis.route.structure.RoutePath

object RoutePathsReport {
  def report(context: RouteAnalysisContext): String = {
    new RoutePathsReport(context).report()
  }
}

class RoutePathsReport(context: RouteAnalysisContext) {
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
    context.path.segments.map { segment =>
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

  private def paths(segment: NewSegment): String = {
    segment.paths.map { path =>
      s"""<tr>
         |  <td colspan="5">
         |    Path ${path.id} ${path.direction.toString.toLowerCase}
         |  </td>
         |  <td>
         |    ${ReportUtil.osmNodeLink(path.fromNodeId)}
         |  </td>
         |  <td>
         |    ${ReportUtil.osmNodeLink(path.toNodeId)}
         |  </td>
         |  <td>
         |  </td>
         |</tr>
         |${pathLinks(path)}
         |""".stripMargin
    }.mkString
  }

  private def pathLinks(path: RoutePath): String = {
    path.links.map { routeLink =>
      routeMemberRow(routeLink)
    }.mkString
  }

  private def routeMemberRow(routeLink: RouteLink): String = {

    val (elementType, elementId, name, from, to, networkNodes) = routeLink match {
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
       |    ${routeLink.idString}
       |  </td>
       |  <td style="padding:0">
       |    <img src="images/${routeLink.linkName}.png"/>
       |  </td>
       |  <td>
       |    <pre>${routeLink.linkDetail}</pre>
       |  </td>
       |  <td>
       |    <a href="https://www.openstreetmap.org/$elementType/$elementId">$elementId</a>
       |  </td>
       |  <td>
       |    ${routeLink.role.getOrElse("")}
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
