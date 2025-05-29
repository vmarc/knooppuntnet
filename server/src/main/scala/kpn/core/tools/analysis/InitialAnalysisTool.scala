package kpn.core.tools.analysis

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.database.base.Exit
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.analyzer.full.MainFullAnalyzer
import kpn.server.repository.AnalysisRepository

/*
  Loads the initial analysis state with the information on "2019-11-01 00:00:00" (arbitrary taken
  as the start of the use of the new tagging scheme using "network:type=node_network").
  For all nodes and routes an initial NodeChange/RouteChange/NetworkChange document is created. These documents
  can be used to display the oldest known state of the nodes and routes in the changes pages.
 */
object InitialAnalysisTool {

  private val log = Log(classOf[InitialAnalysisTool])

  def main(args: Array[String]): Unit = {
    val exitCode = execute(args)
    System.exit(exitCode)
  }

  private def execute(args: Array[String]): Int = {
    InitialAnalysisToolOptions.parse(args) match {
      case Some(options) => executeWithOptions(options)
      case None => Exit.Failure
    }
  }

  private def executeWithOptions(options: InitialAnalysisToolOptions): Int = {
    val configuration = new InitialAnalysisConfiguration(options)
    try {
      runAnalysisTool(configuration)
      Exit.Success
    }
    catch {
      case exception: Throwable =>
        log.error(s"Failed: ${exception.getMessage}")
        Exit.Failure
    }
    finally {
      configuration.shutdown()
    }
  }

  private def runAnalysisTool(configuration: InitialAnalysisConfiguration): Unit = {
    log.info("Start")
    val changeSetContext = buildInitialContext()
    new InitialAnalysisTool(
      configuration.mainFullAnalyzer,
      configuration.analysisRepository,
      changeSetContext
    ).analyze()
    log.info(s"Done")
  }

  private def buildInitialContext(): ChangeSetContext = {
    val timestamp = Timestamp.analysisStart
    ChangeSetContext(
      ReplicationId(1),
      ChangeSet(
        0,
        timestamp,
        timestamp,
        timestamp,
        timestamp,
        timestamp,
        Seq.empty
      ),
      ElementIds()
    )
  }
}

class InitialAnalysisTool(
  mainFullAnalyzer: MainFullAnalyzer,
  analysisRepository: AnalysisRepository,
  changeSetContext: ChangeSetContext
) {

  def analyze(): Unit = {
    mainFullAnalyzer.analyze(changeSetContext.changeSet.timestamp, Some(changeSetContext))
    analysisRepository.saveLastUpdated(changeSetContext.changeSet.timestamp)
  }
}
