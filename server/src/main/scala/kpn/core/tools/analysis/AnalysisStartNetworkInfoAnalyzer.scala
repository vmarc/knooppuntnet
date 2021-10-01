package kpn.core.tools.analysis

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
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
        extraNodeDiffs = IdDiffs(),
        extraWayDiffs = IdDiffs(),
        extraRelationDiffs = IdDiffs(),
        happy = true,
        investigate = networkInfoDoc.facts.nonEmpty,
        impact = true
      )
    )
  }
}
