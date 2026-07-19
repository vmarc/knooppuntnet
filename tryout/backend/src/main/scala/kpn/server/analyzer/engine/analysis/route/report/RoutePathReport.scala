package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext

object RoutePathReport {
  def report(context: BaseRouteAnalysisContext): String = {
    new RoutePathReport(context).report()
  }
}

class RoutePathReport(context: BaseRouteAnalysisContext) {
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
    context.analysisSegments.flatMap(_.elements).map { element =>
      val fromNode = element.fromNetworkNode
      val toNode = element.toNetworkNode
      val from = fromNode.map(n => s"${ReportUtil.osmNodeLink(n.node.id)}(${n.name})").getOrElse("")
      val to = toNode.map(n => s"${ReportUtil.osmNodeLink(n.node.id)}(${n.name})").getOrElse("")
      val elementIds = s"${element.id}"

      s"""<tr>
         |  <td>
         |    Path ${element.id}
         |  </td>
         |  <td>
         |    ${element.direction.toString.toLowerCase}
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
