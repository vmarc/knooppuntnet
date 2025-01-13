package kpn.server.analyzer.engine.changes.network

import kpn.api.common.ChangeType
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.main.NetworkMainAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
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

      val createNetworkIds = context.baseNetworkCreatedIds
      val updateNetworkIds = context.baseNetworkUpdatedIds
      val deleteNetworkIds = context.baseNetworkDeletedIds

      val createChanges = createNetworkIds.flatMap(networkId => processCreate(context, networkId))
      val updateChanges = updateNetworkIds.flatMap(networkId => processUpdate(context, networkId))
      val deleteChanges = deleteNetworkIds.flatMap(networkId => processDelete(context, networkId))

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
        networkMainAnalyzer.analyze(baseNetworkDoc, context.timestampAfter) match {
          case None =>
            // TODO log message?
            None
          case Some(networkDoc) =>


            // TODO TagInterpreter.isNetworkRelation(after) ==> change to delete !!

            networkRepository.save(networkDoc)
            val key = context.buildChangeKey(networkDoc._id)
            val networkDataUpdate = NetworkDataUpdate(
              None,
              Some(
                NetworkData(
                  networkDoc.detail.toMeta,
                  networkDoc.summary.name
                )
              )
            )

            Some(
              NetworkChange(
                _id = key.toId,
                key = key,
                networkId = networkDoc._id,
                networkName = networkDoc.summary.name,
                changeType = ChangeType.Create,
                networkDataUpdate = Some(networkDataUpdate),
                nodes = IdDiffs(added = networkDoc.nodes.map(_.id)),
                ways = IdDiffs(added = Seq.empty /* TODO networkDoc.wayMembers.map(_.ref)*/),
                relations = IdDiffs(added = Seq.empty /*after.relationMembers.map(_.ref)*/),
              )
            )
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

            networkRepository.save(networkDoc)

            beforeNetworkOption match {
              case None => None // TODO build 'create' NetworkChange
              case Some(beforeNetworkDoc) =>

                val key = context.buildChangeKey(networkDoc._id)

                val relationDiffAnalyzer = new NetworkDiffAnalyzer(beforeNetworkDoc, networkDoc)

                val metaBefore = beforeNetworkDoc.detail.toMeta
                val metaAfter = networkDoc.detail.toMeta

                val networkDataUpdate = if (beforeNetworkDoc.summary.name != networkDoc.summary.name || metaBefore != metaAfter) {
                  Some(
                    NetworkDataUpdate(
                      Some(
                        NetworkData(
                          beforeNetworkDoc.detail.toMeta,
                          beforeNetworkDoc.summary.name
                        )
                      ),
                      Some(
                        NetworkData(
                          networkDoc.detail.toMeta,
                          networkDoc.summary.name
                        )
                      )
                    )
                  )
                }
                else {
                  None
                }

                Some(
                  NetworkChange(
                    _id = key.toId,
                    key = key,
                    networkId = networkDoc._id,
                    networkName = networkDoc.summary.name,
                    changeType = ChangeType.Update,
                    networkDataUpdate = networkDataUpdate,
                    nodes = relationDiffAnalyzer.nodeDiffs,
                    ways = relationDiffAnalyzer.wayDiffs,
                    relations = relationDiffAnalyzer.relationDiffs
                  )
                )
            }
        }
    }
  }

  private def processDelete(context: ChangeSetContext, networkId: Long): Option[NetworkChange] = {

    analysisContext.watched.networks.delete(networkId)
    networkRepository.findById(networkId) match {
      case None => None
      case Some(networkDoc) =>

        networkRepository.save(networkDoc.copy(active = false))

        val key = context.buildChangeKey(networkDoc._id)
        val networkDataUpdate = NetworkDataUpdate(
          Some(
            NetworkData(
              networkDoc.detail.toMeta,
              networkDoc.summary.name,
            )
          ),
          None
        )

        Some(
          NetworkChange(
            _id = key.toId,
            key = key,
            networkId = networkDoc._id,
            networkName = networkDoc.summary.name,
            changeType = ChangeType.Delete,
            networkDataUpdate = Some(networkDataUpdate),
            nodes = IdDiffs(removed = networkDoc.memberNodeIds),
            ways = IdDiffs(removed = networkDoc.memberWayIds),
            relations = IdDiffs(removed = networkDoc.memberRelationIds)
          )
        )
    }
  }
}
