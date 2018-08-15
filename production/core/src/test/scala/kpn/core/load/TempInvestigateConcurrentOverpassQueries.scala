package kpn.core.load

import kpn.core.app.ActorSystemConfig
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorWithThrotteling
import kpn.core.util.Log
import kpn.shared.NetworkType
import kpn.shared.Timestamp

object TempInvestigateConcurrentOverpassQueries {
  def main(args: Array[String]): Unit = {
    new TempInvestigateConcurrentOverpassQueries().run()
  }
}

class TempInvestigateConcurrentOverpassQueries {

  def run(): Unit = {

    val log = Log(classOf[TempSavePerformanceDemo])

    val system = ActorSystemConfig.actorSystem()
    try {
      val nonCachingExecutor = new OverpassQueryExecutorWithThrotteling(system, new OverpassQueryExecutorImpl())
      val networkIdsLoader = new NetworkIdsLoaderImpl(nonCachingExecutor)
      val networkLoader = new NetworkLoaderImpl(nonCachingExecutor)

      val ids = networkIdsLoader.load(Timestamp(2016, 1, 1), NetworkType.bicycle)

      println(s"About to load ${ids.size} networks")

      ids.zipWithIndex.par.foreach { case (id, index) =>
        log.elapsed {
          log.info(s"network $index/${ids.size} $id start")
          networkLoader.load(None, id)
          (s"network $index/${ids.size} $id", id)
        }
      }
    }
    finally {
      system.terminate()
    }
  }
}
