package kpn.server.analyzer.engine.changes.node.base

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.BaseNodeBulkAnalyzer
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.repository.NodeRepository
import org.springframework.stereotype.Component

@Component
class BaseNodeChangeProcessor(
  analysisContext: AnalysisContext,
  nodeChangeAnalyzer: NodeChangeAnalyzer,
  nodeRepository: NodeRepository,
  baseNodeBulkAnalyzer: BaseNodeBulkAnalyzer
) {

  private val log = Log(classOf[BaseNodeChangeProcessor])

  def process(context: ChangeSetContext): ChangeSetContext = {
    log.debugElapsed {
      val nodeElementChanges = nodeChangeAnalyzer.analyze(context.changeSet)
      val nodeIds = nodeElementChanges.elementIds
      val baseNodeDocsBefore = nodeRepository.baseNodesWithIds(nodeIds)

      val deletedBaseNodeDocs = nodeElementChanges.deletes.flatMap { nodeId =>
        baseNodeDocsBefore.find(_._id == nodeId).map { doc =>
          doc.copy(
            active = false,
            name = None,
            names = Seq.empty,
            tags = Seq.empty,
            facts = Seq.empty,
            tiles = Seq.empty,
          )
        }
      }
      nodeRepository.bulkSaveBaseNodes(deletedBaseNodeDocs)

      val baseNodeDocsAfter = baseNodeBulkAnalyzer.analyze(context.timestampAfter, nodeIds)
      nodeRepository.bulkSaveBaseNodes(baseNodeDocsAfter)

      val beforeNodeIds = baseNodeDocsBefore.map(_._id).toSet
      val afterNodeIds = baseNodeDocsAfter.filter(_.active).map(_._id).toSet
      val createNodeIds = (afterNodeIds -- beforeNodeIds).toSeq.sorted

      val updateNodeIds = ((afterNodeIds -- beforeNodeIds) ++ (beforeNodeIds -- afterNodeIds)).toSeq.sorted

      val lostNodeTagsNodeIds = updateNodeIds.filter { nodeId =>
        baseNodeDocsBefore.find(_._id == nodeId) match {
          case None => false
          case Some(before) =>
            baseNodeDocsAfter.find(_._id == nodeId) match {
              case None => false
              case Some(after) =>
                NodeChangeFactAnalyzer.facts(before, after).nonEmpty
            }
        }
      }

      createNodeIds.foreach(analysisContext.watched.nodes.add)

      val lostNodeTagsNodeDocs = lostNodeTagsNodeIds.flatMap { nodeId =>
        baseNodeDocsAfter.find(_._id == nodeId)
      }
      lostNodeTagsNodeDocs.filterNot(_.active).map(_._id).foreach(analysisContext.watched.nodes.delete)

      val deleteNodeIds = (beforeNodeIds -- afterNodeIds).toSeq.sorted

      deleteNodeIds.foreach(analysisContext.watched.nodes.delete)

      val updatedContext = context.withImpact(nodeIds = nodeIds)
      (s"${baseNodeDocsAfter.size} node changes", updatedContext)
    }
  }
}
