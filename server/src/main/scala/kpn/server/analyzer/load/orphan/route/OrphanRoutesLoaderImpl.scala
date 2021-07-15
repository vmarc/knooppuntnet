package kpn.server.analyzer.load.orphan.route

import java.util.concurrent.CompletableFuture.allOf
import java.util.concurrent.CompletableFuture.runAsync
import java.util.concurrent.Executor

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.DatabaseIndexer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.BlackListRepository
import org.springframework.stereotype.Component

@Component
class OrphanRoutesLoaderImpl(
  analysisExecutor: Executor,
  analysisContext: AnalysisContext,
  routeIdsLoader: RouteIdsLoader,
  blackListRepository: BlackListRepository,
  databaseIndexer: DatabaseIndexer,
  worker: OrphanRoutesLoaderWorker
) extends OrphanRoutesLoader {

  private val log = Log(classOf[OrphanRoutesLoaderImpl])

  def load(timestamp: Timestamp): Unit = {
    ScopedNetworkType.all.foreach { scopedNetworkType =>
      Log.context(scopedNetworkType.key) {
        databaseIndexer.index(true)
        val routeIds = routeIdsLoader.load(timestamp, scopedNetworkType)
        val blackListedRouteIds = blackListRepository.get.routes.map(_.id).toSet
        val candidateOrphanRouteIds = (routeIds -- blackListedRouteIds).filterNot(isReferenced).toSeq.sorted

        log.info(s"Found ${routeIds.size} routes, ${blackListedRouteIds.size} blacklisted routes, ${candidateOrphanRouteIds.size} candidate orphan routes (unreferenced)")

        val futures = candidateOrphanRouteIds.zipWithIndex.map { case (routeId, index) =>
          val context = Log.contextAnd(s"${index + 1}/${candidateOrphanRouteIds.size}, routeId=$routeId")
          runAsync(() => Log.context(context)(worker.process(timestamp, routeId)), analysisExecutor).exceptionally { ex =>
            val message = s"Exception while loading orphan route $routeId"
            Log.context(context) {
              log.error(message, ex)
            }
            throw new RuntimeException(s"[$context] $message")
          }
        }
        allOf(futures: _*).join()
      }
    }
  }

  private def isReferenced(routeId: Long): Boolean = {
    analysisContext.data.networks.isReferencingRelation(routeId)
  }
}
