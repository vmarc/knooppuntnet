package kpn.server.analyzer.engine.changes.network.base

import kpn.api.common.data.MemberType
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
class BaseNetworkChangeProcessorImpl(
  analysisContext: AnalysisContext,
  baseNetworkChangeAnalyzer: BaseNetworkChangeAnalyzer,
  rawDataRepository: RawDataRepository,
  networkRepository: NetworkRepository,
  baseNetworkMainAnalyzer: BaseNetworkMainAnalyzer,
) extends BaseNetworkChangeProcessor {

  private val log = Log(classOf[BaseNetworkChangeProcessorImpl])

  def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {
      val elementChanges = baseNetworkChangeAnalyzer.analyze(context)
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
    val rawRelationMap = buildRawRelationMap(initialContext, networkIds)
    val baseNetworkDocMap = buildBaseNetworkDocMap(networkIds)
    networkIds.foldLeft(initialContext) { (context, networkId) =>
      val beforeBaseNetworkDocOption = baseNetworkDocMap.get(networkId)
      val rawRelation = rawRelationMap.get(networkId)
      processNetwork(context, beforeBaseNetworkDocOption, rawRelation, networkId)
    }
  }

  private def processNetwork(context: ChangeSetContext, beforeBaseNetworkDocOption: Option[BaseNetworkDoc], rawRelationOption: Option[RawRelation], networkId: Long): ChangeSetContext = {
    beforeBaseNetworkDocOption match {
      case None =>
        rawRelationOption match {
          case None =>
            // nothing to do
            context

          case Some(rawRelation) =>
            processCreate(context, rawRelation, networkId)
        }

      case Some(beforeNetworkDoc) =>
        rawRelationOption match {
          case None =>
            processDelete(context, beforeNetworkDoc, networkId)

          case Some(rawRelation) =>
            processUpdate(context, beforeNetworkDoc, rawRelation, networkId)
        }
    }
  }

  private def buildBaseNetworkDocMap(networkIds: Seq[Long]): Map[Long, BaseNetworkDoc] = {
    networkIds.flatMap(networkRepository.findBaseNetworkById).map(baseNetworkDoc => baseNetworkDoc._id -> baseNetworkDoc).toMap
  }

  private def buildRawRelationMap(initialContext: ChangeSetContext, networkIds: Seq[Long]): Map[Long, RawRelation] = {
    rawDataRepository.networks(initialContext.timestampAfter, networkIds).map(rawRelation => rawRelation.id -> rawRelation).toMap
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
          baseNetworkDoc.members.filter(_.memberType == MemberType.Node).map(_.ref),
          baseNetworkDoc.members.filter(_.memberType == MemberType.Relation).map(_.ref),
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

        val beforeNodeIds = before.members.filter(_.memberType == MemberType.Node).map(_.ref).toSet
        val afterNodeIds = baseNetworkDoc.members.filter(_.memberType == MemberType.Node).map(_.ref).toSet
        val addedNodeIds = afterNodeIds -- beforeNodeIds
        val removedNodeIds = beforeNodeIds -- afterNodeIds

        val updatedNodeIds = afterNodeIds.intersect(beforeNodeIds).filter { nodeId =>
          val beforeNodeMember = before.members.find(member => member.isNode && member.ref == nodeId)
          val afterNodeMember = baseNetworkDoc.members.find(member => member.isNode && member.ref == nodeId)
          beforeNodeMember.map(_.role) != afterNodeMember.map(_.role)
        }

        val impactedNodeIds = (addedNodeIds ++ removedNodeIds ++ updatedNodeIds).toSeq.sorted

        val beforeRouteIds = before.members.filter(_.memberType == MemberType.Relation).map(_.ref).toSet
        val afterRouteIds = baseNetworkDoc.members.filter(_.memberType == MemberType.Relation).map(_.ref).toSet
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
    analysisContext.watched.networks.remove(networkId)
    val updatedDoc = before.copy(
      active = false,
      members = Seq.empty,
    )
    networkRepository.saveBaseNetwork(updatedDoc)
    context.withImpact(
      before.members.filter(_.memberType == MemberType.Node).map(_.ref),
      before.members.filter(_.memberType == MemberType.Relation).map(_.ref),
      Seq(networkId)
    )
  }
}
