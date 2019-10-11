package kpn.core.engine.changes.orphan.node

import kpn.core.engine.changes.ChangeSetContext
import kpn.core.engine.changes.node.NodeChangeAnalyzer
import kpn.core.load.data.LoadedNode
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.NodeInfoBuilder.fromLoadedNode
import kpn.core.tools.analyzer.AnalysisContext
import kpn.shared.Fact
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NodeChange
import kpn.shared.diff.common.FactDiffs

class OrphanNodeCreateProcessorImpl(
  analysisContext: AnalysisContext,
  analysisRepository: AnalysisRepository
) extends OrphanNodeCreateProcessor {

  override def process(optionalContext: Option[ChangeSetContext], loadedNode: LoadedNode): Option[NodeChange] = {
    analysisContext.data.orphanNodes.watched.add(loadedNode.id)
    val nodeInfo = fromLoadedNode(loadedNode, orphan = true)
    analysisRepository.saveNode(nodeInfo)

    optionalContext.map { context =>
      analyzed(
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
  }

  private def analyzed(nodeChange: NodeChange): NodeChange = {
    new NodeChangeAnalyzer(nodeChange).analyzed()
  }

}
