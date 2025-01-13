package kpn.core.tools.analysis

import kpn.api.common.ChangeType
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.core.doc.NetworkDoc
import kpn.core.util.Log

class AnalysisStartNetworkInfoAnalyzer(log: Log, config: AnalysisStartConfiguration) {

  def analyze(networkIds: Seq[Long]): Unit = {
    networkIds.zipWithIndex.foreach { case (networkId, index) =>
      Log.context(s"${index + 1}/${networkIds.size}") {
        log.infoElapsed {
          config.networkRepository.findBaseNetworkById(networkId) match {
            case None =>
            case Some(baseNetworkDoc) =>
              config.networkMainAnalyzer.analyze(baseNetworkDoc, config.timestamp) match {
                case Some(networkDoc) => saveNetworkInfoChange(networkDoc)
                case None =>
              }
          }
          (s"network $networkId", ())
        }
      }
    }
  }

  private def saveNetworkInfoChange(networkDoc: NetworkDoc): Unit = {

    val nodeRefs = networkDoc.nodes.map(_.toRef)
    val routeRefs = networkDoc.routes.map(_.toRef)
    val key = config.changeSetContext.buildChangeKey(networkDoc._id)

    val extraNodeDiffs = IdDiffs(added = networkDoc.extraNodeIds)
    val extraWayDiffs = IdDiffs(added = networkDoc.extraWayIds)
    val extraRelationDiffs = IdDiffs(added = networkDoc.extraRelationIds)

    val investigate = extraNodeDiffs.added.nonEmpty ||
      extraWayDiffs.added.nonEmpty ||
      extraRelationDiffs.added.nonEmpty

    config.changeSetRepository.saveNetworkInfoChange(
      NetworkInfoChange(
        _id = key.toId,
        key = key,
        changeType = ChangeType.InitialValue,
        networkDoc.country,
        networkDoc.summary.routeType,
        networkDoc._id,
        networkDoc.summary.name,
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
    config.networkInfoRepository.updateNetworkChangeCount(networkDoc._id)
  }
}
