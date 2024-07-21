package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouteSummaryReport {

  def report(context: RouteDetailAnalysisContext): String = {
    val name = context.routeNameAnalysis.name
    val nodeNetwork = yes(context.nodeNetwork)
    val superRoute = yes(context.superRoute)
    val proposed = yes(context.proposed)
    val networkType = context.networkTypes.map(_.name).mkString(", ")
    val scopedNetworkType = context.scopedNetworkTypeOption.map(_.key).getOrElse("")
    val countries = context.countries.map(_.domain).mkString(", ")
    val unexpectedNodeIds = context.unexpectedNodeIds.mkString(", ")
    val unexpectedRelationIds = context.unexpectedRelationIds.mkString(", ")

    s"""
       |<table>
       |  <tr><td>name</td><td>$name</td></tr>
       |  <tr><td>nodeNetwork</td><td>$nodeNetwork</td></tr>
       |  <tr><td>superRoute</td><td>$superRoute</td></tr>
       |  <tr><td>proposed</td><td>$proposed</td></tr>
       |  <tr><td>networkType</td><td>$networkType</td></tr>
       |  <tr><td>scopedNetworkType</td><td>$scopedNetworkType</td></tr>
       |  <tr><td>countries</td><td>$countries</td></tr>
       |  <tr><td>unexpectedNodeIds</td><td>$unexpectedNodeIds</td></tr>
       |  <tr><td>unexpectedRelationIds</td><td>$unexpectedRelationIds</td></tr>
       |</table>
       |""".stripMargin
  }

  private def yes(value: Boolean): String = {
    if (value) "yes" else ""
  }
}
