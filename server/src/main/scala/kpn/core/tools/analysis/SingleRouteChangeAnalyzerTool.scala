package kpn.core.tools.analysis

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.ChangeSet
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Change
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.TimestampUtil
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.ChangeSetInfoUpdater
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.location.RouteLocator
import kpn.server.analyzer.engine.analysis.network.base.BaseNetworkMainAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.NetworkMainAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkCountryAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkExtraAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkNodeDocAnalyzer
import kpn.server.analyzer.engine.analysis.network.main.analyzers.NetworkRouteAnalyzer
import kpn.server.analyzer.engine.analysis.node.BaseNodeBulkAnalyzer
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.BaseNodeMainAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeCountryAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeLocationAnalyzer
import kpn.server.analyzer.engine.analysis.node.base.analyzers.BaseNodeTileAnalyzer
import kpn.server.analyzer.engine.analysis.node.main.NodeMainAnalyzer
import kpn.server.analyzer.engine.analysis.node.main.analyzers.NodeNetworkReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.node.main.analyzers.NodeRouteReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteCountryAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLocationAnalyzerImpl
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTileAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.RouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteBoundsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteIdsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteNetworkReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteParentAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteStructureRowsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteSuperSegmentAnalyzer
import kpn.server.analyzer.engine.changes.ChangeProcessorPipeline
import kpn.server.analyzer.engine.changes.ChangeSaver
import kpn.server.analyzer.engine.changes.ChangeSetProcessor
import kpn.server.analyzer.engine.changes.ElementIdAnalyzer
import kpn.server.analyzer.engine.changes.network.base.BaseNetworkChangeAnalyzer
import kpn.server.analyzer.engine.changes.network.base.BaseNetworkChangeProcessor
import kpn.server.analyzer.engine.changes.network.main.NetworkChangeProcessor
import kpn.server.analyzer.engine.changes.node.base.BaseNodeChangeAnalyzer
import kpn.server.analyzer.engine.changes.node.base.BaseNodeChangeProcessor
import kpn.server.analyzer.engine.changes.node.main.NodeChangeProcessor
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeAnalyzer
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeCreateProcessor
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeDeleteProcessor
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeDeleterImpl
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeProcessor
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeUpdateProcessor
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeUpdateTileProcessor
import kpn.server.analyzer.engine.changes.route.base.BaseRouteChangeUpdateWayProcessor
import kpn.server.analyzer.engine.changes.route.base.BaseRouteDiffAnalyzer
import kpn.server.analyzer.engine.changes.route.base.RouteGeometryAnalyzer
import kpn.server.analyzer.engine.changes.route.main.RouteChangeCreateProcessor
import kpn.server.analyzer.engine.changes.route.main.RouteChangeDeleteProcessor
import kpn.server.analyzer.engine.changes.route.main.RouteChangeProcessor
import kpn.server.analyzer.engine.changes.route.main.RouteChangeUpdateProcessor
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.ElementIds
import kpn.server.analyzer.engine.tile.LineSegmentTileCalculator
import kpn.server.analyzer.engine.tile.NodeTileCalculator
import kpn.server.analyzer.engine.tile.NodeTileChangeAnalyzer
import kpn.server.analyzer.engine.tile.RouteTileCache
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilder
import kpn.server.overpass.OverpassRepositoryImpl
import kpn.server.repository.BlacklistRepositoryImpl
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkInfoRepository
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NodeRepository
import kpn.server.repository.RawDataRepositoryImpl
import kpn.server.repository.RouteRepository
import kpn.server.repository.RouteTileRepository
import kpn.server.repository.TaskRepositoryImpl

object SingleRouteChangeAnalyzerTool {
  def main(args: Array[String]): Unit = {
    val relationId = 11140514
    val replicationId = ReplicationId(0)
    val changeSetId = 176115830
    val timestamp = Timestamp.apply(2025, 12, 18, 22, 30, 30)

    val changeSets = Seq(
      ChangeSet(
        id = changeSetId,
        timestamp = timestamp, // timestamp found in minute diff state file
        timestampFrom = timestamp,
        timestampUntil = timestamp,
        timestampBefore = TimestampUtil.relativeSeconds(timestamp, -1),
        timestampAfter = TimestampUtil.relativeSeconds(timestamp, 1),
        changes = Seq(
          Change(
            action = ChangeAction.Modify,
            nodes = Seq.empty, // Seq[RawNode],
            ways = Seq.empty, // Seq[RawWay],
            relations = Seq(
              RawRelation(
                id = relationId,
                version = 0,
                timestamp = timestamp,
                changeSetId = changeSetId,
                members = Seq.empty,
                tags = Tags.from(
                  "type" -> "route",
                  "route" -> "hiking"
                )
              )
            )
          )
        )
      )
    )

    Mongo.executeIn("test") { database =>
      val configuration = new SingleRouteChangeAnalyzerConfiguration(database)
      configuration.analysisContext.watched.routes.add(relationId, ElementIds())

      val processor = new ChangeSetProcessor(configuration.changeProcessorPipeline)
      processor.processChangeSets(replicationId, changeSets)
    }
  }
}

class SingleRouteChangeAnalyzerTool {
}

class SingleRouteChangeAnalyzerConfiguration(database: Database) {
  val analysisContext = new AnalysisContext()
  val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl("http://server-1:9005/api/overpass")

  val overpassRepository = new OverpassRepositoryImpl(
    overpassQueryExecutor: OverpassQueryExecutor
  )

  private val changeSetRepository = new ChangeSetRepository(database)
  val nodeRepository = new NodeRepository(database)

  private val routeRepository = new RouteRepository(database)
  private val routeTileRepository = new RouteTileRepository(database)
  private val baseRouteRepository = new RouteRepository(database)
  private val networkRepository = new NetworkRepository(database)

  private val rawDataRepository = new RawDataRepositoryImpl(overpassRepository)
  private val changeSetInfoRepository = new ChangeSetInfoRepository(database)
  private val networkInfoRepository = new NetworkInfoRepository(database)

  private val taskRepository = new TaskRepositoryImpl(database)

  private val blacklistRepository = new BlacklistRepositoryImpl(database)
  val locationAnalyzer: LocationAnalyzer = new LocationAnalyzerImpl(true, false)

  private val routeTileCache = new RouteTileCache()
  private val lineSegmentTileCalculator = new LineSegmentTileCalculator(routeTileCache)
  private val routeTileAnalyzer = new BaseRouteTileAnalyzer(lineSegmentTileCalculator)
  private val routeCountryAnalyzer = new BaseRouteCountryAnalyzerImpl(locationAnalyzer, routeRepository)
  private val routeLocator = new RouteLocator(locationAnalyzer)
  private val routeLocationAnalyzer = new BaseRouteLocationAnalyzerImpl(
    routeRepository,
    routeLocator
  )
  private val baseRouteMainAnalyzer = new BaseRouteMainAnalyzer(
    routeCountryAnalyzer,
    routeLocationAnalyzer,
    routeTileAnalyzer
  )

  private val routeMainAnalyzer = {
    val routeBoundsAnalyzer = new RouteBoundsAnalyzer(baseRouteRepository)
    val routeIdsAnalyzer = new RouteIdsAnalyzer(baseRouteRepository)
    val routeSuperSegmentAnalyzer = new RouteSuperSegmentAnalyzer(routeRepository)
    val routeStructureRowsAnalyzer = new RouteStructureRowsAnalyzer(baseRouteRepository)
    val routeParentAnalyzer = new RouteParentAnalyzer(baseRouteRepository)
    val networkReferencesAnalyzer = new RouteNetworkReferencesAnalyzer(networkRepository)
    new RouteMainAnalyzer(
      routeBoundsAnalyzer,
      routeIdsAnalyzer,
      routeSuperSegmentAnalyzer,
      routeStructureRowsAnalyzer,
      routeParentAnalyzer,
      networkReferencesAnalyzer,
    )
  }

  private val elementIdAnalyzer = new ElementIdAnalyzer(analysisContext)

  val nodeRouteReferencesAnalyzer = new NodeRouteReferencesAnalyzer(nodeRepository)

  private val routeTileChangeAnalyzer = new BaseRouteChangeUpdateTileProcessor(routeTileRepository)

  private val changeSetInfoUpdater = new ChangeSetInfoUpdater(
    changeSetInfoRepository,
    taskRepository
  )

  private val routeChangeCreateProcessor = new RouteChangeCreateProcessor()
  private val routeChangeUpdateProcessor = new RouteChangeUpdateProcessor()
  private val routeChangeDeleteProcessor = new RouteChangeDeleteProcessor()

  private val routeChangeProcessor: RouteChangeProcessor = new RouteChangeProcessor(
    analysisContext,
    routeMainAnalyzer,
    routeRepository,
    routeChangeCreateProcessor,
    routeChangeUpdateProcessor,
    routeChangeDeleteProcessor
  )

  private val nodeChangeAnalyzer = new BaseNodeChangeAnalyzer(
    analysisContext,
    blacklistRepository
  )

  private val nodeTileChangeAnalyzer = new NodeTileChangeAnalyzer(
    new TileDataNodeBuilder()
  )

  private val bulkNodeAnalyzer = {
    val nodeNetworkReferencesAnalyzer = new NodeNetworkReferencesAnalyzer(networkRepository)
    val nodeMainAnalyzer = new NodeMainAnalyzer(
      nodeRouteReferencesAnalyzer,
      nodeNetworkReferencesAnalyzer,
    )
    new BulkNodeAnalyzer(
      rawDataRepository,
      nodeMainAnalyzer,
      nodeRepository,
    )
  }

  private val nodeChangeProcessor = new NodeChangeProcessor(
    bulkNodeAnalyzer,
    nodeChangeAnalyzer,
    nodeRepository,
    networkRepository,
    nodeTileChangeAnalyzer
  )

  private val baseNetworkMainAnalyzer = new BaseNetworkMainAnalyzer()

  private val networkMainAnalyzer = {
    val networkInfoRouteAnalyzer = new NetworkRouteAnalyzer(routeRepository)
    val networkInfoNodeDocAnalyzer = new NetworkNodeDocAnalyzer(nodeRepository)
    val networkCountryAnalyzer = new NetworkCountryAnalyzer(locationAnalyzer)
    val networkInfoExtraAnalyzer = new NetworkExtraAnalyzer(overpassRepository)
    new NetworkMainAnalyzer(
      database,
      networkInfoRouteAnalyzer,
      networkInfoNodeDocAnalyzer,
      networkCountryAnalyzer,
      networkInfoExtraAnalyzer
    )
  }

  private val baseNodeMainAnalyzer = {
    val nodeTileCalculator: NodeTileCalculator = new NodeTileCalculator(new RouteTileCache())
    val baseNodeCountryAnalyzer = new BaseNodeCountryAnalyzer(locationAnalyzer)
    val baseNodeLocationAnalyzer = new BaseNodeLocationAnalyzer(locationAnalyzer)
    val baseNodeTileAnalyzer = new BaseNodeTileAnalyzer(nodeTileCalculator)
    new BaseNodeMainAnalyzer(
      baseNodeCountryAnalyzer,
      baseNodeLocationAnalyzer,
      baseNodeTileAnalyzer
    )
  }

  private val baseRouteDocBuilder = new BaseRouteDocBuilder()

  val changeProcessorPipeline: ChangeProcessorPipeline = {

    val changeSaver = new ChangeSaver(
      changeSetRepository,
      networkInfoRepository
    )

    val baseNodeBulkAnalyzer = new BaseNodeBulkAnalyzer(
      rawDataRepository,
      baseNodeMainAnalyzer,
    )

    val networkChangeProcessor = {

      new NetworkChangeProcessor(
        analysisContext,
        networkRepository,
        networkMainAnalyzer,
      )
    }

    val baseNetworkChangeProcessor = {
      val baseNetworkChangeAnalyzer = new BaseNetworkChangeAnalyzer(
        analysisContext,
        blacklistRepository
      )
      new BaseNetworkChangeProcessor(
        analysisContext,
        baseNetworkChangeAnalyzer,
        rawDataRepository,
        networkRepository,
        baseNetworkMainAnalyzer,
      )
    }

    val baseNodeChangeProcessor = new BaseNodeChangeProcessor(
      analysisContext,
      nodeChangeAnalyzer: BaseNodeChangeAnalyzer,
      baseNodeBulkAnalyzer: BaseNodeBulkAnalyzer,
      nodeRepository: NodeRepository
    )

    val baseRouteChangeProcessor = {
      val routeChangeAnalyzer = new BaseRouteChangeAnalyzer(
        analysisContext,
        blacklistRepository,
        elementIdAnalyzer
      )

      val baseRouteChangeCreateProcessor = new BaseRouteChangeCreateProcessor(
        analysisContext,
        rawDataRepository,
        routeRepository,
        routeTileRepository,
        baseRouteMainAnalyzer,
        baseRouteDocBuilder
      )

      val baseRouteDeleter = new BaseRouteChangeDeleterImpl(
        analysisContext,
        routeRepository,
        routeTileRepository
      )

      val baseRouteChangeUpdateWayProcessor = new BaseRouteChangeUpdateWayProcessor()
      val baseRouteDiffAnalyzer = new BaseRouteDiffAnalyzer()
      val routeGeometryAnalyzer = new RouteGeometryAnalyzer()

      val baseRouteChangeUpdateProcessor = new BaseRouteChangeUpdateProcessor(
        analysisContext,
        rawDataRepository,
        routeRepository,
        baseRouteMainAnalyzer,
        baseRouteDocBuilder,
        baseRouteChangeUpdateWayProcessor,
        baseRouteDiffAnalyzer,
        routeGeometryAnalyzer,
        routeTileChangeAnalyzer,
        baseRouteDeleter
      )

      val baseRouteChangeDeleteProcessor = new BaseRouteChangeDeleteProcessor(
        baseRouteDeleter
      )

      new BaseRouteChangeProcessor(
        routeChangeAnalyzer,
        baseRouteChangeCreateProcessor,
        baseRouteChangeUpdateProcessor,
        baseRouteChangeDeleteProcessor,
      )
    }

    new ChangeProcessorPipeline(
      baseNodeChangeProcessor,
      baseNetworkChangeProcessor,
      baseRouteChangeProcessor,
      networkChangeProcessor,
      routeChangeProcessor,
      nodeChangeProcessor,
      changeSetInfoUpdater,
      changeSaver
    )
  }
}

