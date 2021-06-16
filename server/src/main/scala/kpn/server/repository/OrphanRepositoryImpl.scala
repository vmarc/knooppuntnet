package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.OrphanRouteInfo
import kpn.api.custom.Subset
import kpn.core.database.views.analyzer.OrphanNodeView
import kpn.core.database.views.analyzer.OrphanRouteView
import kpn.core.mongo.Database
import kpn.core.mongo.actions.subsets.MongoQuerySubsetOrphanNodes
import kpn.core.mongo.actions.subsets.MongoQuerySubsetOrphanRoutes
import org.springframework.stereotype.Component

@Component
class OrphanRepositoryImpl(
  database: Database,
  // old
  analysisDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends OrphanRepository {

  override def orphanRoutes(subset: Subset): Seq[OrphanRouteInfo] = {
    if (mongoEnabled) {
      new MongoQuerySubsetOrphanRoutes(database).execute(subset)
    }
    else {
      OrphanRouteView.query(analysisDatabase, subset)
    }
  }

  override def orphanNodes(subset: Subset): Seq[NodeInfo] = {
    if (mongoEnabled) {
      new MongoQuerySubsetOrphanNodes(database).execute(subset)
    }
    else {
      OrphanNodeView.query(analysisDatabase, subset)
    }
  }
}
