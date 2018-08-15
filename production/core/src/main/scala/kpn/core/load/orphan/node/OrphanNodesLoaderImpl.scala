package kpn.core.load.orphan.node

import kpn.core.engine.changes.data.AnalysisData
import kpn.core.engine.changes.orphan.node.OrphanNodeCreateProcessor
import kpn.core.load.NodeLoader
import kpn.core.repository.OrphanRepository
import kpn.core.tools.analyzer.CouchIndexer
import kpn.core.util.Log
import kpn.shared.NetworkType
import kpn.shared.Timestamp

/*
  Loads the details of orphan nodes.
 */
class OrphanNodesLoaderImpl(
  analysisData: AnalysisData,
  nodeIdsLoader: NodeIdsLoader,
  nodesLoader: NodeLoader,
  orphanRepository: OrphanRepository,
  createProcessor: OrphanNodeCreateProcessor,
  analysisDatabaseIndexer: CouchIndexer
) extends OrphanNodesLoader {

  private val log = Log(classOf[OrphanNodesLoaderImpl])

  override def load(timestamp: Timestamp): Unit = {

    NetworkType.all.foreach { networkType =>
      analysisDatabaseIndexer.index()
      val ignoredNodeIds = orphanRepository.ignoredNodeIds(networkType)
      val nodeIds = nodeIdsLoader.load(timestamp, networkType)
      val candidateOrphanNodeIds = (nodeIds -- ignoredNodeIds).filterNot(isReferenced).toSeq.sorted
      val loadedNodes = nodesLoader.load(timestamp, networkType, candidateOrphanNodeIds)
      loadedNodes.zipWithIndex.foreach { case (loadedNode, index) =>
        log.unitElapsed {
          createProcessor.process(None, loadedNode)
          s"(${index + 1}/${loadedNodes.size}) Loaded node ${loadedNode.id}"
        }
      }
    }
  }

  private def isReferenced(nodeId: Long): Boolean = {
    analysisData.networks.isReferencingNode(nodeId) || analysisData.orphanRoutes.isReferencingNode(nodeId)
  }
}
