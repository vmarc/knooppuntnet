package kpn.database.tools

import kpn.database.util.Mongo

object DropCollectionsTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      DropCollections.execute(database)
    }
  }
}
