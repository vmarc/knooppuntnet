package kpn.server.analyzer.engine.analysis.post

import kpn.database.util.Mongo

object StatisticsUpdaterDemo {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-experimental") { database =>
      new StatisticsUpdater(database).update()
    }
  }
}
