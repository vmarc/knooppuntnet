package kpn.server.analyzer.engine.changes.node

import kpn.api.common.LatLonImpl
import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.diff.common.FactDiffs
import kpn.api.custom.ChangeType
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.analysis.TagInterpreter
import kpn.core.doc.NodeDoc
import kpn.core.history.NodeTagDiffAnalyzer
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class NodeChangeProcessorImpl(
  analysisContext: AnalysisContext,
  bulkNodeAnalyzer: BulkNodeAnalyzer,
  nodeChangeAnalyzer: NodeChangeAnalyzer,
  nodeRepository: NodeRepository
) extends NodeChangeProcessor {

  private val log = Log(classOf[NodeChangeProcessorImpl])

  def process(context: ChangeSetContext): ChangeSetContext = {

    log.debugElapsed {

      val impactedNodeIds = (
        context.changes.routeChanges.flatMap(_.impactedNodeIds) ++
          context.changes.networkChanges.flatMap(_.impactedNodeIds)
        ).distinct.sorted

      val nodeElementChanges = nodeChangeAnalyzer.analyze(context.changeSet)
      val batchSize = 500
      val changedNodeIds = (nodeElementChanges.elementIds ++ impactedNodeIds).distinct.sorted
      if (changedNodeIds.nonEmpty) {
        log.info(s"${changedNodeIds.size} node(s) impacted: ${changedNodeIds.mkString(", ")}")
      }
      val nodeChanges = changedNodeIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (nodeIds, index) =>
        processBatch(context, nodeElementChanges, nodeIds)
      }.toSeq

      (
        s"${nodeChanges.size} node changes",
        context.copy(
          changes = context.changes.copy(
            nodeChanges = nodeChanges
          )
        )
      )
    }
  }

  private def processBatch(context: ChangeSetContext, nodeElementChanges: ElementChanges, nodeIds: Seq[Long]): Seq[NodeChange] = {

    val nodeDocsBefore = nodeRepository.nodesWithIds(nodeIds)
    val nodeDocsAfter = bulkNodeAnalyzer.analyze(context.timestampAfter, nodeIds)

    nodeIds.flatMap { nodeId =>
      val nodeDocBeforeOption = nodeDocsBefore.find(_._id == nodeId)
      val nodeDocAfterOption = nodeDocsAfter.find(_._id == nodeId)
      val action = nodeElementChanges.action(nodeId)
      processChangeData(
        context,
        nodeDocBeforeOption,
        nodeDocAfterOption,
        action
      )
    }
  }

  private def processChangeData(
    context: ChangeSetContext,
    nodeDocBeforeOption: Option[NodeDoc],
    nodeDocAfterOption: Option[NodeDoc],
    action: ChangeAction
  ): Option[NodeChange] = {

    nodeDocBeforeOption match {
      case None =>
        nodeDocAfterOption match {
          case Some(nodeDocAfter) => processCreate(context, nodeDocAfter, action)
          case None =>
            // TODO message ?
            None
        }
      case Some(nodeDocBefore) =>
        nodeDocAfterOption match {
          case Some(nodeDocAfter) => processUpdate(context, nodeDocBefore, nodeDocAfter)
          case None => processDelete(context, nodeDocBefore)
        }
    }
  }

  private def processCreate(context: ChangeSetContext, nodeDoc: NodeDoc, action: ChangeAction): Option[NodeChange] = {

    analysisContext.watched.nodes.add(nodeDoc._id)
    val key = context.buildChangeKey(nodeDoc._id)
    val subsets = nodeDoc.country.toSeq.flatMap { country =>
      nodeDoc.names.map(_.networkType).flatMap(networkType => Subset.of(country, networkType))
    }

    Some(
      analyzed(
        NodeChange(
          _id = key.toId,
          key = key,
          changeType = ChangeType.Create, // TODO MONGO or derive from action ?
          subsets,
          locations = nodeDoc.locations,
          nodeDoc.name,
          before = None,
          after = Some(nodeDoc.toMeta),
          connectionChanges = Seq.empty,
          roleConnectionChanges = Seq.empty,
          definedInNetworkChanges = Seq.empty,
          tagDiffs = None,
          nodeMoved = None,
          addedToRoute = nodeDoc.routeReferences.map(_.toRef),
          removedFromRoute = Seq.empty,
          addedToNetwork = Seq.empty,
          removedFromNetwork = Seq.empty,
          factDiffs = FactDiffs(),
          facts = Seq.empty,
          initialTags = Some(nodeDoc.tags),
          initialLatLon = Some(LatLonImpl(nodeDoc.latitude, nodeDoc.longitude)),
          tiles = nodeDoc.tiles
        )
      )
    )
  }

  private def analyzed(nodeChange: NodeChange): NodeChange = {
    new NodeChangeStateAnalyzer(nodeChange).analyzed()
  }

  private def processUpdate(context: ChangeSetContext, nodeDocBefore: NodeDoc, nodeDocAfter: NodeDoc): Option[NodeChange] = {

    val nodeId = nodeDocBefore._id

    val lostNodeTagFacts: Seq[Fact] = Seq(
      lostNodeTag(NetworkType.hiking, nodeDocBefore, nodeDocAfter, Fact.LostHikingNodeTag),
      lostNodeTag(NetworkType.cycling, nodeDocBefore, nodeDocAfter, Fact.LostBicycleNodeTag),
      lostNodeTag(NetworkType.horseRiding, nodeDocBefore, nodeDocAfter, Fact.LostHorseNodeTag),
      lostNodeTag(NetworkType.motorboat, nodeDocBefore, nodeDocAfter, Fact.LostMotorboatNodeTag),
      lostNodeTag(NetworkType.canoe, nodeDocBefore, nodeDocAfter, Fact.LostCanoeNodeTag),
      lostNodeTag(NetworkType.inlineSkating, nodeDocBefore, nodeDocAfter, Fact.LostInlineSkateNodeTag)
    ).flatten

    if (!TagInterpreter.isNetworkNode(nodeDocAfter.tags)) {
      analysisContext.watched.nodes.delete(nodeId)
      nodeRepository.save(nodeDocAfter.deactivated)

      val key = context.buildChangeKey(nodeDocAfter._id)
      val subsets = nodeDocBefore.names.flatMap { nodeName => // TODO also consider nodeDocAfter
        nodeDocBefore.country.flatMap { country =>
          Subset.of(country, nodeName.networkType)
        }
      }

      val tagDiffs = new NodeTagDiffAnalyzer(nodeDocBefore, nodeDocAfter).diffs

      val addedToNetwork = context.changes.networkChanges.filter { networkChange =>
        networkChange.nodes.added.contains(nodeId)
      }.map(_.toRef)

      val removedFromNetwork = context.changes.networkChanges.filter { networkChange =>
        networkChange.nodes.removed.contains(nodeId)
      }.map(_.toRef)

      val allTiles = (nodeDocBefore.tiles ++ nodeDocAfter.tiles).distinct.sorted
      val allLocations = (nodeDocBefore.locations ++ nodeDocAfter.locations).distinct.sorted

      Some(
        analyzed(
          NodeChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Delete,
            subsets = subsets,
            locations = allLocations,
            name = nodeDocBefore.name,
            before = Some(nodeDocBefore.toMeta),
            after = Some(nodeDocAfter.toMeta),
            connectionChanges = Seq.empty,
            roleConnectionChanges = Seq.empty,
            definedInNetworkChanges = Seq.empty,
            tagDiffs = tagDiffs,
            nodeMoved = None,
            addedToRoute = Seq.empty,
            removedFromRoute = nodeDocBefore.routeReferences.map(_.toRef),
            addedToNetwork = addedToNetwork,
            removedFromNetwork = removedFromNetwork,
            factDiffs = FactDiffs(),
            facts = lostNodeTagFacts,
            initialTags = None,
            initialLatLon = None,
            tiles = allTiles
          )
        )
      )
    }
    else {
      new NodeDocChangeAnalyzer(context, nodeDocBefore, nodeDocAfter).analyze()
    }
  }

  private def lostNodeTag(networkType: NetworkType, nodeDocBefore: NodeDoc, nodeDocAfter: NodeDoc, fact: Fact): Option[Fact] = {
    if (TagInterpreter.isNetworkNode(nodeDocBefore.tags, networkType) &&
      !TagInterpreter.isNetworkNode(nodeDocAfter.tags, networkType)) {
      Some(fact)
    }
    else {
      None
    }
  }

  private def processDelete(context: ChangeSetContext, nodeDoc: NodeDoc): Option[NodeChange] = {

    analysisContext.watched.nodes.delete(nodeDoc._id)

    nodeRepository.save(nodeDoc.deactivated)

    val key = context.buildChangeKey(nodeDoc._id)
    val subsets = nodeDoc.names.flatMap { nodeName =>
      nodeDoc.country.flatMap { country =>
        Subset.of(country, nodeName.networkType)
      }
    }

    Some(
      analyzed(
        NodeChange(
          _id = key.toId,
          key = key,
          changeType = ChangeType.Delete,
          subsets = subsets,
          locations = nodeDoc.locations,
          name = nodeDoc.name,
          before = Some(nodeDoc.toMeta),
          after = None,
          connectionChanges = Seq.empty,
          roleConnectionChanges = Seq.empty,
          definedInNetworkChanges = Seq.empty,
          tagDiffs = None,
          nodeMoved = None,
          addedToRoute = Seq.empty,
          removedFromRoute = nodeDoc.routeReferences.map(_.toRef),
          addedToNetwork = Seq.empty,
          removedFromNetwork = Seq.empty,
          factDiffs = FactDiffs(),
          facts = Seq(Fact.Deleted),
          initialTags = None,
          initialLatLon = None,
          tiles = nodeDoc.tiles
        )
      )
    )
  }
}
