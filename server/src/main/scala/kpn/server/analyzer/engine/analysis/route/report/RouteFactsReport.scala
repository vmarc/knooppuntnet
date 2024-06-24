package kpn.server.analyzer.engine.analysis.route.report

import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteFactsReport {

  def report(context: RouteAnalysisContext): String = {
    if (context.facts != context.oldFacts) {
      s"""<pre>
         |FACTS DO NOT MATCH
         |  oldFacts=${context.oldFacts.map(_.name).mkString(", ")}
         |  newFacts=${context.facts.map(_.name).mkString(", ")}
         |</pre>
         |""".stripMargin
    }
    else {
      s"""<pre>
         |facts=${context.facts.map(_.name).mkString(", ")}
         |</pre>
         |""".stripMargin
    }
  }
}
