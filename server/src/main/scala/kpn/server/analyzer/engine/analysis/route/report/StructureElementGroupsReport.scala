package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.RouteNodeData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.StructureElement
import kpn.server.analyzer.engine.analysis.route.structure.StructureElementGroup
import kpn.server.analyzer.engine.analysis.route.structure.StructureFragment

class StructureElementGroupsReport(context: RouteDetailAnalysisContext) {

  def report: String = {

    val groups = context.segmentAnalysis.elementGroups

    s"""<table>
       |  <tr class="header">
       |    <td class="spacer"></td>
       |    <td class="spacer"></td>
       |    <td class="spacer"></td>
       |    <td>way</td>
       |    <td>direction</td>
       |    <td>from</td>
       |    <td>to</td>
       |    <td>nodes</td>
       |    <td>network nodes</td>
       |  </tr>
       |  ${groups.map(elementGroupRows).mkString}
       |</table>
       |""".stripMargin
  }

  private def elementGroupRows(group: StructureElementGroup): String = {
    s"""<tr>
       |  <td colspan="7">StructureElementGroup</td>
       |</tr>
       |${group.elements.map(elementRows).mkString}
       |""".stripMargin
  }

  private def elementRows(element: StructureElement): String = {
    val from = element.fragments.head.nodeIds.head
    val to = element.fragments.last.nodeIds.last
    s"""<tr>
       |  <td></td>
       |  <td colspan="3">StructureElement ${element.id}</td>
       |  <td>${element.direction.map(_.toString).getOrElse("")}</td>
       |  <td>$from</td>
       |  <td>$to</td>
       |  <td></td>
       |  <td></td>
       |</tr>
       |${element.fragments.map(fragmentRow).mkString}
       |""".stripMargin
  }

  private def fragmentRow(fragment: StructureFragment): String = {
    val from = fragment.nodeIds.head
    val to = fragment.nodeIds.last
    val nodes = {
      val nodeString = if (fragment.nodeIds == fragment.way.nodeIds) {
        "all"
      }
      else if (fragment.nodeIds == fragment.way.nodeIds.reverse) {
        "all reversed"
      }
      else {
        fragment.nodeIds.mkString(",")
      }
      s"(${fragment.nodeIds.size}) $nodeString"
    }

    s"""<tr>
       |  <td colspan="2"></td>
       |  <td>StructureFragment</td>
       |  <td>${fragment.way.id}</td>
       |  <td>${if (fragment.bidirectional) "" else "unidirectional"}</td>
       |  <td>$from</td>
       |  <td>$to</td>
       |  <td>$nodes</td>
       |  <td>${networkNodes(fragment)}</td>
       |</tr>
       |""".stripMargin
  }

  private def networkNodes(fragment: StructureFragment): String = {
    Seq(
      fragmentNodes(fragment, "start", context.routeNodeAnalysis.startNode.toSeq),
      fragmentNodes(fragment, "end", context.routeNodeAnalysis.endNode.toSeq),
      fragmentNodes(fragment, "start tentacle", context.routeNodeAnalysis.startTentacleFromNodes),
      fragmentNodes(fragment, "end tentacle", context.routeNodeAnalysis.endTentacleToNodes),
      fragmentNodes(fragment, "free", context.routeNodeAnalysis.freeNodes),
      fragmentNodes(fragment, "redundant", context.routeNodeAnalysis.redundantNodes)
    ).flatten.mkString(", ")
  }

  private def fragmentNodes(fragment: StructureFragment, nodeType: String, routeNodeDatas: Seq[RouteNodeData]): Seq[String] = {
    val routeNodeDatasInFragment = routeNodeDatas.filter(n => fragment.nodeIds.contains(n.node.id))
    routeNodeDatasInFragment.map(routeNodeData => s"$nodeType=${routeNodeData.name}(${routeNodeData.node.id})")
  }
}
