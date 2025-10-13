package kpn.server.analyzer.load

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongSet
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
    val before = Util.memoryUsed()
    log.infoElapsed {
      loader.load()
      (s"analysis context loaded", ())
    }
    val after = Util.memoryUsed()

    log.infoElapsed {
      addExtraMaps()
      (s"add extra maps", ())
    }

    val after2 = Util.memoryUsed()
    reportIdCounts(analysisContext)

    log.info(s"nodeRoutes")
    printMapEntrySizes(analysisContext.watched.nodeRoutes)

    log.info(s"wayRoutes")
    printMapEntrySizes(analysisContext.watched.wayRoutes)

    log.info(s"relationRoutes")
    printMapEntrySizes(analysisContext.watched.relationRoutes)

    log.info(s"memory ${Util.humanReadableBytes(after - before)}")
    log.info(s"memory ${Util.humanReadableBytes(after2 - after)}")
  }

  private def addExtraMaps(): Unit = {
    val routeCount = analysisContext.watched.routes.size
    analysisContext.watched.routes.ids.toSeq.zipWithIndex.foreach { case (routeId, index) =>
      if (index % 100 == 0) {
        log.info(s"$index/$routeCount")
      }
      val elementIds = analysisContext.watched.routes.get(routeId).get
      updateRouteMap(elementIds.nodeIds, routeId, analysisContext.watched.nodeRoutes)
      updateRouteMap(elementIds.wayIds, routeId, analysisContext.watched.wayRoutes)
      updateRouteMap(elementIds.relationIds, routeId, analysisContext.watched.relationRoutes)
    }
  }

  private def updateRouteMap(
    elementIds: LongSet,
    routeId: Long,
    routeMap: Long2ObjectOpenHashMap[LongArrayList]
  ): Unit = {
    val iterator = elementIds.iterator()
    while (iterator.hasNext) {
      val elementId = iterator.nextLong
      val routeIds = routeMap.get(elementId)
      if (routeIds == null) {
        routeMap.put(elementId, LongArrayList.of(routeId))
      }
      else {
        routeIds.add(routeId)
      }
    }
  }

  private def reportIdCounts(analysisContext: AnalysisContext): Unit = {
    val watchedRoutes = analysisContext.watched.routes
    val routeNodeCount = watchedRoutes.values.map(_.nodeIds.size).sum
    val routeWayCount = watchedRoutes.values.map(_.wayIds.size).sum
    val routeRelationCount = watchedRoutes.values.map(_.relationIds.size).sum
    log.info(s"nodeCount=${Util.humanReadableBytes(analysisContext.watched.nodes.size)}")
    log.info(s"routeCount=${Util.humanReadableBytes(analysisContext.watched.routes.size)}")
    log.info(s"networkCount=${Util.humanReadableBytes(analysisContext.watched.networks.size)}")
    log.info(s"routeNodeCount=${Util.humanReadableBytes(routeNodeCount)}")
    log.info(s"routeWayCount=${Util.humanReadableBytes(routeWayCount)}")
    log.info(s"routeRelationCount=$routeRelationCount")
  }

  private def bytes: Long = {
    System.gc()
    Runtime.getRuntime.totalMemory() - Runtime.getRuntime.freeMemory()
  }

  private def printMapEntrySizes(map: Long2ObjectOpenHashMap[LongArrayList]): Unit = {

    val counts = scala.collection.mutable.Map[Int, Int]()
    val i = map.keySet.iterator()
    while (i.hasNext) {
      val key = i.nextLong()
      val values = map.get(key)
      val size = values.size
      counts.updateWith(size) {
        case None => Some(1)
        case Some(count) => Some(count + 1)
      }
    }
    counts.foreach { case (arraySize, count) =>
      log.info(s"  array size $arraySize: $count entries")
    }
  }
}
