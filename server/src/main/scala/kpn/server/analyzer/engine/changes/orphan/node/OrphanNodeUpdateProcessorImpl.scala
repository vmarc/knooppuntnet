package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.diff.NodeData
import kpn.api.common.diff.common.FactDiffs
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.core.history.NodeDataDiffAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.NodeInfoBuilder
import org.springframework.stereotype.Component

@Component
class OrphanNodeUpdateProcessorImpl(
  analysisContext: AnalysisContext,
  analysisRepository: AnalysisRepository,
  nodeInfoBuilder: NodeInfoBuilder
) extends OrphanNodeUpdateProcessor {

  override def process(context: ChangeSetContext, loadedNodeChange: LoadedNodeChange): Option[NodeChange] = {

    val facts = {
      val lostNodeTagFacts = Seq(
        lostNodeTag(NetworkType.hiking, loadedNodeChange, Fact.LostHikingNodeTag),
        lostNodeTag(NetworkType.cycling, loadedNodeChange, Fact.LostBicycleNodeTag),
        lostNodeTag(NetworkType.horseRiding, loadedNodeChange, Fact.LostHorseNodeTag),
        lostNodeTag(NetworkType.motorboat, loadedNodeChange, Fact.LostMotorboatNodeTag),
        lostNodeTag(NetworkType.canoe, loadedNodeChange, Fact.LostCanoeNodeTag),
        lostNodeTag(NetworkType.inlineSkating, loadedNodeChange, Fact.LostInlineSkateNodeTag)
      ).flatten

      if (lostNodeTagFacts.nonEmpty) {
        Seq(Fact.WasOrphan) ++ lostNodeTagFacts
      }
      else {
        Seq(Fact.OrphanNode)
      }
    }

    val isNetworkNodeX = analysisContext.isValidNetworkNode(loadedNodeChange.after.node.raw)

    if (!isNetworkNodeX) {
      analysisContext.data.orphanNodes.watched.delete(loadedNodeChange.id)
    }

    val before = NodeData(
      loadedNodeChange.before.subsets,
      loadedNodeChange.before.name,
      loadedNodeChange.before.node.raw
    )

    val after = NodeData(
      loadedNodeChange.after.subsets,
      loadedNodeChange.after.name,
      loadedNodeChange.after.node.raw
    )

    val nodeDataUpdate = new NodeDataDiffAnalyzer(before, after).analysis

    val nodeInfo = nodeInfoBuilder.fromLoadedNode(loadedNodeChange.after, active = isNetworkNodeX, orphan = true)
    analysisRepository.saveNode(nodeInfo)

    val subsets = (loadedNodeChange.before.subsets.toSet ++ loadedNodeChange.after.subsets.toSet).toSeq
    val name = if (loadedNodeChange.after.name.nonEmpty) {
      loadedNodeChange.after.name
    }
    else {
      loadedNodeChange.before.name
    }

    Some(
      analyzed(
        NodeChange(
          key = context.buildChangeKey(loadedNodeChange.after.node.id),
          changeType = ChangeType.Update,
          subsets = subsets,
          location = nodeInfo.location,
          name = name,
          before = Some(loadedNodeChange.before.node.raw),
          after = Some(loadedNodeChange.after.node.raw),
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
          facts
        )
      )
    )
  }

  private def analyzed(nodeChange: NodeChange): NodeChange = {
    new NodeChangeAnalyzer(nodeChange).analyzed()
  }

  private def lostNodeTag(networkType: NetworkType, loadedNodeChange: LoadedNodeChange, fact: Fact): Option[Fact] = {
    if (analysisContext.isValidNetworkNode(networkType, loadedNodeChange.before.node.raw) &&
      !analysisContext.isValidNetworkNode(networkType, loadedNodeChange.after.node.raw)) {
      Some(fact)
    }
    else {
      None
    }
  }

}
