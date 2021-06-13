package kpn.core.mongo.statistics

import kpn.core.mongo.util.Mongo

object StatisticsUpdaterDemo {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      new StatisticsUpdater(database).execute()
    }
  }
}
