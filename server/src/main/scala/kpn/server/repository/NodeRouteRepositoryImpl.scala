package kpn.server.repository

import kpn.api.common.NodeRoute
import kpn.api.common.common.NodeRouteCount
import kpn.api.common.common.NodeRouteExpectedCount
import kpn.api.common.common.NodeRouteRefs
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.custom.NetworkScope
import kpn.api.custom.ScopedNetworkType
import kpn.core.database.doc.NodeRouteDoc
import kpn.core.database.views.node.NodeRouteExpectedView
import kpn.core.database.views.node.NodeRouteReferenceView
import kpn.core.database.views.node.NodeRouteView
import kpn.core.db.KeyPrefix
import kpn.core.mongo.Database
import kpn.core.mongo.actions.nodes.MongoQueryNodeRouteReferences
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class NodeRouteRepositoryImpl(
  database: Database,
  // old
  analysisDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends NodeRouteRepository {

  private val log = Log(classOf[NodeRouteRepositoryImpl])

  override def save(nodeRoute: NodeRoute): Unit = {
    if (mongoEnabled) {
      ???
    }
    else {
      log.debugElapsed {
        analysisDatabase.save(NodeRouteDoc(docId(nodeRoute.id, nodeRoute.scopedNetworkType), nodeRoute))
        (s"Save node-route ${nodeRoute.id}:${nodeRoute.scopedNetworkType.key}", ())
      }
    }
  }

  override def delete(nodeId: Long, scopedNetworkType: ScopedNetworkType): Unit = {
    if (mongoEnabled) {
      ???
    }
    else {
      analysisDatabase.deleteDocWithId(docId(nodeId, scopedNetworkType))
    }
  }

  override def nodeRoutes(scopedNetworkType: ScopedNetworkType): Seq[NodeRoute] = {
    if (mongoEnabled) {
      ???
    }
    else {
      NodeRouteView.query(analysisDatabase, scopedNetworkType, stale = false)
    }
  }

  override def nodeRouteReferences(scopedNetworkType: ScopedNetworkType, nodeId: Long): Seq[Ref] = {
    if (mongoEnabled) {
      ???
    }
    else {
      NodeRouteReferenceView.query(analysisDatabase, scopedNetworkType, nodeId, stale = true)
    }
  }

  override def nodesRouteReferences(scopedNetworkType: ScopedNetworkType, nodeIds: Seq[Long]): Seq[NodeRouteRefs] = {
    if (mongoEnabled) {
      val nodeRouteRefs = new MongoQueryNodeRouteReferences(database).execute(nodeIds)

      nodeIds.map { nodeId =>
        val references = nodeRouteRefs.filter(_.nodeId == nodeId).map { nodeRouteRef =>
          Reference(
            nodeRouteRef.networkType,
            NetworkScope.regional, // TODO MONGO pick up the real NetworkScope from the route collection
            nodeRouteRef.routeId,
            nodeRouteRef.routeName
          )
        }.sortBy(_.name)
        NodeRouteRefs(nodeId, references)
      }
    }
    else {
      NodeRouteReferenceView.queryNodeIds(analysisDatabase, scopedNetworkType, nodeIds, stale = true)
    }
  }

  override def actualNodeRouteCounts(scopedNetworkType: ScopedNetworkType): Seq[NodeRouteCount] = {
    if (mongoEnabled) {
      ???
    }
    else {
      NodeRouteReferenceView.queryCount(analysisDatabase, scopedNetworkType, stale = false)
    }
  }

  override def expectedNodeRouteCounts(scopedNetworkType: ScopedNetworkType): Seq[NodeRouteExpectedCount] = {
    if (mongoEnabled) {
      ???
    }
    else {
      NodeRouteExpectedView.queryScopedNetworkType(analysisDatabase, scopedNetworkType, stale = false)
    }
  }

  private def docId(nodeId: Long, scopedNetworkType: ScopedNetworkType): String = {
    s"${KeyPrefix.NodeRoute}:$nodeId:${scopedNetworkType.key}"
  }
}
