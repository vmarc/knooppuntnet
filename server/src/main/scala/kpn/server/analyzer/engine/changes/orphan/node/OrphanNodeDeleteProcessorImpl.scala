package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.diff.common.FactDiffs
import kpn.api.custom.Fact
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.NodeAnalyzer
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.node.NodeChangeAnalyzer
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class OrphanNodeDeleteProcessorImpl(
  analysisContext: AnalysisContext,
  nodeRepository: NodeRepository,
  nodeAnalyzer: NodeAnalyzer
) extends OrphanNodeDeleteProcessor {

  private val log = Log(classOf[OrphanNodeDeleteProcessorImpl])

  override def process(context: ChangeSetContext, loadedNodeDelete: LoadedNodeDelete): Option[NodeChange] = {

    analysisContext.data.orphanNodes.watched.delete(loadedNodeDelete.id)

    loadedNodeDelete.loadedNode match {
      case Some(loadedNode) =>

        val nodeAnalysis = nodeAnalyzer.analyze(
          NodeAnalysis(
            loadedNode.node.raw,
            active = false,
            orphan = true,
            facts = Seq(Fact.Deleted)
          )
        )
        nodeRepository.save(nodeAnalysis.toNodeInfo)

        if (nodeAnalysis.subsets.nonEmpty) {
          val key = context.buildChangeKey(loadedNodeDelete.id)
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

      case None =>

        log.warn(s"Could not load the 'before' situation at ${context.timestampBefore.yyyymmddhhmmss} while processing node ${loadedNodeDelete.id} delete" +
          " this is unexpected, please investigate")

        val nodeAnalysis = nodeAnalyzer.analyze(
          NodeAnalysis(
            loadedNodeDelete.rawNode,
            active = false,
            orphan = true,
            facts = Seq(Fact.Deleted)
          )
        )
        if (nodeAnalysis.country.nonEmpty) {
          nodeRepository.save(nodeAnalysis.toNodeInfo)
        }
        None
    }
  }

  private def analyzed(nodeChange: NodeChange): NodeChange = {
    new NodeChangeAnalyzer(nodeChange).analyzed()
  }
}
