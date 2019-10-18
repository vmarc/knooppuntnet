package kpn.core.tools.config

import kpn.core.db.couch.OldDatabase
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.AnalysisRepositoryImpl
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class AnalysisRepositoryConfiguration(oldAnalysisDatabase: OldDatabase) {

  private val networkRepository = new NetworkRepositoryImpl(oldAnalysisDatabase)
  private val routeRepository = new RouteRepositoryImpl(oldAnalysisDatabase)
  private val nodeRepository = new NodeRepositoryImpl(oldAnalysisDatabase)

  val analysisRepository: AnalysisRepository = new AnalysisRepositoryImpl(
    oldAnalysisDatabase,
    networkRepository,
    routeRepository,
    nodeRepository
  )
}
