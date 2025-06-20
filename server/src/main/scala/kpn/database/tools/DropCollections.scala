package kpn.database.tools

import kpn.database.base.Database
import kpn.database.index.Indexer

object DropCollections {
  def execute(database: Database): Unit = {
    database.baseNodes.drop()
    database.baseRoutes.drop()
    database.baseNetworks.drop()
    database.nodes.drop()
    database.routes.drop()
    database.networks.drop()

    database.changes.drop()
    database.nodeChanges.drop()
    database.networkChanges.drop()
    database.routeChanges.drop()
    database.routeTiles.drop()

    database.statistics.drop()
    database.status.drop()
    database.transactions.drop()

    new Indexer(database).createIndexes()
  }
}
