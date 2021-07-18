package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.data.raw.RawNode
import kpn.api.common.diff.common.FactDiffs
import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.data.LoadedNode
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class OrphanNodeCreateProcessorImpl(
  analysisContext: AnalysisContext,
  nodeRepository: NodeRepository,
  nodeAnalyzer: NodeAnalyzer
) extends OrphanNodeCreateProcessor {

  override def process(optionalContext: Option[ChangeSetContext], node: RawNode): Option[NodeChange] = {

    analysisContext.data.orphanNodes.watched.add(node.id)

    val nodeAnalysis = nodeAnalyzer.analyze(NodeAnalysis(node, orphan = true))
    nodeRepository.save(nodeAnalysis.toNodeInfo)

    optionalContext.map { context =>
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
    new NodeChangeAnalyzer(nodeChange).analyzed()
  }
}
