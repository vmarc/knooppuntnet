package kpn.server.analyzer.engine.changes.network.info

import kpn.core.mongo.Database
import kpn.core.mongo.actions.nodes.MongoQueryNodeNetworkReferences
import kpn.core.mongo.actions.routes.MongoQueryRouteNetworkReferences
import kpn.server.analyzer.engine.changes.ChangeSetContext
import org.springframework.stereotype.Component

@Component
class NetworkInfoImpactAnalyzer(database: Database) {

  def analyze(context: ChangeSetContext): Seq[Long] = {
    val impactedNetworkIds1 = context.changes.networkChanges.map(_.networkId)
    val impactedRouteIds = context.changes.routeChanges.map(_.id)
    val impactedNetworkIds2 = new MongoQueryRouteNetworkReferences(database).executeRouteIds(impactedRouteIds)
    val impactedNodeIds1 = context.changes.networkChanges.flatMap(_.impactedNodeIds)
    val impactedNodeIds2 = context.changes.routeChanges.flatMap(_.impactedNodeIds)
    val impactedNodeIds3 = context.changes.nodeChanges.map(_.id)
    val impactedNodeIds = (impactedNodeIds1 ++ impactedNodeIds2 ++ impactedNodeIds3).distinct.sorted
    val impactedNetworkIds3 = new MongoQueryNodeNetworkReferences(database).executeNodeIds(impactedNodeIds)
    (impactedNetworkIds1 ++ impactedNetworkIds2 ++ impactedNetworkIds3).distinct.sorted
  }
}