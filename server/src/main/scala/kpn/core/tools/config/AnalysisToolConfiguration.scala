package kpn.core.tools.config

import akka.actor.ActorSystem
import kpn.core.database.Database
import kpn.server.repository.AnalysisRepositoryNoop

class AnalysisToolConfiguration(
  id: Int,
  system: ActorSystem,
  analysisDatabase: Database,
  changeDatabase: Database,
  taskDatabase: Database,
  initialLoad: Boolean
) {

  private val analysisRepository = new AnalysisRepositoryConfiguration(analysisDatabase).analysisRepository

  private val initialLoadAnalysisRepository = if (initialLoad) analysisRepository else new AnalysisRepositoryNoop()

  val config = new Configuration(
    id,
    system,
    analysisDatabase,
    changeDatabase,
    taskDatabase,
    analysisRepository,
    initialLoadAnalysisRepository
  )
}
