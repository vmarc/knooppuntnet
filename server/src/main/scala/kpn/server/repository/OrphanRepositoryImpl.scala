package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.OrphanRouteInfo
import kpn.api.custom.Subset
import kpn.core.database.views.analyzer.OrphanNodeView
import kpn.core.database.views.analyzer.OrphanRouteView
import kpn.core.mongo.Database
import kpn.core.mongo.actions.nodes.MongoQueryNodes
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
      throw new RuntimeException("not suppurted - use MongoQuerySubsetOrphanRoutes.execute instead")
    }
    else {
      OrphanRouteView.query(analysisDatabase, subset)
    }
  }

  override def orphanRouteIds(subset: Subset): Seq[Long] = {
    if (mongoEnabled) {
      new MongoQuerySubsetOrphanRoutes(database).ids(subset)
    }
    else {
      OrphanRouteView.query(analysisDatabase, subset).map(_.id)
    }
  }

  override def orphanNodes(subset: Subset): Seq[NodeInfo] = {
    if (mongoEnabled) {
      val orphanNodeIds = new MongoQuerySubsetOrphanNodes(database).ids(subset)
      new MongoQueryNodes(database).execute(orphanNodeIds)

    }
    else {
      OrphanNodeView.query(analysisDatabase, subset)
    }
  }
}
