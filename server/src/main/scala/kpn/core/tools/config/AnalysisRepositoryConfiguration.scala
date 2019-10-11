package kpn.core.tools.config

import kpn.core.db.couch.Database
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.AnalysisRepositoryImpl
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class AnalysisRepositoryConfiguration(analysisDatabase: Database) {

  private val networkRepository = new NetworkRepositoryImpl(analysisDatabase)
  private val routeRepository = new RouteRepositoryImpl(analysisDatabase)
  private val nodeRepository = new NodeRepositoryImpl(analysisDatabase)

  val analysisRepository: AnalysisRepository = new AnalysisRepositoryImpl(
    analysisDatabase,
    networkRepository,
    routeRepository,
    nodeRepository
  )
}
