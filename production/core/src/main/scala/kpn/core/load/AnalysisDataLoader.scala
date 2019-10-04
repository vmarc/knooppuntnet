package kpn.core.load

import kpn.core.engine.changes.data.AnalysisData
import kpn.core.load.orphan.node.OrphanNodesLoader
import kpn.core.load.orphan.route.OrphanRoutesLoader
import kpn.core.repository.FactRepository
import kpn.core.repository.OrphanRepository
import kpn.core.util.Log
import kpn.shared.Timestamp

/**
  * Loads the state of all networks at a given timestamp. Loads the current state if no timestamp is given.
  */
class AnalysisDataLoader(
  analysisData: AnalysisData,
  networksLoader: NetworksLoader,
  orphanRoutesLoader: OrphanRoutesLoader,
  orphanNodesLoader: OrphanNodesLoader,
  orphanRepository: OrphanRepository,
  factRepository: FactRepository
) {

  private val log = Log(classOf[AnalysisDataLoader])

  def load(timestamp: Timestamp): Unit = {
    Log.context(timestamp.yyyymmddhhmmss) {
      log.info("Start loading complete analysis state")
      log.elapsed {
        networksLoader.load(timestamp)
        orphanRoutesLoader.load(timestamp)
        orphanNodesLoader.load(timestamp)
        (s"Loaded current state (${analysisData.summary})", ())
      }
    }
  }

}
