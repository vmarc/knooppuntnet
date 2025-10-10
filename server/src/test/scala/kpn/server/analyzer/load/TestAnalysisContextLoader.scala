package kpn.server.analyzer.load

import kpn.core.util.Log
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.TestAnalysisContextLoader.log
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

object TestAnalysisContextLoader {

  private val log = Log(classOf[TestAnalysisContextLoader])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      val analysisContext = new AnalysisContext()
      val loader: AnalysisContextLoader = {
        val networkRepository = new NetworkRepositoryImpl(database)
        val routeRepository = new RouteRepositoryImpl(database)
        val nodeRepository = new NodeRepositoryImpl(database)
        new AnalysisContextLoaderImpl(
          analysisContext,
          networkRepository,
          routeRepository,
          nodeRepository
        )
      }
      new TestAnalysisContextLoader(analysisContext, loader).run()
    }
  }
}

class TestAnalysisContextLoader(analysisContext: AnalysisContext, loader: AnalysisContextLoader) {
  def run(): Unit = {
    val before = bytes
    log.infoElapsed {
      loader.load()
      (s"analysis context loaded", ())
    }
    val after = bytes
    reportIdCounts(analysisContext)
    log.info(s"memory ${toMb(after - before)}")
  }

  private def reportIdCounts(analysisContext: AnalysisContext): Unit = {
    val watchedRoutes = analysisContext.watched.routes
    val routeNodeCount = watchedRoutes.ids.map(id => watchedRoutes.get(id).get.nodeIds.size).sum
    val routeWayCount = watchedRoutes.ids.map(id => watchedRoutes.get(id).get.wayIds.size).sum
    val routeRelationCount = watchedRoutes.ids.map(id => watchedRoutes.get(id).get.relationIds.size).sum
    log.info(s"nodeCount=${analysisContext.watched.nodes.size}")
    log.info(s"routeCount=${analysisContext.watched.routes.size}")
    log.info(s"networkCount=${analysisContext.watched.networks.size}")
    log.info(s"routeNodeCount=$routeNodeCount")
    log.info(s"routeWayCount=$routeWayCount")
    log.info(s"routeRelationCount=$routeRelationCount")
  }

  private def toMb(bytes: Long): String = f"${bytes / 1000000d}%.0fMb"

  private def bytes: Long = {
    System.gc()
    Runtime.getRuntime.totalMemory() - Runtime.getRuntime.freeMemory()
  }
}
