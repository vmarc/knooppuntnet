package kpn.server.repository

import kpn.api.common.OrphanRouteInfo
import kpn.api.custom.Subset
import kpn.core.doc.NodeDoc
import kpn.database.actions.nodes.MongoQueryNodes
import kpn.database.actions.subsets.MongoQuerySubsetOrphanNodes
import kpn.database.actions.subsets.MongoQuerySubsetOrphanRoutes
import kpn.database.base.Database
import org.springframework.stereotype.Component

@Component
class OrphanRepositoryImpl(database: Database) extends OrphanRepository {

  override def orphanRoutes(subset: Subset): Seq[OrphanRouteInfo] = {
    throw new RuntimeException("not suppurted - use MongoQuerySubsetOrphanRoutes.execute instead, or move MongoQuerySubsetOrphanRoutes.execute here?")
  }

  override def orphanRouteIds(subset: Subset): Seq[Long] = {
    new MongoQuerySubsetOrphanRoutes(database).ids(subset)
  }

  override def orphanNodes(subset: Subset): Seq[NodeDoc] = {
    val orphanNodeIds = new MongoQuerySubsetOrphanNodes(database).ids(subset)
    new MongoQueryNodes(database).execute(orphanNodeIds)
  }
}
