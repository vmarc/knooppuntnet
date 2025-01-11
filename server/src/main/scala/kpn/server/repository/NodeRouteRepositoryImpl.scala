package kpn.server.repository

import kpn.api.common.common.NodeRouteRefs
import kpn.api.common.common.Reference
import kpn.api.custom.ScopedRouteType
import kpn.database.actions.nodes.MongoQueryNodeRouteReferences
import kpn.database.base.Database
import org.springframework.stereotype.Component

@Component
class NodeRouteRepositoryImpl(database: Database) extends NodeRouteRepository {

  override def nodesRouteReferences(scopedRouteType: ScopedRouteType, nodeIds: Seq[Long]): Seq[NodeRouteRefs] = {
    val nodeRouteRefs = new MongoQueryNodeRouteReferences(database).execute(nodeIds)
    nodeIds.map { nodeId =>
      val references = nodeRouteRefs.filter(nodeRouteRef => nodeRouteRef.nodeId == nodeId &&
        nodeRouteRef.routeType == scopedRouteType.routeType &&
        nodeRouteRef.routeScope == scopedRouteType.routeScope
      ).map { nodeRouteRef =>
        Reference(
          nodeRouteRef.routeType,
          nodeRouteRef.routeScope,
          nodeRouteRef.routeId,
          nodeRouteRef.routeName
        )
      }.sortBy(_.name)
      NodeRouteRefs(nodeId, references)
    }
  }
}
