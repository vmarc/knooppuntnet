package kpn.core.tools.config

import akka.actor.ActorSystem
import kpn.core.db.couch.OldDatabase
import kpn.server.repository.AnalysisRepositoryNoop

class AnalysisToolConfiguration(
  id: Int,
  system: ActorSystem,
  oldAnalysisDatabase: OldDatabase,
  oldChangeDatabase: OldDatabase,
  oldTaskDatabase: OldDatabase,
  initialLoad: Boolean
) {

  private val analysisRepository = new AnalysisRepositoryConfiguration(oldAnalysisDatabase).analysisRepository

  private val initialLoadAnalysisRepository = if (initialLoad) analysisRepository else new AnalysisRepositoryNoop()

  val config = new Configuration(
    id,
    system,
    oldAnalysisDatabase,
    oldChangeDatabase,
    oldTaskDatabase,
    analysisRepository,
    initialLoadAnalysisRepository
  )
}
