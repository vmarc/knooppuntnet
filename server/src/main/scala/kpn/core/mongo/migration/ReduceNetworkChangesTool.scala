package kpn.core.mongo.migration

import kpn.core.mongo.Database
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log

object ReduceNetworkChangesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { mongoDatabase =>
      new ReduceNetworkChangesTool(mongoDatabase).migrate()
    }
  }
}

class ReduceNetworkChangesTool(database: Database) {

  private val log = Log(classOf[ReduceNetworkChangesTool])

  def migrate(): Unit = {
    log.infoElapsed {
      val ids = database.networkChanges.stringIds()
      log.info(s"ids.size=${ids.size}")
      ids.zipWithIndex.foreach { case (id, index) =>
        Log.context(s"${index + 1}/${ids.size}") {
          log.infoElapsed {
            database.networkChanges.findByStringId(id) match {
              case None =>
              case Some(networkChange) =>
                val migratedNetworkDataUpdate = networkChange.networkDataUpdate.map { networkDataUpdate =>
                  networkDataUpdate.copy(
                    before = networkDataUpdate.before.migrated,
                    after = networkDataUpdate.after.migrated
                  )
                }
                val migratedNetworkChange = networkChange.copy(networkDataUpdate = migratedNetworkDataUpdate)
                database.networkChanges.save(migratedNetworkChange)
            }
            (id, ())
          }
        }
      }
      ("all migrated", ())
    }
  }
}
