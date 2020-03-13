package kpn.server.analyzer.load.orphan.route

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.routing.BalancingPool
import akka.util.Timeout
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.DatabaseIndexer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.orphan.route.OrphanRoutesLoaderImpl.LoadRoute
import kpn.server.repository.BlackListRepository
import kpn.server.repository.OrphanRepository
import org.springframework.stereotype.Component

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object OrphanRoutesLoaderImpl {

  case class LoadRoute(messages: Seq[String], timestamp: Timestamp, routeId: Long)

}

@Component
class OrphanRoutesLoaderImpl(
  system: ActorSystem,
  analysisContext: AnalysisContext,
  routeIdsLoader: RouteIdsLoader,
  orphanRepository: OrphanRepository,
  blackListRepository: BlackListRepository,
  databaseIndexer: DatabaseIndexer,
  worker: OrphanRoutesLoaderWorker
) extends OrphanRoutesLoader {

  private val log = Log(classOf[OrphanRoutesLoaderImpl])

  private implicit val askTimeout: Timeout = Timeout(32.hour)
  private implicit val executionContext: ExecutionContext = system.dispatcher

  class WorkerActor extends Actor {
    def receive: Actor.Receive = {
      case LoadRoute(messages, timestamp, routeId) =>
        Log.context(messages) {
          sender() ! worker.process(timestamp, routeId)
        }
    }
  }

  private val workerPool = {
    val props = Props(classOf[WorkerActor], this)
    system.actorOf(BalancingPool(3).props(props), "orphan-routes-loader")
  }

  def load(timestamp: Timestamp): Unit = {
    ScopedNetworkType.all.foreach { scopedNetworkType =>
      Log.context(scopedNetworkType.key) {
        databaseIndexer.index()
        val routeIds = routeIdsLoader.load(timestamp, scopedNetworkType)
        val blackListedRouteIds = blackListRepository.get.routes.map(_.id).toSet
        val candidateOrphanRouteIds = (routeIds -- blackListedRouteIds).filterNot(isReferenced).toSeq.sorted

        log.info(s"Found ${routeIds.size} routes, ${blackListedRouteIds.size} blacklisted routes, ${candidateOrphanRouteIds.size} candidate orphan routes (unreferenced)")

        val futures = candidateOrphanRouteIds.zipWithIndex.map { case (routeIds, index) =>
          Log.context(s"${index + 1}/${candidateOrphanRouteIds.size}") {
            val messages = Log.contextMessages
            workerPool ? LoadRoute(messages, timestamp, routeIds)
          }
        }
        Await.result(Future.sequence(futures), Duration.Inf)
      }
    }
  }

  private def isReferenced(routeId: Long): Boolean = {
    analysisContext.data.networks.isReferencingRelation(routeId)
  }
}
