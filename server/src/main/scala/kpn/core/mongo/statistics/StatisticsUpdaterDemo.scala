package kpn.core.mongo.statistics

import kpn.core.mongo.util.Mongo

object StatisticsUpdaterDemo {
  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    try {
      val database = Mongo.database(mongoClient, "tryout")
      val updater = new StatisticsUpdater(database)
      updater.execute()
    }
    finally {
      mongoClient.close()
    }
  }
}
