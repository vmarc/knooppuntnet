package kpn.core.tools.config

import akka.actor.ActorSystem
import kpn.core.db.couch.Database
import kpn.core.repository.AnalysisRepositoryNoop

class AnalysisToolConfiguration(
  system: ActorSystem,
  analysisDatabase: Database,
  changeDatabase: Database,
  taskDatabase: Database,
  initialLoad: Boolean
) {

  private val analysisRepository = new AnalysisRepositoryConfiguration(analysisDatabase).analysisRepository

  private val initialLoadAnalysisRepository = if (initialLoad) analysisRepository else new AnalysisRepositoryNoop()

  val config = new Configuration(
    system,
    analysisDatabase,
    changeDatabase,
    taskDatabase,
    analysisRepository,
    initialLoadAnalysisRepository
  )
}
