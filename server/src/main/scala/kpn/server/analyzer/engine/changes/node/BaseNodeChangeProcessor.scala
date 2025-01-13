package kpn.server.analyzer.engine.changes.node

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.BaseNodeBulkAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class BaseNodeChangeProcessor(
  nodeChangeAnalyzer: NodeChangeAnalyzer,
  nodeRepository: NodeRepository,
  baseNodeBulkAnalyzer: BaseNodeBulkAnalyzer
) {

  private val log = Log(classOf[BaseNodeChangeProcessor])

  def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {
      val nodeElementChanges = nodeChangeAnalyzer.analyze(context.changeSet)
      val nodeIds = nodeElementChanges.elementIds
      val baseNodeDocs = baseNodeBulkAnalyzer.analyze(context.timestampAfter, nodeIds)
      nodeRepository.bulkSaveBaseNodes(baseNodeDocs)
      (s"${baseNodeDocs.size} node changes", context)
    }
  }
}
