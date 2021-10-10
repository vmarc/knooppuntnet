package kpn.core.tools.analysis

import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.ChangeType
import kpn.core.doc.NetworkInfoDoc
import kpn.core.util.Log

class AnalysisStartNetworkInfoAnalyzer(log: Log, config: AnalysisStartConfiguration) {

  def analyze(networkIds: Seq[Long]): Unit = {
    networkIds.foreach { networkId =>
      log.infoElapsed {
        config.networkInfoMasterAnalyzer.updateNetwork(config.timestamp, networkId) match {
          case Some(networkInfoDoc) => saveNetworkInfoChange(networkInfoDoc)
          case None =>
        }
        (s"network $networkId", ())
      }
    }
  }

  private def saveNetworkInfoChange(networkInfoDoc: NetworkInfoDoc): Unit = {

    val nodeRefs = networkInfoDoc.nodes.map(_.toRef)
    val routeRefs = networkInfoDoc.routes.map(_.toRef)
    val key = config.changeSetContext.buildChangeKey(networkInfoDoc._id)

    val extraNodeDiffs = IdDiffs(added = networkInfoDoc.extraNodeIds)
    val extraWayDiffs = IdDiffs(added = networkInfoDoc.extraWayIds)
    val extraRelationDiffs = IdDiffs(added = networkInfoDoc.extraRelationIds)

    val investigate = extraNodeDiffs.added.nonEmpty ||
      extraWayDiffs.added.nonEmpty ||
      extraRelationDiffs.added.nonEmpty

    config.changeSetRepository.saveNetworkInfoChange(
      NetworkInfoChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.InitialValue,
        networkInfoDoc.country,
        networkInfoDoc.summary.networkType,
        networkInfoDoc._id,
        networkInfoDoc.summary.name,
        orphanRouteDiffs = RefChanges(),
        orphanNodeDiffs = RefChanges(),
        networkDataUpdate = None,
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
    config.networkInfoRepository.updateNetworkChangeCount(networkInfoDoc._id)
  }
}
