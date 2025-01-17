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

  private def process(initialContext: ChangeSetContext, networkIds: Seq[Long]): ChangeSetContext = {
    val rawRelations: Map[Long, RawRelation] = rawDataRepository.networks(initialContext.timestampAfter, networkIds).map(rawRelation => rawRelation.id -> rawRelation).toMap
    val existingBaseNetworks: Map[Long, BaseNetworkDoc] = networkIds.flatMap(networkRepository.findBaseNetworkById).map(baseNetworkDoc => baseNetworkDoc._id -> baseNetworkDoc).toMap

    var context = initialContext
    networkIds.foreach { networkId =>
      existingBaseNetworks.get(networkId) match {
        case None =>
          rawRelations.get(networkId) match {
            case None =>
            // nothing to do

            case Some(rawRelation) =>
              context = processCreate(context, rawRelation, networkId)
          }

        case Some(beforeNetworkDoc) =>
          rawRelations.get(networkId) match {
            case None =>
              context = processDelete(context, beforeNetworkDoc, networkId)

            case Some(rawRelation) =>
              context = processUpdate(context, beforeNetworkDoc, rawRelation, networkId)
          }
      }
    }

    //    val createBaseNetworkIds = rawRelations.keys.toSet -- existingBaseNetworks.keys.toSet
    //    val udpateBaseNetworkIds = rawRelations.keys.toSet -- createBaseNetworkIds
    //    val deleteBaseNetworkIds = existingBaseNetworks.keys.toSet -- createBaseNetworkIds -- udpateBaseNetworkIds
    //
    //    val createRelations = rawRelations.values.toSeq.filter(rawRelation => createBaseNetworkIds.contains(rawRelation.id))
    //    val updateRelations = rawRelations.values.toSeq.filter(rawRelation => udpateBaseNetworkIds.contains(rawRelation.id))
    //
    //    var updatedContext = context
    //
    //    val createdIds = createRelations.flatMap(rawRelation => analyzeBaseNetwork(context, rawRelation))
    //    val updatedIds = updateRelations.flatMap(rawRelation => analyzeBaseNetwork(context, rawRelation))
    //
    //    val notUpdatedNetworkIds = udpateBaseNetworkIds -- updatedIds.toSet
    //
    //    val allDeletedIds = (deleteBaseNetworkIds ++ notUpdatedNetworkIds).toSeq.sorted
    //
    //    allDeletedIds.foreach { networkId =>
    //      updatedContext = existingBaseNetworks.get(networkId) match {
    //        case None => updatedContext
    //        case Some(doc) =>
    //          processDelete(updatedContext, doc, networkId)
    //      }
    //    }
    context
  }

  private def processCreate(context: ChangeSetContext, rawRelation: RawRelation, networkId: Long): ChangeSetContext = {
    baseNetworkMainAnalyzer.analyze(rawRelation) match {
      case None =>
        // TODO message?
        context

      case Some(baseNetworkDoc) =>
        analysisContext.watched.networks.add(rawRelation.id)
        networkRepository.saveBaseNetwork(baseNetworkDoc)
        context.withImpact(
          baseNetworkDoc.nodeIds,
          baseNetworkDoc.routeIds,
          Seq(networkId)
        )
    }
  }

  private def processUpdate(context: ChangeSetContext, before: BaseNetworkDoc, rawRelation: RawRelation, networkId: Long): ChangeSetContext = {
    baseNetworkMainAnalyzer.analyze(rawRelation) match {
      case None =>
        processDelete(context, before, networkId)

      case Some(baseNetworkDoc) =>

        analysisContext.watched.networks.add(rawRelation.id)
        networkRepository.saveBaseNetwork(baseNetworkDoc)

        val beforeNodeIds = before.nodeIds.toSet
        val afterNodeIds = baseNetworkDoc.nodeIds.toSet
        val addedNodeIds = afterNodeIds -- beforeNodeIds
        val removedNodeIds = beforeNodeIds -- afterNodeIds
        val impactedNodeIds = (addedNodeIds ++ removedNodeIds).toSeq.sorted

        val beforeRouteIds = before.routeIds.toSet
        val afterRouteIds = baseNetworkDoc.routeIds.toSet
        val addedRouteIds = afterRouteIds -- beforeRouteIds
        val removedRouteIds = beforeRouteIds -- afterRouteIds
        val impactedRouteIds = (addedRouteIds ++ removedRouteIds).toSeq.sorted

        context.withImpact(
          impactedNodeIds,
          impactedRouteIds,
          Seq(networkId)
        )
    }
  }

  private def processDelete(context: ChangeSetContext, before: BaseNetworkDoc, networkId: Long): ChangeSetContext = {
    analysisContext.watched.networks.delete(networkId)
    networkRepository.saveBaseNetwork(before.copy(active = false))
    context.withImpact(
      before.nodeIds,
      before.routeIds,
      Seq(networkId)
    )
  }
}
