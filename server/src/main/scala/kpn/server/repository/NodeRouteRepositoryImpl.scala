package kpn.server.repository

import kpn.api.common.NodeRoute
import kpn.api.common.common.NodeRouteCount
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.doc.NodeRouteDoc
import kpn.core.database.views.location.NodeRouteExpectedView
import kpn.core.database.views.location.NodeRouteReferenceView
import kpn.core.database.views.location.NodeRouteView
import kpn.core.db.KeyPrefix
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class NodeRouteRepositoryImpl(analysisDatabase: Database) extends NodeRouteRepository {

  private val log = Log(classOf[NodeRouteRepositoryImpl])

  override def save(nodeRoute: NodeRoute): Unit = {
    log.debugElapsed {
      analysisDatabase.save(NodeRouteDoc(docId(nodeRoute.id, nodeRoute.networkType), nodeRoute))
      (s"Save node-route ${nodeRoute.id}:${nodeRoute.networkType.name}", ())
    }
  }

  override def delete(nodeId: Long, networkType: NetworkType): Unit = {
    analysisDatabase.deleteDocWithId(docId(nodeId, networkType))
  }

  override def nodeRoutes(networkType: NetworkType): Seq[NodeRoute] = {
    NodeRouteView.query(analysisDatabase, networkType, stale = false)
  }

  override def actualNodeRouteCounts(networkType: NetworkType): Seq[NodeRouteCount] = {
    NodeRouteReferenceView.queryCount(analysisDatabase, networkType, stale = false)
  }

  override def expectedNodeRouteCounts(networkType: NetworkType): Seq[NodeRouteCount] = {
    NodeRouteExpectedView.query(analysisDatabase, networkType, stale = false)
  }

  private def docId(nodeId: Long, networkType: NetworkType): String = {
    s"${KeyPrefix.NodeRoute}:$nodeId:${networkType.name}"
  }
}
