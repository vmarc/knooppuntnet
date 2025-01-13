package kpn.server.analyzer.engine.changes.network

import kpn.api.common.data.raw.RawRelation
import kpn.core.doc.BaseNetworkDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.network.base.BaseNetworkMainAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.NetworkRepository
import kpn.server.repository.RawDataRepository
import org.springframework.stereotype.Component

@Component
class BaseNetworkChangeProcessor(
  analysisContext: AnalysisContext,
  networkChangeAnalyzer: NetworkChangeAnalyzer,
  rawDataRepository: RawDataRepository,
  networkRepository: NetworkRepository,
  baseNetworkMainAnalyzer: BaseNetworkMainAnalyzer,
) {

  private val log = Log(classOf[BaseNetworkChangeProcessor])

  def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {
      val batchSize = 100
      val elementChanges = networkChangeAnalyzer.analyze(context)
      val changedNetworkIds = elementChanges.elementIds
      if (changedNetworkIds.nonEmpty) {
        log.info(s"${changedNetworkIds.size} network(s) impacted: ${changedNetworkIds.mkString(", ")}")
      }
      val updatedContext = process(context, changedNetworkIds)

      (
        s"${changedNetworkIds.size} network changes",
        updatedContext
      )
    }
  }

  private def process(context: ChangeSetContext, networkIds: Seq[Long]): ChangeSetContext = {
    val rawRelations: Map[Long, RawRelation] = rawDataRepository.networks(context.timestampAfter, networkIds).map(rawRelation => rawRelation.id -> rawRelation).toMap
    val existingBaseNetworks: Map[Long, BaseNetworkDoc] = networkIds.flatMap(networkRepository.findBaseNetworkById).map(baseNetworkDoc => baseNetworkDoc._id -> baseNetworkDoc).toMap

    val createBaseNetworkIds = rawRelations.keys.toSet -- existingBaseNetworks.keys.toSet
    val udpateBaseNetworkIds = rawRelations.keys.toSet -- createBaseNetworkIds
    val deleteBaseNetworkIds = existingBaseNetworks.keys.toSet -- createBaseNetworkIds -- udpateBaseNetworkIds

    val createRelations = rawRelations.values.toSeq.filter(rawRelation => createBaseNetworkIds.contains(rawRelation.id))
    val updateRelations = rawRelations.values.toSeq.filter(rawRelation => udpateBaseNetworkIds.contains(rawRelation.id))

    val createdIds = createRelations.flatMap(rawRelation => analyzeBaseNetwork(context, rawRelation))
    val updatedIds = updateRelations.flatMap(rawRelation => analyzeBaseNetwork(context, rawRelation))

    val notUpdatedNetworkIds = udpateBaseNetworkIds -- updatedIds.toSet

    val allDeletedIds = deleteBaseNetworkIds ++ notUpdatedNetworkIds

    val realDeletedIds = allDeletedIds.flatMap(processDelete)

    context.copy(
      baseNetworkCreatedIds = createdIds,
      baseNetworkUpdatedIds = updatedIds,
      baseNetworkDeletedIds = realDeletedIds.toSeq.sorted,
    )
  }

  def analyzeBaseNetwork(context: ChangeSetContext, rawRelation: RawRelation): Option[Long] = {
    analysisContext.watched.networks.add(rawRelation.id)
    baseNetworkMainAnalyzer.analyze(rawRelation) match {
      case None => None
      case Some(baseNetworkDoc) =>
        networkRepository.saveBaseNetwork(baseNetworkDoc)
        Some(baseNetworkDoc._id)
    }
  }

  def processDelete(networkId: Long): Option[Long] = {
    analysisContext.watched.networks.delete(networkId)
    networkRepository.findBaseNetworkById(networkId) match {
      case None => None
      case Some(baseNetworkDoc) =>
        networkRepository.saveBaseNetwork(baseNetworkDoc.copy(active = false))
        Some(baseNetworkDoc._id)
    }
  }
}
