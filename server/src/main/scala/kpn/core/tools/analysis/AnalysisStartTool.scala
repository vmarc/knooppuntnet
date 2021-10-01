package kpn.core.tools.analysis

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.post.OrphanNodeUpdater
import kpn.server.analyzer.engine.analysis.post.OrphanRouteUpdater
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy
import scala.concurrent.ExecutionContext

/*
  Loads the initial analysis state with the information on "2019-11-01 00:00:00" (arbitrary taken
  as the start of the use of the new tagging scheme using "network:type=node_network").
  For all nodes and routes an initial NodeChange/RouteChange document is created. These documents
  can be used to display the oldest known state of the nodes and routes in the changes pages.
 */
object AnalysisStartTool {

  private val log = Log(classOf[AnalysisStartTool])

  def main(args: Array[String]): Unit = {

    val exit = AnalysisStartToolOptions.parse(args) match {
      case Some(options) =>

        log.info("Start")
        val executor = buildExecutor()
        try {
          val configuration = new AnalysisStartConfiguration(options)
          implicit val executionContext: ExecutionContext = ExecutionContext.fromExecutor(executor)
          val networkAnalyzer = new AnalysisStartNetworkAnalyzer(log, configuration)
          val routeAnalyzer = new AnalysisStartRouteAnalyzer(log, configuration)
          val nodeAnalyzer = new AnalysisStartNodeAnalyzer(log, configuration)
          val networkInfoAnalyzer = new AnalysisStartNetworkInfoAnalyzer(log, configuration)

          new AnalysisStartTool(
            networkAnalyzer,
            routeAnalyzer,
            nodeAnalyzer,
            networkInfoAnalyzer,
            configuration.orphanNodeUpdater,
            configuration.orphanRouteUpdater,
            configuration.statisticsUpdater
          ).analyze()
        }
        finally {
          executor.shutdown()
          log.info(s"Done")
          ()
        }

        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }

    System.exit(exit)
  }

  private def buildExecutor(): ThreadPoolTaskExecutor = {
    val executor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(6)
    executor.setMaxPoolSize(6)
    executor.setRejectedExecutionHandler(new CallerRunsPolicy)
    executor.setThreadNamePrefix("analyzer-start-")
    executor.initialize()
    executor
  }
}

class AnalysisStartTool(
  networkAnalyzer: AnalysisStartNetworkAnalyzer,
  routeAnalyzer: AnalysisStartRouteAnalyzer,
  nodeAnalyzer: AnalysisStartNodeAnalyzer,
  networkInfoAnalyzer: AnalysisStartNetworkInfoAnalyzer,
  orphanNodeUpdater: OrphanNodeUpdater,
  orphanRouteUpdater: OrphanRouteUpdater,
  statisticsUpdater: StatisticsUpdater
) {

  def analyze(): Unit = {
    val networkIds = networkAnalyzer.analyze()
    routeAnalyzer.analyze()
    nodeAnalyzer.analyze()
    networkInfoAnalyzer.analyze(networkIds)
    orphanNodeUpdater.update()
    orphanRouteUpdater.update()
    statisticsUpdater.update()
  }
}
