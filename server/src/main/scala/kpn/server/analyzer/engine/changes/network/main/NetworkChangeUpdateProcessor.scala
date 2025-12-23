package kpn.server.analyzer.engine.changes.network.main

import kpn.api.common.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.core.doc.NetworkDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext

class NetworkChangeUpdateProcessor(
  context: ChangeSetContext,
  before: NetworkDoc,
  after: NetworkDoc,
  networkId: Long
) {

  def process(): Option[NetworkChange] = {

    val networkDataBefore = NetworkData(
      before.base.raw.meta,
      before.base.name
    )

    val networkDataAfter = NetworkData(
      after.base.raw.meta,
      after.base.name
    )

    val networkDataUpdate = Option.when(networkDataBefore != networkDataAfter) {
      NetworkDataUpdate(
        Some(networkDataBefore),
        Some(networkDataAfter)
      )
    }

    val diffs = NetworkInfoDiffs(
      nodeDiffs = NetworkUpdateNodeDiffsAnalyzer.analyze(context, before, after),
      routeDiffs = NetworkUpdateRouteDiffsAnalyzer.analyze(context, before, after),
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
          after.base.name,
          ChangeType.Update,
          after.country,
          after.base.routeType,
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
