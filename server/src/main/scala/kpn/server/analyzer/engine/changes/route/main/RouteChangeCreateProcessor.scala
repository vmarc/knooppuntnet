package kpn.server.analyzer.engine.changes.route.main

import kpn.api.common.ChangeType
import kpn.api.common.ElementChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.diff.RouteData
import kpn.api.common.route.RouteNodeChange
import kpn.core.doc.RouteDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class RouteChangeCreateProcessor {

  def process(context: ChangeSetContext, routeDocAfter: RouteDoc, routeId: Long): Option[RouteChangeContext] = {

    val impactedNodeIds: Seq[Long] = routeDocAfter.base.nodes.nodeIds

    val key = context.buildChangeKey(routeId)

    val addedToNetwork = routeDocAfter.networkReferences.map(_.toRef)
    val impactedNetworkIds = addedToNetwork.map(_.id)

    val nodeChanges = routeDocAfter.base.nodes.nodes.map { node =>
      RouteNodeChange(
        node.nodeId,
        node.latitude,
        node.longitude,
        ElementChangeType.Added
      )
    }

    Some(
      RouteChangeContext(
        RouteChangeStateAnalyzer.analyzed(
          RouteChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Create,
            name = routeDocAfter.base.name,
            locationAnalysis = routeDocAfter.base.locationAnalysis,
            addedToNetwork = addedToNetwork,
            removedFromNetwork = Seq.empty,
            before = None,
            after = Some(RouteData.from(routeDocAfter)),
            nodeChanges = nodeChanges,
            facts = Seq.empty,
          )
        ),
        impactedNodeIds = impactedNodeIds,
        impactedNetworkIds = impactedNetworkIds
      )
    )
  }
}
