package kpn.core.engine.changes.orphan.node

import kpn.core.engine.analysis.Interpreter
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.engine.changes.ignore.IgnoredNodeAnalyzer
import kpn.core.history.NodeDataDiffAnalyzer
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.NodeInfoBuilder.fromLoadedNode
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NodeChange
import kpn.shared.diff.NodeData
import kpn.shared.diff.common.FactDiffs

class OrphanNodeUpdateProcessorImpl(
  analysisData: AnalysisData,
  analysisRepository: AnalysisRepository,
  ignoredNodeAnalyzer: IgnoredNodeAnalyzer
) extends OrphanNodeUpdateProcessor {

  override def process(context: ChangeSetContext, loadedNodeChange: LoadedNodeChange): Option[NodeChange] = {

    val facts = {
      val ff = Seq(
        if (new Interpreter(NetworkType.hiking).isNetworkNode(loadedNodeChange.before.node.raw) &&
          !new Interpreter(NetworkType.hiking).isNetworkNode(loadedNodeChange.after.node.raw)) {
          Some(Fact.LostHikingNodeTag)
        }
        else {
          None
        },
        if (new Interpreter(NetworkType.bicycle).isNetworkNode(loadedNodeChange.before.node.raw) &&
          !new Interpreter(NetworkType.bicycle).isNetworkNode(loadedNodeChange.after.node.raw)) {
          Some(Fact.LostBicycleNodeTag)
        }
        else {
          None
        },
        if (new Interpreter(NetworkType.horseRiding).isNetworkNode(loadedNodeChange.before.node.raw) &&
          !new Interpreter(NetworkType.horseRiding).isNetworkNode(loadedNodeChange.after.node.raw)) {
          Some(Fact.LostHorseNodeTag)
        }
        else {
          None
        },
        if (new Interpreter(NetworkType.motorboat).isNetworkNode(loadedNodeChange.before.node.raw) &&
          !new Interpreter(NetworkType.motorboat).isNetworkNode(loadedNodeChange.after.node.raw)) {
          Some(Fact.LostMotorboatNodeTag)
        }
        else {
          None
        },
        if (new Interpreter(NetworkType.canoe).isNetworkNode(loadedNodeChange.before.node.raw) &&
          !new Interpreter(NetworkType.canoe).isNetworkNode(loadedNodeChange.after.node.raw)) {
          Some(Fact.LostCanoeNodeTag)
        }
        else {
          None
        },
        if (new Interpreter(NetworkType.inlineSkates).isNetworkNode(loadedNodeChange.before.node.raw) &&
          !new Interpreter(NetworkType.inlineSkates).isNetworkNode(loadedNodeChange.after.node.raw)) {
          Some(Fact.LostInlineSkateNodeTag)
        }
        else {
          None
        }
      ).flatten

      if (ff.contains(Fact.LostHikingNodeTag) ||
        ff.contains(Fact.LostBicycleNodeTag) ||
        ff.contains(Fact.LostHorseNodeTag) ||
        ff.contains(Fact.LostMotorboatNodeTag) ||
        ff.contains(Fact.LostCanoeNodeTag)) {
        Seq(Fact.WasOrphan) ++ ff
      }
      else {
        Seq(Fact.OrphanNode) ++ ff
      }
    }

    val isNetworkNodeX = new Interpreter(NetworkType.hiking).isNetworkNode(loadedNodeChange.after.node.raw) ||
      new Interpreter(NetworkType.bicycle).isNetworkNode(loadedNodeChange.after.node.raw) ||
      new Interpreter(NetworkType.horseRiding).isNetworkNode(loadedNodeChange.after.node.raw) ||
      new Interpreter(NetworkType.motorboat).isNetworkNode(loadedNodeChange.after.node.raw) ||
      new Interpreter(NetworkType.canoe).isNetworkNode(loadedNodeChange.after.node.raw) ||
      new Interpreter(NetworkType.inlineSkates).isNetworkNode(loadedNodeChange.after.node.raw)

    if (!isNetworkNodeX) {
      analysisData.orphanNodes.ignored.delete(loadedNodeChange.id)
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

    val ignoreFacts = ignoredNodeAnalyzer.analyze(loadedNodeChange.after)

    if (ignoreFacts.isEmpty) {
      val display = ignoredNodeAnalyzer.displayAnalyze(loadedNodeChange.after, orphan = true)
      val nodeInfo = fromLoadedNode(loadedNodeChange.after, active = isNetworkNodeX, display = display, orphan = true)
      analysisRepository.saveNode(nodeInfo)

      val subsets = (loadedNodeChange.before.subsets.toSet ++ loadedNodeChange.after.subsets.toSet).toSeq
      val name = if (loadedNodeChange.after.name.nonEmpty) {
        loadedNodeChange.after.name
      }
      else {
        loadedNodeChange.before.name
      }

      Some(
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
    }
    else {
      val nodeInfo = fromLoadedNode(loadedNodeChange.after, active = isNetworkNodeX, orphan = true, ignored = true, facts = ignoreFacts)
      analysisRepository.saveNode(nodeInfo)
      None
    }
  }
}
