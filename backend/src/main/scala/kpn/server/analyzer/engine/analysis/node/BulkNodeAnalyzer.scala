package kpn.server.analyzer.engine.analysis.node

import kpn.core.doc.BaseNodeDoc
import kpn.core.doc.NodeDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.main.NodeMainAnalyzer
import kpn.server.repository.NodeRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class BulkNodeAnalyzer(
  nodeMainAnalyzer: NodeMainAnalyzer,
  nodeRepository: NodeRepository,
) {

  private val BatchSize = 500
  private val log = Log(classOf[BulkNodeAnalyzer])

  def analyze(nodeIds: Seq[Long]): Seq[NodeDoc] = {
    Log.context("nodes") {
      log.info(s"Analyzing ${nodeIds.size} nodes")
      log.infoElapsed {
        val nodeDocs = analyzeNodes(nodeIds)
        nodeRepository.bulkSave(nodeDocs *)
        (s"Analyzed ${nodeDocs.size} nodes", nodeDocs)
      }
    }
  }

  private def analyzeNodes(nodeIds: Seq[Long]): Seq[NodeDoc] = {
    nodeIds.sliding(BatchSize, BatchSize).toSeq.flatMap { batchNodeIds =>
      log.info(s"Analyzing batch $BatchSize nodes")
      val baseNodeDocs = nodeRepository.baseNodesWithIds(batchNodeIds)
      baseNodeDocs.flatMap { baseNodeDoc =>
        Log.context(s"${baseNodeDoc._id}") {
          log.info(s"Analyzing node ${baseNodeDoc._id}")
          analyzeNode(baseNodeDoc)
        }
      }
    }
  }

  private def analyzeNode(baseNodeDoc: BaseNodeDoc): Option[NodeDoc] = {
    nodeMainAnalyzer.analyze(baseNodeDoc).orElse {
      log.error(s"Could not analyze node ${baseNodeDoc._id}")
      None
    }
  }
}
