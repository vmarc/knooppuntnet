package kpn.server.analyzer.load.orphan.node

import kpn.server.analyzer.load.NodeLoader
import kpn.core.util.Log
import kpn.server.analyzer.engine.AnalysisContext
import kpn.server.analyzer.engine.CouchIndexer
import kpn.server.analyzer.engine.changes.orphan.node.OrphanNodeCreateProcessor
import kpn.server.repository.OrphanRepository
import kpn.shared.NetworkType
import kpn.shared.Timestamp
import org.springframework.stereotype.Component

/*
  Loads the details of orphan nodes.
 */
@Component
class OrphanNodesLoaderImpl(
  analysisContext: AnalysisContext,
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
      val nodeIds = nodeIdsLoader.load(timestamp, networkType)
      val orphanNodeIds = nodeIds.filterNot(isReferenced).toSeq.sorted
      val loadedNodes = nodesLoader.load(timestamp, networkType, orphanNodeIds)
      loadedNodes.zipWithIndex.foreach { case (loadedNode, index) =>
        log.unitElapsed {
          createProcessor.process(None, loadedNode)
          s"(${index + 1}/${loadedNodes.size}) Loaded node ${loadedNode.id}"
        }
      }
    }
  }

  private def isReferenced(nodeId: Long): Boolean = {
    analysisContext.data.networks.isReferencingNode(nodeId) ||
      analysisContext.data.orphanRoutes.isReferencingNode(nodeId)
  }
}
