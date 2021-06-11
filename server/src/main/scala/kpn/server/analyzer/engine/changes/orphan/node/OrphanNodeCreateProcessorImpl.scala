package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.diff.common.FactDiffs
import kpn.api.custom.Fact
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedNode
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.NodeInfoBuilder
import org.springframework.stereotype.Component

@Component
class OrphanNodeCreateProcessorImpl(
  analysisContext: AnalysisContext,
  analysisRepository: AnalysisRepository,
  nodeInfoBuilder: NodeInfoBuilder
) extends OrphanNodeCreateProcessor {

  override def process(optionalContext: Option[ChangeSetContext], loadedNode: LoadedNode): Option[NodeChange] = {

    analysisContext.data.orphanNodes.watched.add(loadedNode.id)
    val nodeInfo = nodeInfoBuilder.fromLoadedNode(loadedNode, orphan = true)
    analysisRepository.saveNode(nodeInfo)

    optionalContext.map { context =>
      val key = context.buildChangeKey(loadedNode.id)
      analyzed(
        NodeChange(
          _id = key.toId,
          key = key,
          changeType = ChangeType.Create,
          loadedNode.subsets,
          location = nodeInfo.location,
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
