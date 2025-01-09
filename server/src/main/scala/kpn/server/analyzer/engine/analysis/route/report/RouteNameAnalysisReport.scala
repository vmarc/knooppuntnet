package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext

object RouteNameAnalysisReport {

  def report(context: BaseRouteAnalysisContext): String = {
    val routeNameAnalysis = context.routeNameAnalysis
    val name = routeNameAnalysis.name.getOrElse("")
    val startNodeName = routeNameAnalysis.startNodeName.getOrElse("")
    val endNodeName = routeNameAnalysis.endNodeName.getOrElse("")
    val reversed = yesNo(routeNameAnalysis.reversed)
    val derivedFromNodes = yesNo(routeNameAnalysis.derivedFromNodes)
    val derivedFromDeprecatedNoteTag = yesNo(routeNameAnalysis.derivedFromDeprecatedNoteTag)

    s"""
       |<table>
       |  <tr class="header"><td colspan="2">RouteNameAnalysis</td></tr>
       |  <tr><td>name</td><td>$name</td></tr>
       |  <tr><td>startNodeName</td><td>$startNodeName</td></tr>
       |  <tr><td>endNodeName</td><td>$endNodeName</td></tr>
       |  <tr><td>reversed</td><td>$reversed</td></tr>
       |  <tr><td>derivedFromNodes</td><td>$derivedFromNodes</td></tr>
       |  <tr><td>derivedFromDeprecatedNoteTag</td><td>$derivedFromDeprecatedNoteTag</td></tr>
       |</table>
       |""".stripMargin
  }

  private def yesNo(value: Boolean): String = {
    if (value) "yes" else "no"
  }
}
