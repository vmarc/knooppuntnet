package kpn.server.analyzer.engine.analysis.node

import kpn.api.custom.Timestamp
import kpn.core.doc.BaseNodeDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.base.BaseNodeMainAnalyzer
import kpn.server.repository.RawDataRepository
import org.springframework.stereotype.Component

@Component
class BaseNodeBulkAnalyzerImpl(
  rawDataRepository: RawDataRepository,
  baseNodeMainAnalyzer: BaseNodeMainAnalyzer,
) extends BaseNodeBulkAnalyzer {

  private val log = Log(classOf[BaseNodeBulkAnalyzerImpl])

  def analyze(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[BaseNodeDoc] = {
    val batchSize = 500
    Log.context("base-nodes") {
      val nodeCount = nodeIds.size
      log.debug(s"Analyzing $nodeCount base nodes")
      log.debugElapsed {
        val baseNodeDocs = nodeIds.sliding(batchSize, batchSize).toSeq.zipWithIndex.flatMap { case (nodeIdsBatch, index) =>
          log.infoElapsed {
            val rawNodes = rawDataRepository.nodes(timestamp, nodeIdsBatch)
            val batchNodeDocs = rawNodes.flatMap { rawNode =>
              baseNodeMainAnalyzer.analyze(rawNode) match {
                case None =>
                  log.error(s"Could not analyze node ${rawNode.id}")
                  None
                case Some(baseNodeDoc) => Some(baseNodeDoc)
              }
            }
            (s"Analyzed ${batchSize * (index + 1)}/$nodeCount nodes", batchNodeDocs)
          }
        }
        (s"Analyzed $nodeCount base nodes", baseNodeDocs)
      }
    }
  }
}
