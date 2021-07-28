package kpn.core.mongo.migration

import kpn.core.mongo.Database
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log

object MigrateNetworkDocsTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      new MigrateNetworkDocsTool(database).migrate()
    }
  }
}

class MigrateNetworkDocsTool(database: Database) {

  private val log = Log(classOf[MigrateNetworksTool])

  def migrate(): Unit = {
    val networkIds = database.oldNetworks.ids()
    networkIds.zipWithIndex.foreach { case (networkId, index) =>
      log.info(s"${index + 1}/${networkIds.size}")
      database.oldNetworks.findById(networkId) match {
        case Some(networkInfo) => database.networks.save(networkInfo.toNetworkDoc)
        case None =>
      }
    }
    log.info("Done")
  }
}
