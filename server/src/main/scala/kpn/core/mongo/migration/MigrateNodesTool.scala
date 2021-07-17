package kpn.core.mongo.migration

import kpn.core.db.couch.Couch
import kpn.core.mongo.Database
import kpn.core.mongo.migration.MigrateNodesTool.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import kpn.server.repository.NodeRepositoryImpl

/*
  Note: should run AFTER route migration (so that route references can be picked up from the new database)
 */
object MigrateNodesTool {

  private val log = Log(classOf[MigrateNodesTool])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      Couch.executeIn("kpn-database", "analysis") { couchDatabase =>
        new MigrateNodesTool(couchDatabase, database).migrate()
      }
    }
    log.info("Done")
  }
}

class MigrateNodesTool(couchDatabase: kpn.core.database.Database, database: Database) {

  private val nodeRepository = new NodeRepositoryImpl(null, couchDatabase, false)

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
    database.nodes.insertMany(nodeInfos)
  }
}
