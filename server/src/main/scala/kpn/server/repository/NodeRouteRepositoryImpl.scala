package kpn.server.repository

import kpn.api.common.common.NodeRouteRefs
import kpn.api.common.common.Reference
import kpn.api.custom.ScopedNetworkType
import kpn.database.actions.nodes.MongoQueryNodeRouteReferences
import kpn.database.base.Database
import org.springframework.stereotype.Component

@Component
class NodeRouteRepositoryImpl(database: Database) extends NodeRouteRepository {

  override def nodesRouteReferences(scopedNetworkType: ScopedNetworkType, nodeIds: Seq[Long]): Seq[NodeRouteRefs] = {
    val nodeRouteRefs = new MongoQueryNodeRouteReferences(database).execute(nodeIds)
    nodeIds.map { nodeId =>
      val references = nodeRouteRefs.filter(nodeRouteRef => nodeRouteRef.nodeId == nodeId &&
        nodeRouteRef.networkType == scopedNetworkType.networkType &&
        nodeRouteRef.networkScope == scopedNetworkType.networkScope
      ).map { nodeRouteRef =>
        Reference(
          nodeRouteRef.networkType,
          nodeRouteRef.networkScope,
          nodeRouteRef.routeId,
          nodeRouteRef.routeName
        )
      }.sortBy(_.name)
      NodeRouteRefs(nodeId, references)
    }
  }
}
