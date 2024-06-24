package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteFactsReport {

  def report(context: RouteAnalysisContext): String = {
    if (context.facts != context.oldFacts) {
      s"""<table>
         |  <tr class="header">
         |    <td colspan="2">
         |      FACTS DO NOT MATCH
         |    </td>
         |  </tr>
         |  <tr>
         |    <td>
         |      old facts
         |    </td>
         |    <td>
         |      ${context.oldFacts.map(_.name).mkString(", ")}
         |    </td>
         |  </tr>
         |  <tr>
         |    <td>
         |      new facts
         |    </td>
         |    <td>
         |      ${context.facts.map(_.name).mkString(", ")}
         |    </td>
         |  </tr>
         |</table>
         |""".stripMargin
    }
    else if (context.facts.nonEmpty) {
      s"""<table>
         |  <tr>
         |    <td>
         |      facts
         |    </td>
         |    <td>
         |      ${context.facts.map(_.name).mkString(", ")}
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
