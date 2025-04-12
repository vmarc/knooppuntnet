package kpn.server.analyzer.engine.changes.node.main

import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.LatLonImpl
import kpn.api.common.RouteType
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.diff.common.FactDiffs
import kpn.api.custom.Subset
import kpn.core.analysis.TagInterpreter
import kpn.core.doc.NodeDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.BulkNodeAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.node.NodeChangeStateAnalyzer
import kpn.server.analyzer.engine.changes.node.base.NodeChangeAnalyzer
import kpn.server.analyzer.engine.tile.NodeTileChangeAnalyzer
import kpn.server.repository.NetworkRepository
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class NodeChangeProcessor(
  bulkNodeAnalyzer: BulkNodeAnalyzer,
  nodeChangeAnalyzer: NodeChangeAnalyzer,
  nodeRepository: NodeRepository,
  networkRepository: NetworkRepository,
  tileChangeAnalyzer: NodeTileChangeAnalyzer
) {

  private val log = Log(classOf[NodeChangeProcessor])

  def process(context: ChangeSetContext): ChangeSetContext = {

    log.debugElapsed {

      val batchSize = 500
      val nodeChanges = context.impactedNodeIds.sliding(batchSize, batchSize).toSeq.flatMap { nodeIds =>
        processBatch(context, nodeIds)
      }

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

  private def processBatch(context: ChangeSetContext, nodeIds: Seq[Long]): Seq[NodeChange] = {

    val nodeDocsBefore = nodeRepository.nodesWithIds(nodeIds)
    val nodeDocsAfter = bulkNodeAnalyzer.analyze(nodeIds)

    nodeIds.flatMap { nodeId =>
      val nodeDocBeforeOption = nodeDocsBefore.find(_._id == nodeId)
      val nodeDocAfterOption = nodeDocsAfter.find(_._id == nodeId)

      processChangeData(
        context,
        nodeDocBeforeOption,
        nodeDocAfterOption
      )
    }
  }

  private def processChangeData(
    context: ChangeSetContext,
    nodeDocBeforeOption: Option[NodeDoc],
    nodeDocAfterOption: Option[NodeDoc],
  ): Option[NodeChange] = {

    nodeDocBeforeOption match {
      case None =>
        nodeDocAfterOption match {
          case Some(nodeDocAfter) => processCreate(context, nodeDocAfter)
          case None =>
            // TODO message ?
            None
        }
      case Some(nodeDocBefore) =>
        nodeDocAfterOption match {
          case Some(nodeDocAfter) =>
            if (nodeDocAfter.active) {
              processUpdate(context, nodeDocBefore, nodeDocAfter)
            }
            else {
              nodeRepository.save(nodeDocAfter)
              processDelete(context, nodeDocBefore)
            }

          case None =>
            nodeRepository.save(nodeDocBefore.deactivated)
            processDelete(context, nodeDocBefore)
        }
    }
  }

  private def processCreate(context: ChangeSetContext, nodeDoc: NodeDoc): Option[NodeChange] = {

    val key = context.buildChangeKey(nodeDoc._id)
    val subsets = nodeDoc.country.toSeq.flatMap { country =>
      nodeDoc.names.map(_.routeType).flatMap(routeType => Subset.of(country, routeType))
    }

    val factDiffs = if (nodeDoc.facts.nonEmpty) {
      Some(
        FactDiffs(
          introduced = nodeDoc.facts
        )
      )
    }
    else {
      None
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
          factDiffs = factDiffs,
          facts = Seq.empty,
          initialTags = Some(nodeDoc.tags),
          initialLatLon = Some(LatLonImpl(nodeDoc.latitude, nodeDoc.longitude)),
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
      lostNodeTag(RouteType.hiking, nodeDocBefore, nodeDocAfter, Fact.LostHikingNodeTag),
      lostNodeTag(RouteType.cycling, nodeDocBefore, nodeDocAfter, Fact.LostBicycleNodeTag),
      lostNodeTag(RouteType.horseRiding, nodeDocBefore, nodeDocAfter, Fact.LostHorseNodeTag),
      lostNodeTag(RouteType.motorboat, nodeDocBefore, nodeDocAfter, Fact.LostMotorboatNodeTag),
      lostNodeTag(RouteType.canoe, nodeDocBefore, nodeDocAfter, Fact.LostCanoeNodeTag),
      lostNodeTag(RouteType.inlineSkating, nodeDocBefore, nodeDocAfter, Fact.LostInlineSkateNodeTag)
    ).flatten

    val allNodeTagsLost = !TagInterpreter.isNetworkNode(nodeDocAfter)

    val changeType = if (allNodeTagsLost || !nodeDocAfter.active) {
      nodeRepository.save(nodeDocAfter.deactivated)
      ChangeType.Delete
    }
    else {
      ChangeType.Update
    }

    new NodeDocChangeAnalyzer(
      tileChangeAnalyzer,
      context,
      nodeDocBefore,
      nodeDocAfter,
      lostNodeTagFacts,
      changeType
    ).analyze()
  }

  private def lostNodeTag(routeType: RouteType, nodeDocBefore: NodeDoc, nodeDocAfter: NodeDoc, fact: Fact): Option[Fact] = {
    if (TagInterpreter.isNetworkNode(nodeDocBefore, routeType) &&
      !TagInterpreter.isNetworkNode(nodeDocAfter, routeType)) {
      Some(fact)
    }
    else {
      None
    }
  }

  private def processDelete(context: ChangeSetContext, nodeDoc: NodeDoc): Option[NodeChange] = {

    val key = context.buildChangeKey(nodeDoc._id)
    val subsets = nodeDoc.names.flatMap { nodeName =>
      nodeDoc.country.flatMap { country =>
        Subset.of(country, nodeName.routeType)
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
          removedFromNetwork = nodeDoc.networkReferences.map(_.toRef),
          factDiffs = None,
          facts = Seq(Fact.Deleted),
          initialTags = None,
          initialLatLon = None,
        )
      )
    )
  }
}
