package kpn.server.analyzer.engine.changes.network

import kpn.core.doc.NetworkDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.main.NetworkMainAnalyzer
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
) {

  private val log = Log(classOf[NetworkChangeProcessor])

  def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {

      val createIds = context.baseNetworkCreateIds
      val updateIds = context.baseNetworkUpdateIds
      val deleteIds = context.baseNetworkDeleteIds

      val createChanges = createIds.flatMap(networkId => processCreate(context, networkId))
      val updateChanges = updateIds.flatMap(networkId => processUpdate(context, networkId))
      val deleteChanges = deleteIds.flatMap(networkId => processDelete(context, networkId))

      val networkChanges = createChanges ++ updateChanges ++ deleteChanges

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

  private def processCreate(context: ChangeSetContext, networkId: Long): Option[NetworkChange] = {

    networkRepository.findBaseNetworkById(networkId) match {
      case None =>
        // TODO log message?
        None
      case Some(baseNetworkDoc) =>
        val beforeOption = networkRepository.findById(networkId)
        val previousKnownCountry = beforeOption.flatMap(_.country)
        networkMainAnalyzer.analyze(baseNetworkDoc, context.timestampAfter, previousKnownCountry) match {
          case None =>
            beforeOption match {
              case None =>
                // TODO log message?
                None
              case Some(beforeNetwork) =>
                deleteNetwork(context, beforeNetwork, networkId)
            }

          case Some(networkDoc) =>

            beforeOption match {
              case None =>
                // TODO TagInterpreter.isNetworkRelation(after) ==> change to delete !!
                createNetwork(context, networkDoc, networkId)

              case Some(beforeNetwork) =>
                if (!networkDoc.active) {
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

  private def processUpdate(context: ChangeSetContext, networkId: Long): Option[NetworkChange] = {

    networkRepository.findBaseNetworkById(networkId) match {
      case None =>
        // TODO log message?
        None
      case Some(baseNetworkDoc) =>
        networkMainAnalyzer.analyze(baseNetworkDoc, context.timestampAfter) match {
          case None =>
            // TODO log message?
            None
          case Some(networkDoc) =>

            val beforeNetworkOption = networkRepository.findById(networkId)

            beforeNetworkOption match {
              case None =>
                createNetwork(context, networkDoc, networkId)

              case Some(beforeNetworkDoc) =>
                updateNetwork(context, beforeNetworkDoc, networkDoc, networkId)
            }
        }
    }
  }

  private def processDelete(context: ChangeSetContext, networkId: Long): Option[NetworkChange] = {
    networkRepository.findById(networkId) match {
      case None =>
        // TODO message ?
        None
      case Some(before) =>
        deleteNetwork(context, before, networkId)
    }
  }

  private def createNetwork(context: ChangeSetContext, after: NetworkDoc, networkId: Long): Option[NetworkChange] = {
    networkRepository.save(after)
    Some(new NetworkCreateAnalyzer(context, after, networkId).analyze())
  }

  private def updateNetwork(context: ChangeSetContext, before: NetworkDoc, after: NetworkDoc, networkId: Long): Option[NetworkChange] = {
    networkRepository.save(after)
    Some(new NetworkUpdateAnalyzer(context, before, after, networkId).analyze())
  }

  private def deleteNetwork(context: ChangeSetContext, before: NetworkDoc, networkId: Long): Option[NetworkChange] = {
    networkRepository.save(before.copy(active = false))
    Some(new NetworkDeleteAnalyzer(context, before, networkId).analyze())
  }
}
