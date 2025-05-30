package kpn.core.tools.analysis

import kpn.core.tools.support.RawDataTool
import kpn.core.util.Log
import kpn.server.analyzer.full.analyzers.SingleBaseRouteAnalyzer

object SingleBaseRouteAnalyzerTool {

  private val log = Log(classOf[SingleBaseRouteAnalyzerTool])

  def main(args: Array[String]): Unit = {
    log.info("Start")
    val options = InitialAnalysisToolOptions("kpn-laptop")
    val configuration = new InitialAnalysisConfiguration(options)
    try {
      val tool = buildTool(configuration)
      tool.analyze(2650)
    }
    finally {
      configuration.shutdown()
    }
    log.info(s"Done")
  }

  private def buildTool(configuration: InitialAnalysisConfiguration): SingleBaseRouteAnalyzerTool = {
    new SingleBaseRouteAnalyzerTool(
      configuration.singleBaseRouteAnalyzer,
    )
  }
}

class SingleBaseRouteAnalyzerTool(singleBaseRouteAnalyzer: SingleBaseRouteAnalyzer) {

  def analyze(routeId: Long): Unit = {
    singleBaseRouteAnalyzer.processRoute(RawDataTool.timestamp, None, routeId)
  }
}
