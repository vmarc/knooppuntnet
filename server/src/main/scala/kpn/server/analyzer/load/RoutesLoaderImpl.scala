package kpn.server.analyzer.load

import java.util.concurrent.CompletableFuture.allOf
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.Executor

import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.load.data.LoadedRoute
import org.springframework.stereotype.Component

@Component
class RoutesLoaderImpl(
  routeLoaderExecutor: Executor,
  routeLoader: RouteLoader
) extends RoutesLoader {

  private val log = Log(classOf[RoutesLoaderImpl])

  override def load(timestamp: Timestamp, routeIds: Seq[Long]): Seq[Option[LoadedRoute]] = {
    if (routeIds.isEmpty) {
      Seq()
    }
    else {
      log.debugElapsed {
        val futures = routeIds.zipWithIndex.map { case (routeId, index) =>
          val context = Log.contextAnd(s"${index + 1}/${routeIds.size}, route=$routeId")
          supplyAsync(() => Log.context(context)(routeLoader.loadRoute(timestamp, routeId)), routeLoaderExecutor).exceptionally { ex =>
            val message = "Exception while loading route"
            Log.context(context) {
              log.error(message, ex)
            }
            throw new RuntimeException(s"[$context] $message", ex)
          }
        }

        allOf(futures: _*).join()
        val loadedRoutes = futures.map(s => s.get())
        (s"Processed ${routeIds.size} routes", loadedRoutes)
      }
    }
  }
}
