package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RoutePathReport {
  def report(context: RouteAnalysisContext): String = {
    new RoutePathReport(context).report()
  }
}

class RoutePathReport(context: RouteAnalysisContext) {
  def report(): String = {
    s"""<table>
       |  <tr class="header">
       |    <td>id</td>
       |    <td>direction</td>
       |    <td>from</td>
       |    <td>to</td>
       |    <td>segment elements</td>
       |  </tr>
       |${paths()}
       |</table>
       |""".stripMargin
  }

  private def paths(): String = {
    context.paths.map { path =>
      val fromNode = path.elements.head.fromNetworkNode
      val toNode = path.elements.last.toNetworkNode
      val from = fromNode.map(n => s"${n.name}(${ReportUtil.osmNodeLink(n.node.id)})").getOrElse("")
      val to = toNode.map(n => s"${n.name}(${ReportUtil.osmNodeLink(n.node.id)})").getOrElse("")
      val elementIds = path.elements.map(element => element.id).mkString(", ")

      s"""<tr>
         |  <td>
         |    Path ${path.id}
         |  </td>
         |  <td>
         |    ${path.direction.toString.toLowerCase}
         |  </td>
         |  <td>
         |    $from
         |  </td>
         |  <td>
         |    $to
         |  </td>
         |  <td>
         |    $elementIds
         |  </td>
         |</tr>
         |""".stripMargin
    }.mkString
  }
}
