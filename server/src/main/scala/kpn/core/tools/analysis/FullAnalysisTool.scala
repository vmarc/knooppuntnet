package kpn.core.tools.analysis

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.full.MainFullAnalyzer
import kpn.server.repository.AnalysisRepository

/*
  Loads the initial analysis state with the information on "2019-11-01 00:00:00" (arbitrary taken
  as the start of the use of the new tagging scheme using "network:type=node_network").
  For all nodes and routes an initial NodeChange/RouteChange document is created. These documents
  can be used to display the oldest known state of the nodes and routes in the changes pages.
 */
object FullAnalysisTool {

  private val log = Log(classOf[FullAnalysisTool])

  def main(args: Array[String]): Unit = {

    val exit = FullAnalysisToolOptions.parse(args) match {
      case Some(options) =>

        log.info("Start")
        val configuration = new FullAnalysisConfiguration(options)
        try {
          new FullAnalysisTool(
            configuration.mainFullAnalyzer,
            configuration.analysisRepository,
            configuration.timestamp
          ).analyze()
        }
        finally {
          configuration.shutdown()
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
}

class FullAnalysisTool(
  mainFullAnalyzer: MainFullAnalyzer,
  analysisRepository: AnalysisRepository,
  timestamp: Timestamp
) {

  def analyze(): Unit = {
    mainFullAnalyzer.analyze(timestamp, initialAnalysis = true)
    analysisRepository.saveLastUpdated(timestamp)
  }
}
