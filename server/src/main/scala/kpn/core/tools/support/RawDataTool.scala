package kpn.core.tools.support

import kpn.api.custom.Timestamp
import kpn.core.doc.RawNetworkDoc
import kpn.core.doc.RawNodeDoc
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.tools.support.RawDataTool.timestamp
import kpn.core.util.Log
import kpn.core.util.ThreadExecutor
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.overpass.OverpassRepositoryImpl
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RawDataRepositoryImpl

object RawDataTool {
  val timestamp: Timestamp = Timestamp(2025, 5, 20, 0, 0, 0)

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
      val overpassRepository = new OverpassRepositoryImpl(overpassQueryExecutor)
      val repository = new RawDataRepositoryImpl(overpassRepository)
      val tool = new RawDataTool(database, repository)
      tool.load()
    }
  }
}

class RawDataTool(database: Database, repository: RawDataRepository) {

  private val log = Log(classOf[RawDataTool])

  def load(): Unit = {
    loadNodes()
    loadNetworks()
    loadRoutes()
  }

  private def loadNodes(): Unit = {
    val batchSize = 500
    Log.context("load-nodes") {
      log.info("Loading nodeIds")
      val nodeIds = repository.nodeIds(timestamp)
      val nodeCount = nodeIds.size
      log.info(s"Loading $nodeCount nodes")
      log.infoElapsed {
        nodeIds.sliding(batchSize, batchSize).toSeq.zipWithIndex.foreach { case (nodeIdsBatch, index) =>
          log.infoElapsed {
            val rawNodes = repository.nodes(timestamp, nodeIdsBatch)
            val rawNodeDocs = rawNodes.map(node => RawNodeDoc(node.id, node))
            database.rawNodes.bulkSave(rawNodeDocs)
            (s"Loaded ${batchSize * (index + 1)}/$nodeCount nodes", ())
          }
        }
        (s"Loaded $nodeCount nodes", ())
      }
    }
  }

  private def loadNetworks(): Unit = {
    val batchSize = 25
    Log.context("load-networks") {
      log.info("Loading networkIds")
      val networkIds = repository.networkIds(timestamp)
      val networkCount = networkIds.size
      log.info(s"Loading $networkCount networks")
      log.infoElapsed {
        networkIds.sliding(batchSize, batchSize).toSeq.zipWithIndex.foreach { case (networkIdsBatch, index) =>
          log.infoElapsed {
            val rawNetworks = repository.networks(timestamp, networkIdsBatch)
            val rawNetworkDocs = rawNetworks.map(node => RawNetworkDoc(node.id, node))
            database.rawNetworks.bulkSave(rawNetworkDocs)
            (s"Loaded ${batchSize * (index + 1)}/$networkCount networks", ())
          }
        }
        (s"Loaded $networkCount networks", ())
      }
    }
  }

  private def loadRoutes(): Unit = {
    Log.context("load-routes") {
      log.info("Loading routeIds")
      val overpassRouteIds = repository.routeIds(timestamp)
      val dbRouteIds = database.rawRoutes.ids()
      val routeIds = (overpassRouteIds.toSet -- dbRouteIds).toSeq.sorted
      val routeCount = routeIds.size
      log.info(s"Loading $routeCount routes")
      val context = Log.contextMessages
      log.infoElapsed {
        ThreadExecutor.execute(25, routeIds) { (index, count, routeId) =>
          Log.context(context) {
            Log.context(s"$index/$count $routeId") {
              log.infoElapsed {
                repository.route(timestamp, routeId) match {
                  case Some(rawRoute) => database.rawRoutes.save(rawRoute)
                  case None =>
                }
                (s"Loaded route $routeId", ())
              }
            }
          }
        }
        (s"Loaded $routeCount routes", ())
      }
    }
  }
}
