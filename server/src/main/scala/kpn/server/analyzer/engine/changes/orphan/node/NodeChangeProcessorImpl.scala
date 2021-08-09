package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.data.Node
import kpn.api.common.diff.common.FactDiffs
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.core.analysis.TagInterpreter
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.ElementChanges
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.overpass.OverpassRepository
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class NodeChangeProcessorImpl(
  analysisContext: AnalysisContext,
  nodeChangeAnalyzer: NodeChangeAnalyzer,
  overpassRepository: OverpassRepository,
  nodeRepository: NodeRepository,
  nodeAnalyzer: NodeAnalyzer

) extends NodeChangeProcessor {

  private val log = Log(classOf[NodeChangeProcessorImpl])

  def process(context: ChangeSetContext): ChangeSetChanges = {

    log.debugElapsed {

      val nodeElementChanges = nodeChangeAnalyzer.analyze(context.changeSet)
      val batchSize = 500
      val changedNodeIds = nodeElementChanges.elementIds
      if (changedNodeIds.nonEmpty) {
        log.info(s"${changedNodeIds.size} node(s) impacted: ${changedNodeIds.mkString(", ")}")
      }
      val nodeChanges = changedNodeIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (nodeIds, index) =>
        processBatch(context, nodeElementChanges, nodeIds)
      }.toSeq
      ("", ChangeSetChanges(nodeChanges = nodeChanges))



      //      val deletes = {
      //        val deletedNodesBefore = nodeLoader.oldLoadNodes(context.timestampBefore, analysis.deletes.map(_.id)).map(loadedNode => loadedNode.id -> loadedNode).toMap
      //        val loadedNodeDeletes = analysis.deletes.map(rawNode => LoadedNodeDelete(rawNode, deletedNodesBefore.get(rawNode.id)))
      //        loadedNodeDeletes.flatMap(n => deleteProcessor.process(context, n))
      //      }
      //
      //      val creates = analysis.creates.flatMap(n => createProcessor.process(Some(context), n))
      //
      //      val updatedNodesAfter = analysis.updates.map(toLoadedNode)
      //      val updatedNodesBefore = nodeLoader.oldLoadNodes(context.timestampBefore, updatedNodesAfter.map(_.id)).map(loadedNode => loadedNode.id -> loadedNode).toMap
      //      val (updatedNodesWithBefore, updatedNodesWithoutBefore) = updatedNodesAfter.partition(loadedNodeAfter => updatedNodesBefore.contains(loadedNodeAfter.id))
      //      val extraCreates = updatedNodesWithoutBefore.flatMap(n => createProcessor.process(Some(context), n.node.raw))
      //      val updateNodeChanges = updatedNodesWithBefore.map { loadedNodeAfter =>
      //        updatedNodesBefore.get(loadedNodeAfter.id) match {
      //          case Some(loadedNodeBefore) => LoadedNodeChange(loadedNodeBefore, loadedNodeAfter)
      //          case None => throw new IllegalStateException(s"Cannot find loadedNodeBefore id=${loadedNodeAfter.id}")
      //        }
      //      }
      //      val updates = updateNodeChanges.flatMap(nodeChange => updateProcessor.process(context, nodeChange))
      //
      //      val allNodeChanges = creates ++ extraCreates ++ updates ++ deletes
      //      val changeSetChanges = merge(ChangeSetChanges(nodeChanges = allNodeChanges))
      //
      //      val message = s"added=${(creates ++ extraCreates).size}, updated=${updates.size}, removed=${deletes.size}"
      //      (message, changeSetChanges)
    }
  }


  private def processBatch(context: ChangeSetContext, nodeElementChanges: ElementChanges, nodeIds: Seq[Long]): Seq[NodeChange] = {
    val nodeChangeDatas = readBeforeAndAfter(context, nodeIds)
    nodeChangeDatas.flatMap { nodeChangeData =>
      val action = nodeElementChanges.action(nodeChangeData.nodeId)
      processChangeData(context, nodeChangeData, action)
    }
  }

  private def readBeforeAndAfter(context: ChangeSetContext, nodeIds: Seq[Long]): Seq[NodeChangeData] = {
    val beforeNodes = overpassRepository.nodes(context.timestampBefore, nodeIds)
    val afterNodes = overpassRepository.nodes(context.timestampAfter, nodeIds)
    nodeIds.map { nodeId =>
      NodeChangeData(
        nodeId,
        beforeNodes.find(_.id == nodeId).map(Node),
        afterNodes.find(_.id == nodeId).map(Node)
      )
    }
  }

  private def processChangeData(context: ChangeSetContext, data: NodeChangeData, action: ChangeAction): Option[NodeChange] = {

    if (action == ChangeAction.Create) {
      processCreate(context, data)
    }
    else if (action == ChangeAction.Modify) {
      processUpdate(context, data)
    }
    else if (action == ChangeAction.Delete) {
      processDelete(context, data)
    }
    else {
      None
    }
  }

  private def processCreate(context: ChangeSetContext, data: NodeChangeData): Option[NodeChange] = {

    analysisContext.data.nodes.watched.add(data.nodeId)

    data.after map { node =>
      val nodeAnalysis = nodeAnalyzer.analyze(NodeAnalysis(node.raw))
      nodeRepository.save(nodeAnalysis.toNodeDoc)
      val key = context.buildChangeKey(node.id)
      analyzed(
        NodeChange(
          _id = key.toId,
          key = key,
          changeType = ChangeType.Create,
          nodeAnalysis.subsets,
          location = nodeAnalysis.oldLocation,
          nodeAnalysis.name,
          before = None,
          after = Some(nodeAnalysis.node),
          connectionChanges = Seq.empty,
          roleConnectionChanges = Seq.empty,
          definedInNetworkChanges = Seq.empty,
          tagDiffs = None,
          nodeMoved = None,
          addedToRoute = Seq.empty,
          removedFromRoute = Seq.empty,
          addedToNetwork = Seq.empty,
          removedFromNetwork = Seq.empty,
          factDiffs = FactDiffs(),
          Seq(Fact.OrphanNode)
        )
      )
    }
  }

  private def analyzed(nodeChange: NodeChange): NodeChange = {
    new kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzer(nodeChange).analyzed()
  }

  private def processUpdate(context: ChangeSetContext, data: NodeChangeData): Option[NodeChange] = {

    val facts = {
      val lostNodeTagFacts = Seq(
        lostNodeTag(NetworkType.hiking, data, Fact.LostHikingNodeTag),
        lostNodeTag(NetworkType.cycling, data, Fact.LostBicycleNodeTag),
        lostNodeTag(NetworkType.horseRiding, data, Fact.LostHorseNodeTag),
        lostNodeTag(NetworkType.motorboat, data, Fact.LostMotorboatNodeTag),
        lostNodeTag(NetworkType.canoe, data, Fact.LostCanoeNodeTag),
        lostNodeTag(NetworkType.inlineSkating, data, Fact.LostInlineSkateNodeTag)
      ).flatten

      if (lostNodeTagFacts.nonEmpty) {
        Seq(Fact.WasOrphan) ++ lostNodeTagFacts
      }
      else {
        Seq(Fact.OrphanNode)
      }
    }

    data.before match {
      case None => None
      case Some(before) =>
        data.after match {
          case None => None
          case Some(after) =>

            val isNetworkNodeX = TagInterpreter.isValidNetworkNode(after.raw)

            if (!isNetworkNodeX) {
              analysisContext.data.nodes.watched.delete(data.nodeId)
            }

            //            val before = NodeData(
            //              loadedNodeChange.before.subsets,
            //              loadedNodeChange.before.name,
            //              loadedNodeChange.before.node.raw
            //            )
            //
            //            val after = NodeData(
            //              loadedNodeChange.after.subsets,
            //              loadedNodeChange.after.name,
            //              loadedNodeChange.after.node.raw
            //            )

            //            val nodeDataUpdate = new NodeDataDiffAnalyzer(before, after).analysis

            val nodeAfterAnalysis = nodeAnalyzer.analyze(
              NodeAnalysis(
                after.raw,
                // active = isNetworkNodeX,
                // orphan = true
              )
            )

            nodeRepository.save(nodeAfterAnalysis.toNodeDoc)

            //            val subsets = (loadedNodeChange.before.subsets.toSet ++ loadedNodeChange.after.subsets.toSet).toSeq
            //            val name = if (loadedNodeChange.after.name.nonEmpty) {
            //              loadedNodeChange.after.name
            //            }
            //            else {
            //              loadedNodeChange.before.name
            //            }

            val key = context.buildChangeKey(data.nodeId)

            Some(
              analyzed(
                NodeChange(
                  _id = key.toId,
                  key = key,
                  changeType = ChangeType.Update,
                  subsets = nodeAfterAnalysis.subsets,
                  location = nodeAfterAnalysis.oldLocation,
                  name = nodeAfterAnalysis.name,
                  before = Some(before.raw),
                  after = Some(after.raw),
                  connectionChanges = Seq.empty,
                  roleConnectionChanges = Seq.empty,
                  definedInNetworkChanges = Seq.empty,
                  tagDiffs = None, // TODO nodeDataUpdate.flatMap(_.tagDiffs),
                  nodeMoved = None, // TODO nodeDataUpdate.flatMap(_.nodeMoved),
                  addedToRoute = Seq.empty,
                  removedFromRoute = Seq.empty,
                  addedToNetwork = Seq.empty,
                  removedFromNetwork = Seq.empty,
                  factDiffs = FactDiffs(),
                  facts
                )
              )
            )
        }
    }
  }

  private def lostNodeTag(networkType: NetworkType, data: NodeChangeData, fact: Fact): Option[Fact] = {
    data.before match {
      case None => None
      case Some(before) =>
        data.after match {
          case None => None
          case Some(after) =>
            if (TagInterpreter.isValidNetworkNode(networkType, before.raw) &&
              !TagInterpreter.isValidNetworkNode(networkType, after.raw)) {
              Some(fact)
            }
            else {
              None
            }
        }
    }
  }

  private def processDelete(context: ChangeSetContext, data: NodeChangeData): Option[NodeChange] = {

    analysisContext.data.nodes.watched.delete(data.nodeId)

    data.before match {
      case Some(before) =>
        val nodeAnalysis = nodeAnalyzer.analyze(
          NodeAnalysis(
            before.raw,
            active = false,
            facts = Seq(Fact.Deleted)
          )
        )
        nodeRepository.save(nodeAnalysis.toNodeDoc)
        saveNodeChange(context, nodeAnalysis)

      case None =>
        log.warn(s"Could not load the 'before' situation at ${context.timestampBefore.yyyymmddhhmmss} while processing node ${data.nodeId} delete" +
          " this is unexpected, please investigate")


        None
      // TODO pick up node data from changeset data, or better/easier to read from database ???

      //        val nodeAnalysis = nodeAnalyzer.analyze(
      //          NodeAnalysis(
      //            loadedNodeDelete.rawNode,
      //            active = false,
      //            orphan = true,
      //            facts = Seq(Fact.Deleted)
      //          )
      //        )
      //
      //        nodeAnalysis.country match {
      //          case None => None
      //          case _ =>
      //            nodeRepository.save(nodeAnalysis.toNodeDoc)
      //            saveNodeChange(context, nodeAnalysis)
      //        }
    }
  }

  private def saveNodeChange(context: ChangeSetContext, nodeAnalysis: NodeAnalysis): Option[NodeChange] = {
    if (nodeAnalysis.subsets.nonEmpty) {
      val key = context.buildChangeKey(nodeAnalysis.node.id)
      Some(
        analyzed(
          NodeChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Delete,
            subsets = nodeAnalysis.subsets,
            location = nodeAnalysis.oldLocation,
            name = nodeAnalysis.name,
            before = Some(nodeAnalysis.node),
            after = None,
            connectionChanges = Seq.empty,
            roleConnectionChanges = Seq.empty,
            definedInNetworkChanges = Seq.empty,
            tagDiffs = None,
            nodeMoved = None,
            addedToRoute = Seq.empty,
            removedFromRoute = Seq.empty,
            addedToNetwork = Seq.empty,
            removedFromNetwork = Seq.empty,
            factDiffs = FactDiffs(),
            Seq(Fact.WasOrphan, Fact.Deleted)
          )
        )
      )
    }
    else {
      None
    }
  }
}
