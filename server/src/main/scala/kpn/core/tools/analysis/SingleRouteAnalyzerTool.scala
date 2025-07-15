package kpn.core.tools.analysis

import kpn.core.tools.support.RawDataTool
import kpn.core.util.Log
import kpn.server.analyzer.full.analyzers.SingleBaseRouteAnalyzer
import kpn.server.analyzer.full.analyzers.SingleRouteAnalyzer

object SingleRouteAnalyzerTool {

  def main(args: Array[String]): Unit = {
    SingleRouteAnalyzerToolOptions.parse(args).foreach(analyzeRoute)
  }

  private def analyzeRoute(options: SingleRouteAnalyzerToolOptions): Unit = {
    val configuration = buildConfiguration(options)
    try {
      val tool = buildTool(configuration)
      tool.analyze(options.routeId)
    }
    finally {
      configuration.shutdown()
    }
  }

  private def buildConfiguration(options: SingleRouteAnalyzerToolOptions) = {
    new InitialAnalysisConfiguration(
      InitialAnalysisToolOptions(options.databaseName)
    )
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
  private val log = Log(classOf[SingleRouteAnalyzerTool])

  def analyze(routeId: Long): Unit = {
    log.info(s"Start analysis of route $routeId")
    analyzeBaseRoute(routeId)
    analyzeRoute(routeId)
    log.info(s"Completed analysis of route $routeId")
  }

  private def analyzeBaseRoute(routeId: Long): Unit = {
    singleBaseRouteAnalyzer.processRoute(RawDataTool.timestamp, None, routeId)
  }

  private def analyzeRoute(routeId: Long): Unit = {
    singleRouteAnalyzer.processRoute(routeId)
  }
}
