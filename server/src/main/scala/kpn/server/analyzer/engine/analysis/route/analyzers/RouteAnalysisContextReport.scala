package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.core.tools.config.Dirs
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.report.RouteFactsReport
import kpn.server.analyzer.engine.analysis.route.report.RouteMembersReport
import kpn.server.analyzer.engine.analysis.route.report.RouteSegmentsReport
import kpn.server.analyzer.engine.analysis.route.report.RouteSummaryReport
import kpn.server.analyzer.engine.analysis.route.report.StructureElementGroupsReport
import kpn.server.analyzer.engine.analysis.route.report.StructureReport

import java.io.File
import java.io.PrintWriter

object RouteAnalysisContextReport {
  def report(context: RouteAnalysisContext): Unit = {
    val out = {
      val dir = new File(Dirs.root, "routes")
      dir.mkdirs
      new PrintWriter(new File(dir, s"/${context.relation.id}.html"))
    }
    out.println(new RouteAnalysisContextReport(context).report())
    out.close()
  }
}

class RouteAnalysisContextReport(context: RouteAnalysisContext) {

  def report(): String = {
    s"""<html>
       |${head()}
       |<body>
       |${RouteSummaryReport.report(context)}
       |${RouteFactsReport.report(context)}
       |${RouteMembersReport.report(context)}
       |${RouteSegmentsReport.report(context)}
       |${StructureElementGroupsReport.report(context.segmentAnalysis.get.elementGroups)}
       |${StructureReport.report(context.segmentAnalysis.get.structure)}
       |</body>
       |</html>
       |""".stripMargin
  }

  private def head(): String = {
    s"""<head>
       |  <meta http-equiv="content-type" content="text/html; charset=UTF-8">
       |  <link href="styles.css" rel="stylesheet" type="text/css">
       |  <title>${context.relation.id} route</title>
       |  <style>
       |  body {
       |    font-family: monospace;
       |  }
       |  table {
       |    margin: 30;
       |  }
       |  table, th, td {
       |    border: 1px solid gray;
       |    border-collapse: collapse;
       |  }
       |  td {
       |    padding: .5em;
       |  }
       |  .header {
       |    background-color: #f8f8f8;
       |  }
       |  .spacer {
       |    min-width: 1em;
       |    border-left: none;
       |    border-right: none;
       |  }
       |
       |  </style>
       |</head>
       |""".stripMargin
  }
}
