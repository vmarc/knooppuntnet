package kpn.server.repository

import kpn.api.common.common.NodeRouteRefs
import kpn.api.common.common.Reference
import kpn.api.custom.NetworkScope
import kpn.api.custom.ScopedNetworkType
import kpn.core.mongo.Database
import kpn.core.mongo.actions.nodes.MongoQueryNodeRouteReferences
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class NodeRouteRepositoryImpl(database: Database) extends NodeRouteRepository {

  private val log = Log(classOf[NodeRouteRepositoryImpl])

  override def nodesRouteReferences(scopedNetworkType: ScopedNetworkType, nodeIds: Seq[Long]): Seq[NodeRouteRefs] = {
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
}
