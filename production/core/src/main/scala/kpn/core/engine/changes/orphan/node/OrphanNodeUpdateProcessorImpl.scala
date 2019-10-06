package kpn.core.engine.changes.orphan.node

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.engine.changes.node.NodeChangeAnalyzer
import kpn.core.history.NodeDataDiffAnalyzer
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.NodeInfoBuilder.fromLoadedNode
import kpn.core.tools.analyzer.AnalysisContext
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NodeChange
import kpn.shared.diff.NodeData
import kpn.shared.diff.common.FactDiffs

class OrphanNodeUpdateProcessorImpl(
  analysisContext: AnalysisContext,
  analysisData: AnalysisData,
  analysisRepository: AnalysisRepository
) extends OrphanNodeUpdateProcessor {

  override def process(context: ChangeSetContext, loadedNodeChange: LoadedNodeChange): Option[NodeChange] = {

    val facts = {
      val lostNodeTagFacts = Seq(
        lostNodeTag(NetworkType.hiking, loadedNodeChange, Fact.LostHikingNodeTag),
        lostNodeTag(NetworkType.bicycle, loadedNodeChange, Fact.LostBicycleNodeTag),
        lostNodeTag(NetworkType.horseRiding, loadedNodeChange, Fact.LostHorseNodeTag),
        lostNodeTag(NetworkType.motorboat, loadedNodeChange, Fact.LostMotorboatNodeTag),
        lostNodeTag(NetworkType.canoe, loadedNodeChange, Fact.LostCanoeNodeTag),
        lostNodeTag(NetworkType.inlineSkates, loadedNodeChange, Fact.LostInlineSkateNodeTag)
      ).flatten

      if (lostNodeTagFacts.nonEmpty) {
        Seq(Fact.WasOrphan) ++ lostNodeTagFacts
      }
      else {
        Seq(Fact.OrphanNode)
      }
    }

    val isNetworkNodeX = analysisContext.isNetworkNode(loadedNodeChange.after.node.raw)

    if (!isNetworkNodeX) {
      analysisData.orphanNodes.watched.delete(loadedNodeChange.id)
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

    val nodeInfo = fromLoadedNode(loadedNodeChange.after, active = isNetworkNodeX, orphan = true)
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
    if (analysisContext.isNetworkNode(networkType, loadedNodeChange.before.node.raw) &&
      !analysisContext.isNetworkNode(networkType, loadedNodeChange.after.node.raw)) {
      Some(fact)
    }
    else {
      None
    }
  }

}
