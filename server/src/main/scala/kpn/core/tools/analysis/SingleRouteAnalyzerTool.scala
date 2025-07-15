package kpn.core.tools.analysis

import kpn.core.tools.support.RawDataTool
import kpn.core.util.Log
import kpn.server.analyzer.full.analyzers.SingleBaseRouteAnalyzer
import kpn.server.analyzer.full.analyzers.SingleRouteAnalyzer

object SingleRouteAnalyzerTool {

  private val log = Log(classOf[SingleRouteAnalyzerTool.type])

  def main(args: Array[String]): Unit = {
    SingleRouteAnalyzerToolOptions.parse(args).foreach(analyzeRoute)
  }

  private def analyzeRoute(options: SingleRouteAnalyzerToolOptions): Unit = {
    log.info(s"Start analysis of route ${options.routeId}")
    val configuration = new InitialAnalysisConfiguration(
      InitialAnalysisToolOptions(options.databaseName)
    )
    try {
      val tool = buildTool(configuration)
      tool.analyze(options.routeId)
    }
    finally {
      configuration.shutdown()
    }
    log.info(s"Completed analysis of route ${options.routeId}")
  }

  private def buildTool(configuration: InitialAnalysisConfiguration): SingleRouteAnalyzerTool = {
    new SingleRouteAnalyzerTool(
      configuration.singleBaseRouteAnalyzer,
      configuration.singleRouteAnalyzer
    )
  }
}

class SingleRouteAnalyzerTool(
  singleBaseRouteAnalyzer: SingleBaseRouteAnalyzer,
  singleRouteAnalyzer: SingleRouteAnalyzer
) {
  def analyze(routeId: Long): Unit = {
    singleBaseRouteAnalyzer.processRoute(RawDataTool.timestamp, None, routeId)
    singleRouteAnalyzer.processRoute(routeId)
  }
}
