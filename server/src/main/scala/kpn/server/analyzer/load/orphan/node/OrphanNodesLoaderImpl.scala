package kpn.server.analyzer.load.orphan.node

import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.orphan.node.OrphanNodeCreateProcessor
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.load.NodeLoader
import org.springframework.stereotype.Component

/*
  Loads the details of orphan nodes.
 */
@Component
class OrphanNodesLoaderImpl(
  analysisContext: AnalysisContext,
  nodeIdsLoader: NodeIdsLoader,
  nodesLoader: NodeLoader,
  createProcessor: OrphanNodeCreateProcessor
) extends OrphanNodesLoader {

  private val log = Log(classOf[OrphanNodesLoaderImpl])

  override def load(timestamp: Timestamp): Unit = {
    ScopedNetworkType.all.foreach { scopedNetworkType =>
      Log.context(scopedNetworkType.key) {
        val nodeIds = nodeIdsLoader.loadByType(timestamp, scopedNetworkType)
        val orphanNodeIds = nodeIds.filterNot(isReferenced)
        val nodes = nodesLoader.oldLoad2(timestamp, orphanNodeIds)
        nodes.zipWithIndex.foreach { case (node, index) =>
          log.unitElapsed {
            createProcessor.process(None, node)
            s"(${index + 1}/${nodes.size}) Loaded node ${node.id}"
          }
        }
      }
    }
  }

  private def isReferenced(nodeId: Long): Boolean = {
    analysisContext.data.networks.isReferencingNode(nodeId) ||
      analysisContext.data.orphanRoutes.isReferencingNode(nodeId)
  }
}
