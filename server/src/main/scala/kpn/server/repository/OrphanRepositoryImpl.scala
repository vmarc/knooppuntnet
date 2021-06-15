package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.OrphanRouteInfo
import kpn.api.custom.Subset
import kpn.core.database.Database
import kpn.core.database.views.analyzer.OrphanNodeView
import kpn.core.database.views.analyzer.OrphanRouteView
import kpn.core.mongo.changes.MongoQuerySubsetOrphanNodes
import kpn.core.mongo.changes.MongoQuerySubsetOrphanRoutes
import org.mongodb.scala.MongoDatabase
import org.springframework.stereotype.Component

@Component
class OrphanRepositoryImpl(
  // old
  analysisDatabase: Database,
  // new
  mongoEnabled: Boolean,
  mongoDatabase: MongoDatabase
) extends OrphanRepository {

  override def orphanRoutes(subset: Subset): Seq[OrphanRouteInfo] = {
    if (mongoEnabled) {
      new MongoQuerySubsetOrphanRoutes(mongoDatabase).execute(subset)
    }
    else {
      OrphanRouteView.query(analysisDatabase, subset)
    }
  }

  override def orphanNodes(subset: Subset): Seq[NodeInfo] = {
    if (mongoEnabled) {
      new MongoQuerySubsetOrphanNodes(mongoDatabase).execute(subset)
    }
    else {
      OrphanNodeView.query(analysisDatabase, subset)
    }
  }
}
