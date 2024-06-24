package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.structure.StructureElement
import kpn.server.analyzer.engine.analysis.route.structure.StructureElementGroup
import kpn.server.analyzer.engine.analysis.route.structure.StructureFragment

object StructureElementGroupsReport {

  def report(groups: Seq[StructureElementGroup]): String = {
    s"""<table>
       |  <tr class="header">
       |    <td class="spacer"></td>
       |    <td class="spacer"></td>
       |    <td class="spacer"></td>
       |    <td>way</td>
       |    <td>direction</td>
       |    <td>nodes</td>
       |  </tr>
       |  ${groups.map(elementGroupRows).mkString}
       |</table>
       |""".stripMargin
  }

  private def elementGroupRows(group: StructureElementGroup): String = {
    s"""<tr>
       |  <td colspan="6">StructureElementGroup</td>
       |</tr>
       |${group.elements.map(elementRows).mkString}
       |""".stripMargin
  }

  private def elementRows(element: StructureElement): String = {
    s"""<tr>
       |  <td></td>
       |  <td colspan="3">StructureElement ${element.id}</td>
       |  <td>${element.direction.map(_.toString).getOrElse("")}</td>
       |  <td></td>
       |</tr>
       |${element.fragments.map(fragmentRow).mkString}
       |""".stripMargin
  }

  private def fragmentRow(fragment: StructureFragment): String = {
    val nodeString = if (fragment.nodeIds == fragment.way.nodeIds) {
      "all"
    }
    else if (fragment.nodeIds == fragment.way.nodeIds.reverse) {
      "all reversed"
    }
    else {
      fragment.nodeIds.mkString(",")
    }
    val nodes = s"(${fragment.nodeIds.size}) $nodeString"
    s"""<tr>
       |  <td colspan="2"></td>
       |  <td>StructureFragment</td>
       |  <td>${fragment.way.id}</td>
       |  <td>${if (fragment.bidirectional) "" else "unidirectional"}</td>
       |  <td>$nodes</td>
       |</tr>
       |""".stripMargin
  }
}
