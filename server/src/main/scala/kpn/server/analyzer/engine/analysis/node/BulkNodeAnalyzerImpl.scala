package kpn.server.analyzer.engine.analysis.node

import kpn.api.custom.Timestamp
import kpn.core.doc.NodeDoc
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class BulkNodeAnalyzerImpl(
  //  rawDataRepository: RawDataRepository,
  //  baseNodeMainAnalyzer: BaseNodeMainAnalyzer,
  //  database: Database,
  //  overpassRepository: OverpassRepository,
  //  nodeAnalyzer: NodeAnalyzer,
  //  nodeMainAnalyzer: NodeMainAnalyzer
) extends BulkNodeAnalyzer {

  private val log = Log(classOf[BulkNodeAnalyzerImpl])

  def analyze(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[NodeDoc] = {
    Seq.empty
    //    val batchSize = 500
    //    Log.context("base-nodes") {
    //      val nodeCount = nodeIds.size
    //      log.info(s"Analyzing $nodeCount base nodes")
    //      log.infoElapsed {
    //        val nodeDocs = nodeIds.sliding(batchSize, batchSize).toSeq.zipWithIndex.flatMap { case (nodeIdsBatch, index) =>
    //          log.infoElapsed {
    //            val rawNodes = rawDataRepository.nodes(timestamp, nodeIdsBatch)
    //            val batchNodeDocs = rawNodes.flatMap { rawNode =>
    //              baseNodeMainAnalyzer.analyze(rawNode) match {
    //                case None =>
    //                  log.error(s"Could not analyze node ${rawNode.id}")
    //                  None
    //                case Some(baseNodeDoc) => Some(baseNodeDoc)
    //              }
    //            }
    //            (s"Analyzed ${batchSize * (index + 1)}/$nodeCount nodes", batchNodeDocs)
    //          }
    //        }
    //        (s"Analyzed $nodeCount nodes", nodeDocs)
    //      }
    //    }
  }
}
