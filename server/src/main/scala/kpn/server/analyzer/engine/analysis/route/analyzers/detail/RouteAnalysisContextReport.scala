package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.core.tools.config.Dirs
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.report.RouteEdgeReport
import kpn.server.analyzer.engine.analysis.route.report.RouteFactsReport
import kpn.server.analyzer.engine.analysis.route.report.RouteLinksReport
import kpn.server.analyzer.engine.analysis.route.report.RouteNameAnalysisReport
import kpn.server.analyzer.engine.analysis.route.report.RouteNodeAnalysisReport
import kpn.server.analyzer.engine.analysis.route.report.RoutePathReport
import kpn.server.analyzer.engine.analysis.route.report.RouteSegmentReport
import kpn.server.analyzer.engine.analysis.route.report.RouteSummaryReport

import java.io.File
import java.io.PrintWriter

object RouteAnalysisContextReport {
  def report(context: RouteDetailAnalysisContext): Unit = {
    val out = {
      val dir = new File(Dirs.root, "routes")
      dir.mkdirs
      new PrintWriter(new File(dir, s"/${context.relation.id}.html"))
    }
    out.println(new RouteAnalysisContextReport(context).report())
    out.close()
  }
}

class RouteAnalysisContextReport(context: RouteDetailAnalysisContext) {

  def report(): String = {
    s"""<html>
       |${head()}
       |<body>
       |${RouteSummaryReport.report(context)}
       |${RouteFactsReport.report(context)}
       |${RouteNodeAnalysisReport.report(context)}
       |${RouteNameAnalysisReport.report(context)}
       |${RouteLinksReport.report(context)}
       |${RouteSegmentReport.report(context)}
       |${RoutePathReport.report(context)}
       |${RouteEdgeReport.report(context.edges)}
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
