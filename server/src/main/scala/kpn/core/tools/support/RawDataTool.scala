package kpn.core.tools.support

import kpn.api.custom.Timestamp
import kpn.core.doc.RawNetworkDoc
import kpn.core.doc.RawNodeDoc
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.tools.support.RawDataTool.timestamp
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.overpass.OverpassRepositoryImpl
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RawDataRepositoryImpl

object RawDataTool {
  private val timestamp = Timestamp(2025, 1, 1, 0, 0, 0)

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-next") { database =>
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
    // loadNodes()
    // loadNetworks()
    loadRoutes()
  }

  private def loadNodes(): Unit = {
    val batchSize = 500
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

  private def loadNetworks(): Unit = {
    val batchSize = 25
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

  private def loadRoutes(): Unit = {
    log.info("Loading routeIds")
    val routeIds = repository.routeIds(timestamp)
    val routeCount = routeIds.size
    log.info(s"Loading $routeCount routes")
    log.infoElapsed {
      routeIds.zipWithIndex.foreach { case (routeId, index) =>
        log.infoElapsed {
          repository.route(timestamp, routeId) match {
            case Some(rawRoute) => database.rawRoutes.save(rawRoute)
            case None =>
          }
          (s"Loaded ${index + 1}/$routeCount routes", ())
        }
      }
      (s"Loaded $routeCount routes", ())
    }
  }
}
