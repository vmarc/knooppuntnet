package kpn.server.analyzer.engine.changes.route.main

import kpn.api.common.ChangeType
import kpn.api.common.ElementChangeType
import kpn.api.common.Fact
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.route.RouteNodeChange
import kpn.core.doc.RouteDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext
import org.springframework.stereotype.Component

@Component
class RouteChangeDeleteProcessor {

  def process(context: ChangeSetContext, routeDoc: RouteDoc): Option[RouteChangeContext] = {

    val impactedNodeIds: Seq[Long] = routeDoc.nodes.nodeIds.sorted

    val removedFromNetwork = routeDoc.networkReferences.map(_.toRef)
    val impactedNetworkIds = removedFromNetwork.map(_.id)

    val beforeRouteData = RouteData.from(routeDoc)

    val nodeChanges = routeDoc.nodes.nodes.map { node =>
      RouteNodeChange(
        node.nodeId,
        node.latitude,
        node.longitude,
        ElementChangeType.Removed
      )
    }

    val key = context.buildChangeKey(routeDoc._id)

    Some(
      RouteChangeContext(
        RouteChangeStateAnalyzer.analyzed(
          RouteChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Delete,
            name = routeDoc.summary.name,
            locationAnalysis = routeDoc.locationAnalysis,
            addedToNetwork = Seq.empty,
            removedFromNetwork = removedFromNetwork,
            before = Some(beforeRouteData),
            after = None,
            diffs = RouteDiff(),
            nodeChanges = nodeChanges,
            facts = Seq(Fact.Deleted),
          )
        ),
        impactedNodeIds = impactedNodeIds,
        impactedNetworkIds = impactedNetworkIds
      )
    )
  }
}
