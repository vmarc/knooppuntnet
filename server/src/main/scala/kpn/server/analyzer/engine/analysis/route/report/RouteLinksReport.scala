package kpn.server.analyzer.engine.analysis.route.report

import kpn.core.analysis.LinkDirection
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisNode
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteLink
import kpn.server.analyzer.engine.analysis.route.domain.RouteLinkNode
import kpn.server.analyzer.engine.analysis.route.domain.RouteLinkRelationId
import kpn.server.analyzer.engine.analysis.route.domain.RouteLinkWay

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
        ("way", routeLinkWay.way.id, "TODO", from, to, allNodes(routeLinkWay.way.nodeIds))
      case routeLinkRelationId: RouteLinkRelationId =>
        ("relation", routeLinkRelationId.relationId, "TODO", "", "", "")
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

  private def allNodes(nodeIds: Seq[Long]): String = {
    Seq(
      nodes(nodeIds, "start", context.nodes.startNode.toSeq),
      nodes(nodeIds, "end", context.nodes.endNode.toSeq),
      nodes(nodeIds, "start tentacle", context.nodes.startTentacleNodes),
      nodes(nodeIds, "end tentacle", context.nodes.endTentacleNodes),
      nodes(nodeIds, "redundant", context.nodes.redundantNodes)
    ).flatten.mkString(", ")
  }

  private def nodes(nodeIds: Seq[Long], nodeType: String, nodeDatas: Seq[RouteAnalysisNode]): Seq[String] = {
    val filteredNodeDatas = nodeDatas.filter(n => nodeIds.contains(n.node.id))
    filteredNodeDatas.map(nodeData => s"$nodeType=${nodeData.node.id}(${nodeData.name})")
  }
}
