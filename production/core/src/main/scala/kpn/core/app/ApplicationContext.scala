package kpn.core.app

import akka.actor.ActorSystem
import kpn.core.db.couch.Couch
import kpn.core.db.couch.DatabaseImpl
import kpn.core.facade.AnalyzerFacade
import kpn.core.facade.AnalyzerFacadeImpl
import kpn.core.facade.pages.ChangeSetPageBuilderImpl
import kpn.core.facade.pages.ChangesPageBuilderImpl
import kpn.core.facade.pages.NetworkChangesPageBuilderImpl
import kpn.core.facade.pages.NetworkDetailsPageBuilderImpl
import kpn.core.facade.pages.NetworkFactsPageBuilderImpl
import kpn.core.facade.pages.NetworkMapPageBuilderImpl
import kpn.core.facade.pages.NetworkNodesPageBuilderImpl
import kpn.core.facade.pages.NetworkRoutesPageBuilderImpl
import kpn.core.facade.pages.NodePageBuilderImpl
import kpn.core.facade.pages.RoutePageBuilderImpl
import kpn.core.facade.pages.SubsetChangesPageBuilderImpl
import kpn.core.facade.pages.SubsetFactDetailsPageBuilderImpl
import kpn.core.facade.pages.SubsetFactsPageBuilderImpl
import kpn.core.facade.pages.SubsetNetworksPageBuilderImpl
import kpn.core.facade.pages.SubsetOrphanNodesPageBuilderImpl
import kpn.core.facade.pages.SubsetOrphanRoutesPageBuilderImpl
import kpn.core.overpass.OverpassQueryExecutorHttp
import kpn.core.planner.PlannerFacade
import kpn.core.planner.PlannerFacadeImpl
import kpn.core.repository.AnalysisRepositoryImpl
import kpn.core.repository.ChangeSetInfoRepositoryImpl
import kpn.core.repository.ChangeSetRepositoryImpl
import kpn.core.repository.FactRepositoryImpl
import kpn.core.repository.GraphRepositoryImpl
import kpn.core.repository.NetworkRepositoryImpl
import kpn.core.repository.NodeRepositoryImpl
import kpn.core.repository.OrphanRepositoryImpl
import kpn.core.repository.OverviewRepositoryImpl
import kpn.core.repository.ReviewRepositoryImpl
import kpn.core.repository.RouteRepositoryImpl

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ApplicationContext(system: ActorSystem, config: ApplicationConfig) {

  val couch = new Couch(system, config.couchConfig)

  val mainDatabase = new DatabaseImpl(couch, config.couchConfig.dbname)
  val changeDatabase = new DatabaseImpl(couch, config.couchConfig.changeDbname)
  private val userDatabase = new DatabaseImpl(couch, config.couchConfig.userDbname)
  private val reviewDatabase = new DatabaseImpl(couch, config.couchConfig.reviewDbname)
  private val database = mainDatabase

  private val nodeRepository = new NodeRepositoryImpl(database)
  private val routeRepository = new RouteRepositoryImpl(database)
  private val networkRepository = new NetworkRepositoryImpl(database)
  private val orphanRepository = new OrphanRepositoryImpl(database)
  private val graphRepository = new GraphRepositoryImpl(database)
  private val overviewRepository = new OverviewRepositoryImpl(database)
  private val factRepository = new FactRepositoryImpl(database)
  private val changeSetRepository = new ChangeSetRepositoryImpl(changeDatabase)
  private val changeSetInfoRepository = new ChangeSetInfoRepositoryImpl(changeDatabase)
  private val reviewRepository = new ReviewRepositoryImpl(reviewDatabase)

  private val analysisRepository = new AnalysisRepositoryImpl(
    database,
    networkRepository,
    routeRepository,
    nodeRepository
  )

  val analyzerFacade: AnalyzerFacade = {

    val nodePageBuilder = new NodePageBuilderImpl(
      nodeRepository,
      changeSetRepository,
      changeSetInfoRepository
    )

    val routePageBuilder = new RoutePageBuilderImpl(
      routeRepository,
      changeSetRepository,
      changeSetInfoRepository
    )

    val networkDetailsPageBuilder = new NetworkDetailsPageBuilderImpl(networkRepository)
    val networkMapPageBuilder = new NetworkMapPageBuilderImpl(networkRepository)
    val networkFactsPageBuilder = new NetworkFactsPageBuilderImpl(networkRepository)
    val networkNodesPageBuilder = new NetworkNodesPageBuilderImpl(networkRepository)
    val networkRoutesPageBuilder = new NetworkRoutesPageBuilderImpl(networkRepository)

    val subsetNetworksPageBuilder = new SubsetNetworksPageBuilderImpl(
      overviewRepository,
      networkRepository
    )

    val subsetFactsPageBuilder = new SubsetFactsPageBuilderImpl(
      overviewRepository
    )

    val subsetFactDetailsPageBuilder = new SubsetFactDetailsPageBuilderImpl(
      overviewRepository,
      factRepository
    )

    val subsetChangesPageBuilder = new SubsetChangesPageBuilderImpl(
      overviewRepository,
      changeSetRepository,
      changeSetInfoRepository
    )

    val subsetOrphanRoutesPageBuilder = new SubsetOrphanRoutesPageBuilderImpl(
      overviewRepository,
      orphanRepository
    )

    val subsetOrphanNodesPageBuilder = new SubsetOrphanNodesPageBuilderImpl(
      overviewRepository,
      orphanRepository
    )

    val changesPageBuilder = new ChangesPageBuilderImpl(
      changeSetRepository,
      changeSetInfoRepository
    )

    val changeSetPageBuilder = new ChangeSetPageBuilderImpl(
      changeSetInfoRepository,
      changeSetRepository,
      nodeRepository,
      routeRepository
    )

    val networkChangesPageBuilder = new NetworkChangesPageBuilderImpl(
      networkRepository,
      changeSetRepository,
      changeSetInfoRepository
    )

    new AnalyzerFacadeImpl(
      nodeRepository,
      routeRepository,
      networkRepository,
      overviewRepository,
      factRepository,
      analysisRepository,
      // ---
      nodePageBuilder,
      routePageBuilder,
      networkDetailsPageBuilder,
      networkMapPageBuilder,
      networkFactsPageBuilder,
      networkNodesPageBuilder,
      networkRoutesPageBuilder,
      subsetNetworksPageBuilder,
      subsetFactsPageBuilder,
      subsetFactDetailsPageBuilder,
      subsetChangesPageBuilder,
      subsetOrphanRoutesPageBuilder,
      subsetOrphanNodesPageBuilder,
      changesPageBuilder,
      changeSetPageBuilder,
      networkChangesPageBuilder
    )
  }

  val plannerFacade: PlannerFacade = new PlannerFacadeImpl(graphRepository, nodeRepository, routeRepository)

  val overpassQueryExecutor = new OverpassQueryExecutorHttp(system, "config.overpassAddress")

  def shutDown(): Unit = {
    couch.shutdown()
    Await.result(system.terminate(), Duration.Inf)
    ()
  }
}
