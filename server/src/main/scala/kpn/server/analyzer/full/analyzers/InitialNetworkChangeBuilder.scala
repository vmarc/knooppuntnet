package kpn.server.analyzer.full.analyzers

import kpn.api.common.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.core.doc.NetworkDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkInfoRepository
import org.springframework.stereotype.Component

@Component
class InitialNetworkChangeBuilder(
  changeSetRepository: ChangeSetRepository,
  networkInfoRepository: NetworkInfoRepository,
) {

  def saveNetworkChange(changeSetContext: ChangeSetContext, networkDoc: NetworkDoc): Unit = {

    val nodeRefs = networkDoc.nodes.map(_.toRef)
    val routeRefs = networkDoc.routes.map(_.toRef)
    val key = changeSetContext.buildChangeKey(networkDoc._id)

    val extraNodeDiffs = IdDiffs(added = networkDoc.extraNodeIds)
    val extraWayDiffs = IdDiffs(added = networkDoc.extraWayIds)
    val extraRelationDiffs = IdDiffs(added = networkDoc.extraRelationIds)

    val investigate = extraNodeDiffs.added.nonEmpty ||
      extraWayDiffs.added.nonEmpty ||
      extraRelationDiffs.added.nonEmpty

    changeSetRepository.saveNetworkChange(
      NetworkChange(
        _id = key.toId,
        key = key,
        networkId = networkDoc._id,
        networkName = networkDoc.base.name,
        changeType = ChangeType.InitialValue,
        country = networkDoc.country,
        routeType = networkDoc.base.routeType,
        networkDataUpdate = None,
        nodes = IdDiffs(),
        ways = IdDiffs(),
        relations = IdDiffs(),
        nodeDiffs = RefDiffs(added = nodeRefs),
        routeDiffs = RefDiffs(added = routeRefs),
        extraNodeDiffs = extraNodeDiffs,
        extraWayDiffs = extraWayDiffs,
        extraRelationDiffs = extraRelationDiffs,
        happy = false,
        investigate = investigate,
        impact = true
      )
    )
  }
}


