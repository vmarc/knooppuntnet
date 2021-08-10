package kpn.server.analyzer.engine.changes.node

import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.data.Node
import kpn.api.common.diff.NodeData
import kpn.api.common.diff.common.FactDiffs
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.analysis.TagInterpreter
import kpn.core.history.NodeDataDiffAnalyzer
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

  def process(context: ChangeSetContext, impactedNodeIds: Seq[Long]): ChangeSetChanges = {

    log.debugElapsed {

      val nodeElementChanges = nodeChangeAnalyzer.analyze(context.changeSet)
      val batchSize = 500
      val changedNodeIds = (nodeElementChanges.elementIds ++ impactedNodeIds).distinct.sorted
      if (changedNodeIds.nonEmpty) {
        log.info(s"${changedNodeIds.size} node(s) impacted: ${changedNodeIds.mkString(", ")}")
      }
      val nodeChanges = changedNodeIds.sliding(batchSize, batchSize).zipWithIndex.flatMap { case (nodeIds, index) =>
        processBatch(context, nodeElementChanges, nodeIds)
      }.toSeq
      ("", ChangeSetChanges(nodeChanges = nodeChanges))
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
    data.before match {
      case None =>
        data.after match {
          case None =>
            // TODO message ?
            None
          case Some(after) =>
            processCreate(context, data)
        }
      case Some(before) =>
        data.after match {
          case None =>
            processDelete(context, data)
          case Some(after) =>
            processUpdate(context, data)
        }
    }
  }

  private def processCreate(context: ChangeSetContext, data: NodeChangeData): Option[NodeChange] = {

    data.after.flatMap { node =>
      nodeAnalyzer.analyze(NodeAnalysis(node.raw)).map { nodeAnalysis =>
        analysisContext.data.nodes.watched.add(data.nodeId)
        nodeRepository.save(nodeAnalysis.toNodeDoc)
        val key = context.buildChangeKey(node.id)
        analyzed(
          NodeChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Create,
            nodeAnalysis.subsets,
            locations = nodeAnalysis.locations,
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
            Seq.empty
          )
        )
      }
    }
  }

  private def analyzed(nodeChange: NodeChange): NodeChange = {
    new kpn.server.analyzer.engine.changes.node.NodeChangeStateAnalyzer(nodeChange).analyzed()
  }

  private def processUpdate(context: ChangeSetContext, data: NodeChangeData): Option[NodeChange] = {

    val lostNodeTagFacts = Seq(
      lostNodeTag(NetworkType.hiking, data, Fact.LostHikingNodeTag),
      lostNodeTag(NetworkType.cycling, data, Fact.LostBicycleNodeTag),
      lostNodeTag(NetworkType.horseRiding, data, Fact.LostHorseNodeTag),
      lostNodeTag(NetworkType.motorboat, data, Fact.LostMotorboatNodeTag),
      lostNodeTag(NetworkType.canoe, data, Fact.LostCanoeNodeTag),
      lostNodeTag(NetworkType.inlineSkating, data, Fact.LostInlineSkateNodeTag)
    ).flatten

    data.before match {
      case None => None
      case Some(before) =>
        data.after match {
          case None => None
          case Some(after) =>

            // val isNetworkNodeX = TagInterpreter.isValidNetworkNode(after.raw)

            if (lostNodeTagFacts.nonEmpty) {
              analysisContext.data.nodes.watched.delete(data.nodeId)
            }

            nodeAnalyzer.analyze(NodeAnalysis(before.raw)) match {
              case None => None // TODO message?
              case Some(nodeBeforeAnalysis) =>

                nodeAnalyzer.analyze(NodeAnalysis(after.raw, active = lostNodeTagFacts.isEmpty)) match {
                  case None => None // TODO message?
                  case Some(nodeAfterAnalysis) =>
                    val nodeDataBefore = NodeData(
                      nodeBeforeAnalysis.subsets,
                      nodeBeforeAnalysis.name,
                      nodeBeforeAnalysis.node
                    )

                    val nodeDataAfter = NodeData(
                      nodeAfterAnalysis.subsets,
                      nodeAfterAnalysis.name,
                      nodeAfterAnalysis.node
                    )

                    val nodeDataUpdate = new NodeDataDiffAnalyzer(nodeDataBefore, nodeDataAfter).analysis

                    nodeRepository.save(nodeAfterAnalysis.toNodeDoc)

                    val subsets = (nodeBeforeAnalysis.subsets.toSet ++ nodeAfterAnalysis.subsets.toSet).toSeq
                    val name = if (nodeAfterAnalysis.name.nonEmpty) {
                      nodeAfterAnalysis.name
                    }
                    else {
                      nodeBeforeAnalysis.name
                    }

                    val key = context.buildChangeKey(data.nodeId)

                    Some(
                      analyzed(
                        NodeChange(
                          _id = key.toId,
                          key = key,
                          changeType = ChangeType.Update,
                          subsets = subsets,
                          locations = nodeAfterAnalysis.locations,
                          name = name,
                          before = Some(before.raw),
                          after = Some(after.raw),
                          connectionChanges = Seq.empty,
                          roleConnectionChanges = Seq.empty,
                          definedInNetworkChanges = Seq.empty,
                          tagDiffs = nodeDataUpdate.flatMap(_.tagDiffs),
                          nodeMoved = nodeDataUpdate.flatMap(_.nodeMoved),
                          addedToRoute = Seq.empty,
                          removedFromRoute = Seq.empty,
                          addedToNetwork = Seq.empty,
                          removedFromNetwork = Seq.empty,
                          factDiffs = FactDiffs(),
                          lostNodeTagFacts
                        )
                      )
                    )
                }
            }
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
      case None =>
        nodeRepository.nodeWithId(data.nodeId).map { nodeDoc =>
          nodeRepository.save(
            nodeDoc.copy(
              labels = Seq.empty,
              active = false,
              facts = Seq.empty,
              tiles = Seq.empty,
              integrity = None,
              routeReferences = Seq.empty
            )
          )

          val key = context.buildChangeKey(data.nodeId)
          val subsets = nodeDoc.names.flatMap { nodeName =>
            nodeDoc.country.flatMap { country =>
              Subset.of(country, nodeName.networkType)
            }
          }

          analyzed(
            NodeChange(
              _id = key.toId,
              key = key,
              changeType = ChangeType.Delete,
              subsets = subsets,
              locations = nodeDoc.locations,
              name = nodeDoc.name,
              before = None,
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
              Seq(Fact.Deleted)
            )
          )
        }

      case Some(before) =>
        val nodeAnalysisOption = nodeAnalyzer.analyze(
          NodeAnalysis(
            before.raw,
            active = false,
            facts = Seq(Fact.Deleted)
          )
        )
        nodeAnalysisOption match {
          case None => None // TODO message?
          case Some(nodeAnalysis) =>
            nodeRepository.save(nodeAnalysis.toNodeDoc)
            saveNodeChange(context, nodeAnalysis)
        }

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
            locations = nodeAnalysis.locations,
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
