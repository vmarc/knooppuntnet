package kpn.database.tools

import kpn.database.index.Indexer
import kpn.database.util.Mongo

object CreateIndexesTool {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-laptop") { database =>
      new Indexer(database).createIndexes()
    }
  }
}
