package kpn.server.analyzer.load

import kpn.core.util.Log
import kpn.core.util.Util
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
        new AnalysisContextLoader(
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
    val before = Util.memoryUsed()
    log.infoElapsed {
      loader.load()
      (s"analysis context loaded", ())
    }
    val after = Util.memoryUsed()

    log.info(s"memory ${Util.humanReadableBytes(after - before)}")
  }

  private def reportIdCounts(analysisContext: AnalysisContext): Unit = {
    val watchedRoutes = analysisContext.watched.routes
    log.info(s"nodeCount=${Util.humanReadableBytes(analysisContext.watched.nodes.size)}")
    log.info(s"routeCount=${Util.humanReadableBytes(analysisContext.watched.routes.size)}")
    log.info(s"networkCount=${Util.humanReadableBytes(analysisContext.watched.networks.size)}")
  }

  private def bytes: Long = {
    System.gc()
    Runtime.getRuntime.totalMemory() - Runtime.getRuntime.freeMemory()
  }
}
