package kpn.server.analyzer.engine.analysis.route.report

import kpn.core.analysis.LinkDirection
import kpn.server.analyzer.engine.analysis.route.RouteNodeData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.RouteLink
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinkNode
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinkRelationId
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinkWay

object RouteLinksReport {
  def report(context: RouteDetailAnalysisContext): String = {
    new RouteLinksReport(context).report()
  }
}

class RouteLinksReport(context: RouteDetailAnalysisContext) {
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
       |${routeMemberRows()}
       |</table>
       |""".stripMargin
  }

  private def routeMemberRows(): String = {
    context.links.links.zipWithIndex.map { case (routeLink, index) =>
      routeMemberRow(routeLink, index)
    }.mkString
  }

  private def routeMemberRow(routeLink: RouteLink, index: Int): String = {

    val (elementType, elementId, name, from, to, networkNodes) = routeLink match {
      case routeLinkNode: RouteLinkNode =>
        ("node", routeLinkNode.node.id, "TODO", "", "", "")
      case routeLinkWay: RouteLinkWay =>
        val head = routeLinkWay.way.nodes.head.id.toString
        val last = routeLinkWay.way.nodes.last.id.toString
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
       |    ${index + 1}
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
       |    <a href="https://www.openstreetmap.org/node/$from">$from</a>
       |  </td>
       |  <td>
       |    <a href="https://www.openstreetmap.org/node/$to">$to</a>
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
