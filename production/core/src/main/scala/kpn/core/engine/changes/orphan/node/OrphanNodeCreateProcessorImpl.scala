package kpn.core.engine.changes.orphan.node

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.data.AnalysisData
import kpn.core.engine.changes.ignore.IgnoredNodeAnalyzer
import kpn.core.load.data.LoadedNode
import kpn.core.repository.AnalysisRepository
import kpn.core.repository.NodeInfoBuilder.fromLoadedNode
import kpn.shared.Fact
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NodeChange
import kpn.shared.diff.common.FactDiffs

class OrphanNodeCreateProcessorImpl(
  analysisData: AnalysisData,
  analysisRepository: AnalysisRepository,
  ignoredNodeAnalyzer: IgnoredNodeAnalyzer
) extends OrphanNodeCreateProcessor {

  override def process(optionalContext: Option[ChangeSetContext], loadedNode: LoadedNode): Option[NodeChange] = {
    val ignoreFacts = ignoredNodeAnalyzer.analyze(loadedNode)

    if (ignoreFacts.isEmpty) {
      analysisData.orphanNodes.ignored.delete(loadedNode.id)
      analysisData.orphanNodes.watched.add(loadedNode.id)
      val display = ignoredNodeAnalyzer.displayAnalyze(loadedNode, orphan = true)
      val nodeInfo = fromLoadedNode(loadedNode, display = display, orphan = true)
      analysisRepository.saveNode(nodeInfo)
    }
    else {
      analysisData.orphanNodes.watched.delete(loadedNode.id)
      analysisData.orphanNodes.ignored.add(loadedNode.id)
      val nodeInfo = fromLoadedNode(loadedNode, ignored = true, orphan = true, facts = ignoreFacts)
      analysisRepository.saveNode(nodeInfo)
    }

    optionalContext.flatMap { context =>
      if (ignoreFacts.isEmpty) {
        Some(
          NodeChange(
            key = context.buildChangeKey(loadedNode.id),
            changeType = ChangeType.Create,
            loadedNode.subsets,
            loadedNode.name,
            before = None,
            after = Some(loadedNode.node.raw),
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
      else {
        None
      }
    }
  }
}
