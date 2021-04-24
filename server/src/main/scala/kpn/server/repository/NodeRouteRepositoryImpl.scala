package kpn.server.repository

import kpn.api.common.NodeRoute
import kpn.api.common.common.NodeRouteCount
import kpn.api.common.common.NodeRouteExpectedCount
import kpn.api.common.common.NodeRouteRefs
import kpn.api.common.common.Ref
import kpn.api.custom.ScopedNetworkType
import kpn.core.database.Database
import kpn.core.database.doc.NodeRouteDoc
import kpn.core.database.views.node.NodeRouteExpectedView
import kpn.core.database.views.node.NodeRouteReferenceView
import kpn.core.database.views.node.NodeRouteView
import kpn.core.db.KeyPrefix
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class NodeRouteRepositoryImpl(analysisDatabase: Database) extends NodeRouteRepository {

  private val log = Log(classOf[NodeRouteRepositoryImpl])

  override def save(nodeRoute: NodeRoute): Unit = {
    log.debugElapsed {
      analysisDatabase.save(NodeRouteDoc(docId(nodeRoute.id, nodeRoute.scopedNetworkType), nodeRoute))
      (s"Save node-route ${nodeRoute.id}:${nodeRoute.scopedNetworkType.key}", ())
    }
  }

  override def delete(nodeId: Long, scopedNetworkType: ScopedNetworkType): Unit = {
    analysisDatabase.deleteDocWithId(docId(nodeId, scopedNetworkType))
  }

  override def nodeRoutes(scopedNetworkType: ScopedNetworkType): Seq[NodeRoute] = {
    NodeRouteView.query(analysisDatabase, scopedNetworkType, stale = false)
  }

  override def nodeRouteReferences(scopedNetworkType: ScopedNetworkType, nodeId: Long): Seq[Ref] = {
    NodeRouteReferenceView.query(analysisDatabase, scopedNetworkType, nodeId, stale = true)
  }

  override def nodesRouteReferences(scopedNetworkType: ScopedNetworkType, nodeIds: Seq[Long]): Seq[NodeRouteRefs] = {
    NodeRouteReferenceView.queryNodeIds(analysisDatabase, scopedNetworkType, nodeIds, stale = true)
  }

  override def actualNodeRouteCounts(scopedNetworkType: ScopedNetworkType): Seq[NodeRouteCount] = {
    NodeRouteReferenceView.queryCount(analysisDatabase, scopedNetworkType, stale = false)
  }

  override def expectedNodeRouteCounts(scopedNetworkType: ScopedNetworkType): Seq[NodeRouteExpectedCount] = {
    NodeRouteExpectedView.queryScopedNetworkType(analysisDatabase, scopedNetworkType, stale = false)
  }

  private def docId(nodeId: Long, scopedNetworkType: ScopedNetworkType): String = {
    s"${KeyPrefix.NodeRoute}:$nodeId:${scopedNetworkType.key}"
  }
}
