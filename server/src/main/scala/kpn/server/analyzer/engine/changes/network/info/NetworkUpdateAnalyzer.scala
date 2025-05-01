package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.core.doc.NetworkDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.network.NetworkDiffAnalyzer

class NetworkUpdateAnalyzer(
  context: ChangeSetContext,
  before: NetworkDoc,
  after: NetworkDoc,
  networkId: Long
) {

  def analyze(): Option[NetworkChange] = {

    val networkDataBefore = NetworkData(
      before.detail.toMeta,
      before.summary.name
    )

    val networkDataAfter = NetworkData(
      after.detail.toMeta,
      after.summary.name
    )

    val networkDataUpdate = if (networkDataBefore != networkDataAfter) {
      Some(
        NetworkDataUpdate(
          Some(networkDataBefore),
          Some(networkDataAfter)
        )
      )
    }
    else {
      None
    }

    val diffs = NetworkInfoDiffs(
      nodeDiffs = NetworkInfoUpdateNodeDiffsAnalyzer.analyze(context, before, after),
      routeDiffs = NetworkInfoUpdateRouteDiffsAnalyzer.analyze(context, before, after),
      extraNodeDiffs = IdDiffsAnalyzer.analyze(before.extraNodeIds, after.extraNodeIds),
      extraWayDiffs = IdDiffsAnalyzer.analyze(before.extraWayIds, after.extraWayIds),
      extraRelationDiffs = IdDiffsAnalyzer.analyze(before.extraRelationIds, after.extraRelationIds)
    )
    val relationDiffAnalyzer = new NetworkDiffAnalyzer(before, after)

    val happy = diffs.happy
    val investigate = diffs.investigate
    val impact = happy || investigate

    if (networkDataUpdate.nonEmpty || diffs.nonEmpty) {
      val key = context.buildChangeKey(networkId)
      Some(
        NetworkChange(
          key.toId,
          key,
          networkId,
          after.summary.name,
          ChangeType.Update,
          after.country,
          after.summary.routeType,
          networkDataUpdate,
          relationDiffAnalyzer.nodeDiffs,
          relationDiffAnalyzer.wayDiffs,
          relationDiffAnalyzer.relationDiffs,
          diffs.nodeDiffs,
          diffs.routeDiffs,
          diffs.extraNodeDiffs,
          diffs.extraWayDiffs,
          diffs.extraRelationDiffs,
          happy,
          investigate,
          impact
        )
      )
    }
    else {
      None
    }
  }
}
