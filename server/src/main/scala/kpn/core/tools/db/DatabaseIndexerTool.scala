package kpn.core.tools.db

import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.DatabaseIndexer

object DatabaseIndexerTool {

  def main(args: Array[String]): Unit = {

    if (args.length < 3) {
      println("Usage: DatabaseIndexerTool host analysisDatabase changesDatabase poisDatabase backendDatabase frontendDatabase")
      System.exit(-1)
    }
    val host = args(0)
    val analysisDatabaseName = args(1)
    val changesDatabaseName = args(2)
    val poisDatabaseName = args(3)
    val backendDatabaseName = args(4)
    val frontendDatabaseName = args(5)

    Couch.executeIn(host, analysisDatabaseName) { analysisDatabase =>
      Couch.executeIn(host, changesDatabaseName) { changeDatabase =>
        Couch.executeIn(host, poisDatabaseName) { poiDatabase =>
          Couch.executeIn(host, backendDatabaseName) { backendActionsDatabase =>
            Couch.executeIn(host, frontendDatabaseName) { frontendActionsDatabase =>
              val indexer = new DatabaseIndexer(
                analysisDatabase,
                changeDatabase,
                poiDatabase,
                backendActionsDatabase,
                frontendActionsDatabase
              )
              indexer.firstIndexing()
            }
          }
        }
      }
    }
  }
}
