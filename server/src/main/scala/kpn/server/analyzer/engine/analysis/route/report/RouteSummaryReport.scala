package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext

object RouteSummaryReport {

  def report(context: BaseRouteAnalysisContext): String = {
    val name = context.routeNameAnalysis.name
    val nodeNetwork = yes(context.nodeNetwork)
    val superRoute = yes(context.superRoute)
    val proposed = yes(context.proposed)
    val routeType = context.routeTypes.map(_.toString).mkString(", ")
    val scopedRouteType = context.scopedRouteTypeOption.map(_.key).getOrElse("")
    val countries = context.countries.map(_.toString).mkString(", ")
    val unexpectedNodeIds = context.unexpectedNodeIds.mkString(", ")

    s"""
       |<table>
       |  <tr><td>name</td><td>$name</td></tr>
       |  <tr><td>nodeNetwork</td><td>$nodeNetwork</td></tr>
       |  <tr><td>superRoute</td><td>$superRoute</td></tr>
       |  <tr><td>proposed</td><td>$proposed</td></tr>
       |  <tr><td>routeType</td><td>$routeType</td></tr>
       |  <tr><td>scopedRouteType</td><td>$scopedRouteType</td></tr>
       |  <tr><td>countries</td><td>$countries</td></tr>
       |  <tr><td>unexpectedNodeIds</td><td>$unexpectedNodeIds</td></tr>
       |</table>
       |""".stripMargin
  }

  private def yes(value: Boolean): String = {
    if (value) "yes" else ""
  }
}
