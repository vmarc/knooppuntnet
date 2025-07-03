package kpn.core.tools.analysis

import kpn.core.tools.support.RawDataTool
import kpn.core.util.Log
import kpn.server.analyzer.full.analyzers.SingleBaseRouteAnalyzer
import kpn.server.analyzer.full.analyzers.SingleRouteAnalyzer

object SingleRouteAnalyzerTool {

  private val log = Log(classOf[SingleRouteAnalyzerTool])

  def main(args: Array[String]): Unit = {
    log.info("Start")
    val options = InitialAnalysisToolOptions("kpn-laptop")
    val configuration = new InitialAnalysisConfiguration(options)
    try {
      val tool = buildTool(configuration)
      tool.analyze(10879005)
    }
    finally {
      configuration.shutdown()
    }
    log.info(s"Done")
  }

  private def buildTool(configuration: InitialAnalysisConfiguration): SingleRouteAnalyzerTool = {
    new SingleRouteAnalyzerTool(
      configuration.singleBaseRouteAnalyzer,
      configuration.singleRouteAnalyzer,
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
