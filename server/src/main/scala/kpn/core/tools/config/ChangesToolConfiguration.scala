package kpn.core.tools.config

import akka.actor.ActorSystem
import kpn.core.db.couch.OldDatabase
import kpn.server.repository.AnalysisRepositoryNoop

class ChangesToolConfiguration(system: ActorSystem, oldAnalysisDatabase: OldDatabase, oldChangeDatabase: OldDatabase, oldTaskDatabase: OldDatabase) {

  val analysisRepository = new AnalysisRepositoryNoop()

  val config = new Configuration(
    0,
    system,
    oldAnalysisDatabase,
    oldChangeDatabase,
    oldTaskDatabase,
    analysisRepository,
    analysisRepository
  )
}
