package kpn.server.analyzer.load

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.orphan.node.OrphanNodesLoader
import kpn.server.analyzer.load.orphan.route.OrphanRoutesLoader
import org.springframework.stereotype.Component

/**
 * Loads the state of all networks at a given timestamp. Loads the current state if no timestamp is given.
 */
@Component
class AnalysisDataLoader(
  analysisContext: AnalysisContext,
  networksLoader: NetworksLoader,
  orphanRoutesLoader: OrphanRoutesLoader,
  orphanNodesLoader: OrphanNodesLoader
) {

  private val log = Log(classOf[AnalysisDataLoader])

  def load(timestamp: Timestamp): Unit = {
    Log.context(timestamp.yyyymmddhhmmss) {
      log.info("Start loading complete analysis state")
      log.infoElapsed {
        networksLoader.load(timestamp)
        orphanRoutesLoader.load(timestamp)
        orphanNodesLoader.load(timestamp)
        (s"Loaded current state (${analysisContext.data.summary})", ())
      }
    }
  }

}
