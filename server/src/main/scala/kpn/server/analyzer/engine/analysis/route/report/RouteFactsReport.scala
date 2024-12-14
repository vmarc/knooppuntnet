package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouteFactsReport {

  def report(context: RouteDetailAnalysisContext): String = {
    if (context.facts.nonEmpty) {
      s"""<table>
         |  <tr>
         |    <td>
         |      facts
         |    </td>
         |    <td>
         |      ${context.facts.map(_.entryName).mkString(", ")}
         |    </td>
         |  </tr>
         |</table>
         |""".stripMargin
    }
    else {
      s"""<table>
         |  <tr>
         |    <td>
         |      OK - no facts
         |    </td>
         |  </tr>
         |</table>
         |""".stripMargin
    }
  }
}
