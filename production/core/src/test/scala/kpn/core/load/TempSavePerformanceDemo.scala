package kpn.core.load

import java.io.File

import kpn.core.analysis.Network
import kpn.core.db.couch.Couch
import kpn.core.engine.analysis.NetworkAnalyzerImpl
import kpn.core.engine.analysis.NetworkRelationAnalyzerImpl
import kpn.core.engine.analysis.country.CountryAnalyzerImpl
import kpn.core.engine.analysis.route.MasterRouteAnalyzerImpl
import kpn.core.load.data.LoadedNetwork
import kpn.core.overpass.CachingOverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.repository._
import kpn.core.util.Log
import kpn.shared.Timestamp

class TempSavePerformanceDemo()

object TempSavePerformanceDemo {

  def main(args: Array[String]): Unit = {

    val log = Log(classOf[TempSavePerformanceDemo])

    Couch.executeIn("master4") { database =>
      database.delete("analysis-data")
    }

    System.exit(0)

    Couch.executeIn("test2") { database =>
      val networkId = 226721L
      // TODO also try 1059986   / 1341166
      val timestamp = Timestamp(2015, 12, 20, 0, 0, 0)
      val cacheDir = new File("/mnt/ssd/kpn/cache")

      val nonCachingExecutor = new OverpassQueryExecutorImpl()
      val cachingExecutor = new CachingOverpassQueryExecutor(cacheDir, nonCachingExecutor)

      val countryAnalyzer = new CountryAnalyzerImpl()

      val analysisRepository: AnalysisRepository = {

        val networkRepository = new NetworkRepositoryImpl(database)
        val routeRepository = new RouteRepositoryImpl(database)
        val nodeRepository = new NodeRepositoryImpl(database)

        new AnalysisRepositoryImpl(
          database,
          networkRepository,
          routeRepository,
          nodeRepository
        )
      }

      val networkLoader: NetworkLoader = new NetworkLoaderImpl(cachingExecutor)
      val routeAnalyzer = new MasterRouteAnalyzerImpl()
      val networkRelationAnalyzer = new NetworkRelationAnalyzerImpl(countryAnalyzer)
      val networkAnalyzer = new NetworkAnalyzerImpl(countryAnalyzer, routeAnalyzer)

      val loadedNetwork: LoadedNetwork = log.elapsed {
        ("Load network", networkLoader.load(Some(timestamp), networkId).get)
      }

      log.elapsed {

        val network: Network = log.elapsed {
          val networkRelationAnalysis = networkRelationAnalyzer.analyze(loadedNetwork.relation)
          ("Build network", networkAnalyzer.analyze(networkRelationAnalysis, loadedNetwork.data, loadedNetwork.networkId))
        }

        log.unitElapsed {
          analysisRepository.saveNetwork(network)
          "save network"
        }

        ("total", ())
      }
    }
  }
}
