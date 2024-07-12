package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RoutePathReport {
  def report(context: RouteDetailAnalysisContext): String = {
    new RoutePathReport(context).report()
  }
}

class RoutePathReport(context: RouteDetailAnalysisContext) {
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
      val from = fromNode.map(n => s"${ReportUtil.osmNodeLink(n.node.id)}(${n.name})").getOrElse("")
      val to = toNode.map(n => s"${ReportUtil.osmNodeLink(n.node.id)}(${n.name})").getOrElse("")
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
