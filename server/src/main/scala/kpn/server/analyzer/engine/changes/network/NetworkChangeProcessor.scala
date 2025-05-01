package kpn.server.analyzer.engine.changes.network

import kpn.api.common.changes.details.NetworkChange
import kpn.core.doc.NetworkDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.main.NetworkMainAnalyzer
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.network.info.NetworkCreateAnalyzer
import kpn.server.analyzer.engine.changes.network.info.NetworkDeleteAnalyzer
import kpn.server.analyzer.engine.changes.network.info.NetworkUpdateAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkChangeProcessor(
  analysisContext: AnalysisContext,
  networkRepository: NetworkRepository,
  networkMainAnalyzer: NetworkMainAnalyzer,
) extends ChangeProcessor {

  private val log = Log(classOf[NetworkChangeProcessor])

  def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {
      val networkChanges = context.impactedNetworkIds.flatMap(networkId => processNetwork(context, networkId))
      (
        s"${networkChanges.size} network changes",
        context.copy(
          changes = context.changes.copy(
            networkChanges = networkChanges
          )
        )
      )
    }
  }

  private def processNetwork(context: ChangeSetContext, networkId: Long): Option[NetworkChange] = {

    networkRepository.findBaseNetworkById(networkId) match {
      case None =>
        // TODO log message?
        None
      case Some(baseNetworkDoc) =>

        if (baseNetworkDoc.active) {
          networkRepository.findById(networkId) match {
            case None =>
              networkMainAnalyzer.analyze(baseNetworkDoc, context.timestampAfter) match {
                case None => None
                case Some(networkDoc) =>
                  createNetwork(context, networkDoc, networkId)
              }
            case Some(before) =>
              networkMainAnalyzer.analyze(baseNetworkDoc, context.timestampAfter) match {
                case None =>
                  deleteNetwork(context, before.copy(active = false), networkId)
                case Some(networkDoc) =>
                  updateNetwork(context, before, networkDoc, networkId)
              }
          }
        }
        else {
          val beforeOption = networkRepository.findById(networkId)
          val previousKnownCountry = beforeOption.flatMap(_.country)
          networkMainAnalyzer.analyze(baseNetworkDoc, context.timestampAfter, previousKnownCountry) match {
            case None =>
              beforeOption match {
                case None =>
                  // TODO log message?
                  None
                case Some(beforeNetwork) =>
                  deleteNetwork(context, beforeNetwork.copy(active = false), networkId)
              }

            case Some(networkDoc) =>

              beforeOption match {
                case None =>
                  // TODO TagInterpreter.isNetworkRelation(after) ==> change to delete !!
                  createNetwork(context, networkDoc, networkId)

                case Some(beforeNetwork) =>
                  if (!networkDoc.active) {
                    networkRepository.save(networkDoc)

                    deleteNetwork(context, beforeNetwork, networkId)
                  }
                  else {
                    if (beforeNetwork.copy(stamp = None) == networkDoc.copy(stamp = None)) {
                      None
                    }
                    else {
                      updateNetwork(context, beforeNetwork, networkDoc, networkId)
                    }
                  }
              }
          }
        }
    }
  }

  private def createNetwork(context: ChangeSetContext, after: NetworkDoc, networkId: Long): Option[NetworkChange] = {
    networkRepository.save(after)
    Some(new NetworkCreateAnalyzer(context, after, networkId).analyze())
  }

  private def updateNetwork(context: ChangeSetContext, before: NetworkDoc, after: NetworkDoc, networkId: Long): Option[NetworkChange] = {
    networkRepository.save(after)
    new NetworkUpdateAnalyzer(context, before, after, networkId).analyze()
  }

  private def deleteNetwork(context: ChangeSetContext, before: NetworkDoc, networkId: Long): Option[NetworkChange] = {
    Some(new NetworkDeleteAnalyzer(context, before, networkId).analyze())
  }
}
