package kpn.core.tools.config

import kpn.core.db.couch.Database
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.AnalysisRepositoryImpl
import kpn.core.repository.NetworkRepositoryImpl
import kpn.core.repository.NodeRepositoryImpl
import kpn.core.repository.RouteRepositoryImpl

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
