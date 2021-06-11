package kpn.core.mongo.migration

import kpn.api.common.NodeInfo
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.core.mongo.migration.MigrateNodesTool.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.repository.NodeRepositoryImpl
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MigrateNodesTool {

  private val log = Log(classOf[MigrateNodesTool])

  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    val mongoDatabase = Mongo.database(mongoClient, "kpn-test")
    Couch.executeIn("kpn-database", "analysis") { couchDatabase =>
      new MigrateNodesTool(couchDatabase, mongoDatabase).migrate()
    }
    mongoClient.close()
    log.info("Done")
  }
}

class MigrateNodesTool(couchDatabase: Database, mongoDatabase: MongoDatabase) {

  private val nodeRepository = new NodeRepositoryImpl(couchDatabase)
  private val nodesCollection = mongoDatabase.getCollection[NodeInfo]("nodes")

  def migrate(): Unit = {
    val allNodeIds = findAllNodeIds()
    val batchSize = 100
    allNodeIds.sliding(batchSize, batchSize).zipWithIndex.foreach { case (nodeIds, index) =>
      log.info(s"${index * batchSize}/${allNodeIds.size}")
      migrateNodes(nodeIds)
    }
  }

  private def findAllNodeIds(): Seq[Long] = {
    log.info("find nodeIds")
    log.elapsed {
      val ids = nodeRepository.allNodeIds()
      (s"${ids.size} nodes", ids)
    }
  }

  private def migrateNodes(nodeIds: Seq[Long]): Unit = {
    val nodeInfos = nodeRepository.nodesWithIds(nodeIds)
    val migratedNodeInfos = nodeInfos.map(nodeInfo => nodeInfo.copy(_id = nodeInfo.id))
    val future = nodesCollection.insertMany(migratedNodeInfos).toFuture()
    Await.result(future, Duration(1, TimeUnit.MINUTES))
  }
}
