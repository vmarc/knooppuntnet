package kpn.core.tools.config

import akka.actor.ActorSystem
import kpn.core.db.couch.Database
import kpn.server.repository.AnalysisRepositoryNoop

class ChangesToolConfiguration(system: ActorSystem, analysisDatabase: Database, changeDatabase: Database, taskDatabase: Database) {

  val analysisRepository = new AnalysisRepositoryNoop()

  val config = new Configuration(
    0,
    system,
    analysisDatabase,
    changeDatabase,
    taskDatabase,
    analysisRepository,
    analysisRepository
  )
}
