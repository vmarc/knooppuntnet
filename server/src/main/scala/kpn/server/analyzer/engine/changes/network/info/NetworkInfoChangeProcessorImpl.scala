package kpn.server.analyzer.engine.changes.network.info

import kpn.api.common.changes.details.NetworkInfoChange
import kpn.core.doc.NetworkDoc
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.network.main.NetworkMainAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkInfoChangeProcessorImpl(
  database: Database,
  networkRepository: NetworkRepository,
  networkInfoImpactAnalyzer: NetworkInfoImpactAnalyzer,
  networkMainAnalyzer: NetworkMainAnalyzer
) extends NetworkInfoChangeProcessor {

  def analyze(changeSetContext: ChangeSetContext): ChangeSetContext = {

    val impactedNetworkIds = networkInfoImpactAnalyzer.analyze(changeSetContext)

    val networkInfoChanges = impactedNetworkIds.flatMap { networkId =>
      val beforeOption = database.networks.findById(networkId)
      val previousKnownCountry = beforeOption.flatMap(_.country)
      val afterOption = networkRepository.findBaseNetworkById(networkId).flatMap { baseNetworkDoc =>
        networkMainAnalyzer.analyze(
          baseNetworkDoc,
          changeSetContext.timestampAfter,
          previousKnownCountry
        )
      }

      beforeOption match {
        case None =>
          afterOption match {
            case Some(after) => processCreate(changeSetContext, after, networkId)
            case None => None // TODO message ?
          }
        case Some(before) =>
          afterOption match {
            case None =>
              processDelete(changeSetContext, before, networkId)
            case Some(after) =>
              processUpdate(changeSetContext, before, after, networkId)
          }
      }
    }

    changeSetContext.copy(
      changes = changeSetContext.changes.copy(
        networkInfoChanges = networkInfoChanges
      )
    )
  }

  private def processCreate(context: ChangeSetContext, after: NetworkDoc, networkId: Long): Option[NetworkInfoChange] = {
    Some(new NetworkInfoCreateAnalyzer(context, after, networkId).analyze())
  }

  private def processDelete(context: ChangeSetContext, before: NetworkDoc, networkId: Long): Option[NetworkInfoChange] = {
    Some(new NetworkInfoDeleteAnalyzer(context, before, networkId).analyze())
  }

  private def processUpdate(context: ChangeSetContext, before: NetworkDoc, after: NetworkDoc, networkId: Long): Option[NetworkInfoChange] = {
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
