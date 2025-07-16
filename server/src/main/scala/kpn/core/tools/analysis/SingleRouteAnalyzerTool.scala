package kpn.core.tools.analysis

import kpn.core.tools.support.RawDataTool
import kpn.core.util.Log
import kpn.database.base.Options
import kpn.database.base.Tool
import kpn.server.analyzer.full.analyzers.SingleBaseRouteAnalyzer
import kpn.server.analyzer.full.analyzers.SingleRouteAnalyzer

object SingleRouteAnalyzerTool extends Tool[SingleRouteAnalyzerToolOptions] {

  override def options: Options[SingleRouteAnalyzerToolOptions] = SingleRouteAnalyzerToolOptions

  override def execute(options: SingleRouteAnalyzerToolOptions): Unit = {
    val configuration = new AnalysisConfiguration(options.databaseName)
    try {
      val tool = buildTool(configuration)
      tool.analyze(options.routeId)
    }
    finally {
      configuration.shutdown()
    }
  }

  private def buildTool(configuration: AnalysisConfiguration): SingleRouteAnalyzerTool = {
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
