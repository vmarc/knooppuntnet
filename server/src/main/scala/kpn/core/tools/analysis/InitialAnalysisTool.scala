package kpn.core.tools.analysis

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.database.base.Options
import kpn.database.base.Tool
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.ChangeElementIds
import kpn.server.analyzer.full.MainFullAnalyzer
import kpn.server.repository.AnalysisRepository

/*
  Loads the initial analysis state with the information on "2019-11-01 00:00:00" (arbitrary taken
  as the start of the use of the new tagging scheme using "network:type=node_network").
  For all nodes and routes an initial NodeChange/RouteChange/NetworkChange document is created. These documents
  can be used to display the oldest known state of the nodes and routes in the changes pages.
 */
object InitialAnalysisTool extends Tool[InitialAnalysisToolOptions] {

  private val log = Log(classOf[InitialAnalysisTool])

  override def options: Options[InitialAnalysisToolOptions] = InitialAnalysisToolOptions

  override def execute(options: InitialAnalysisToolOptions): Unit = {
    log.info("Start")
    val configuration = new AnalysisConfiguration(options.databaseName)
    try {
      val tool = buildTool(configuration)
      tool.analyze()
    }
    finally {
      configuration.shutdown()
    }
    log.info(s"Done")
  }

  private def buildTool(configuration: AnalysisConfiguration): InitialAnalysisTool = {
    val changeSetContext = buildInitialContext()
    new InitialAnalysisTool(
      configuration.mainFullAnalyzer,
      configuration.analysisRepository,
      changeSetContext
    )
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
      ChangeElementIds()
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
