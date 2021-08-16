package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.changes.details.NetworkInfoChange
import kpn.core.mongo.Database
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.server.analyzer.engine.analysis.network.info.NetworkInfoMasterAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import org.springframework.stereotype.Component

@Component
class NetworkInfoChangeProcessorImpl(
  database: Database,
  networkInfoImpactAnalyzer: NetworkInfoImpactAnalyzer,
  networkInfoMasterAnalyzer: NetworkInfoMasterAnalyzer
) extends NetworkInfoChangeProcessor {

  def analyze(changeSetContext: ChangeSetContext): ChangeSetContext = {

    val impactedNetworkIds = networkInfoImpactAnalyzer.analyze(changeSetContext)

    val networkChanges = impactedNetworkIds.flatMap { networkId =>
      val beforeOption = database.networkInfos.findById(networkId)
      val previousKnownCountry = beforeOption.flatMap(_.country)
      val afterOption = networkInfoMasterAnalyzer.updateNetwork(
        changeSetContext.timestampAfter,
        networkId,
        previousKnownCountry
      )

      beforeOption match {
        case None =>
          afterOption match {
            case Some(after) => processCreate(changeSetContext, after, networkId)
            case None => None // TODO message ?
          }
        case Some(before) =>
          afterOption match {
            case None => processDelete(changeSetContext, before, networkId)
            case Some(after) => processUpdate(changeSetContext, before, after, networkId)
          }
      }
    }

    changeSetContext.copy(
      changes = changeSetContext.changes.copy(
        networkInfoChanges = networkChanges
      )
    )
  }

  private def processCreate(context: ChangeSetContext, after: NetworkInfoDoc, networkId: Long): Option[NetworkInfoChange] = {
    Some(new NetworkInfoCreateAnalyzer(context, after, networkId).analyze())
  }

  private def processDelete(context: ChangeSetContext, before: NetworkInfoDoc, networkId: Long): Option[NetworkInfoChange] = {
    Some(new NetworkInfoDeleteAnalyzer(context, before, networkId).analyze())
  }

  private def processUpdate(context: ChangeSetContext, before: NetworkInfoDoc, after: NetworkInfoDoc, networkId: Long): Option[NetworkInfoChange] = {
    if (!after.active) {
      processDelete(context, before, networkId)
    }
    else {
      if (before == after) {
        None
      }
      else {
        Some(
          new NetworkInfoUpdateAnalyzer(context, before, after, networkId).analyze()
        )
      }
    }
  }
}
