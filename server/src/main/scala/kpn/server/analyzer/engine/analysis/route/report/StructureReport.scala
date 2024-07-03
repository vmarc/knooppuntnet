package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.structure.OldStructure
import kpn.server.analyzer.engine.analysis.route.structure.OldStructurePath
import kpn.server.analyzer.engine.analysis.route.structure.OldStructurePathElement
import kpn.server.analyzer.engine.analysis.route.structure.StructureFragment

object StructureReport {

  def report(structure: OldStructure): String = {
    s"""<table>
       |  <tr class="header">
       |    <td class="spacer"></td>
       |    <td class="spacer"></td>
       |    <td class="spacer"></td>
       |    <td>way</td>
       |    <td>direction</td>
       |    <td>nodes</td>
       |  </tr>
       |  ${structure.forwardPath.map(path => structurePath("forwardPath", path)).getOrElse("")}
       |  ${structure.backwardPath.map(path => structurePath("backwardPath", path)).getOrElse("")}
       |  ${structure.otherPaths.map(path => structurePath("otherPath", path)).mkString}
       |</table>
       |""".stripMargin
  }

  private def structurePath(name: String, path: OldStructurePath): String = {
    s"""<tr>
       |  <td colspan="6">StructurePath $name startNodeId=${path.startNodeId}, endNodeId=${path.endNodeId}</td>
       |</tr>
       |${path.elements.map(structurePathElement).mkString}
       |""".stripMargin
  }

  private def structurePathElement(element: OldStructurePathElement): String = {
    s"""<tr>
       |  <td></td>
       |  <td colspan="4">StructurePathElement reversed=${element.reversed}</td>
       |  <td></td>
       |</tr>
       |${element.element.fragments.map(structureFragment).mkString}
       |""".stripMargin
  }

  private def structureFragment(fragment: StructureFragment): String = {
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
