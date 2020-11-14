package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeRoute
import kpn.api.common.common.NodeRouteCount
import kpn.api.custom.NetworkType
import kpn.core.util.Log
import kpn.server.repository.NodeRouteRepository
import org.springframework.stereotype.Component

@Component
class NodeRouteUpdaterImpl(nodeRouteRepository: NodeRouteRepository) extends NodeRouteUpdater {

  private val log = Log(classOf[NodeRouteUpdaterImpl])

  override def update(): Unit = {
    NetworkType.all.foreach { networkType =>
      Log.context(s"NodeRouteUpdate ${networkType.name}") {
        updateNetworkType(networkType)
      }
    }
  }

  private def updateNetworkType(networkType: NetworkType): Unit = {

    val actual = actualNodeRouteCounts(networkType)
    val expected = expectedNodeRouteCounts(networkType)
    val nodeRoutes = readNodeRoutes(networkType)

    val obsoleteIds = analyzeObsoleteNodeRoutes(expected, nodeRoutes)
    val nodeRoutesChanged = analyzeNodeRoutesChanged(networkType, nodeRoutes, obsoleteIds, actual, expected)
    val nodeRoutesAdded = analyzeNodeRoutesAdded(networkType, nodeRoutes, actual, expected)

    deleteObsoleteNodeRoutes(networkType, obsoleteIds)
    updateNodeRoutes(nodeRoutesChanged ++ nodeRoutesAdded)
  }

  private def actualNodeRouteCounts(networkType: NetworkType): Map[Long, Int] = {
    log.debugElapsed(
      ("read actual node route counts", toMap(nodeRouteRepository.actualNodeRouteCounts(networkType)))
    )
  }

  private def expectedNodeRouteCounts(networkType: NetworkType): Map[Long, Int] = {
    log.debugElapsed(
      ("read expected node route counts", toMap(nodeRouteRepository.expectedNodeRouteCounts(networkType)))
    )
  }

  private def readNodeRoutes(networkType: NetworkType): Seq[NodeRoute] = {
    log.debugElapsed(
      ("read node routes", nodeRouteRepository.nodeRoutes(networkType))
    )
  }

  private def updateNodeRoutes(modifiedNodeRoutes: Seq[NodeRoute]): Unit = {
    modifiedNodeRoutes.zipWithIndex.foreach { case (nodeRoute, index) =>
      log.debugElapsed {
        nodeRouteRepository.save(nodeRoute)
        (s"${index + 1}/${modifiedNodeRoutes.size} save ${nodeRoute.id}", ())
      }
    }
  }

  private def deleteObsoleteNodeRoutes(networkType: NetworkType, obsoleteIds: Seq[Long]): Unit = {
    obsoleteIds.zipWithIndex.foreach { case (id, index) =>
      log.debugElapsed {
        nodeRouteRepository.delete(id, networkType)
        (s"${index + 1}/${obsoleteIds.size} delete $id", ())
      }
    }
  }

  private def analyzeObsoleteNodeRoutes(expected: Map[Long, Int], nodeRoutes: Seq[NodeRoute]) = {
    (nodeRoutes.map(_.id).toSet -- expected.keys).toSeq.sorted
  }

  private def analyzeNodeRoutesChanged(
    networkType: NetworkType,
    nodeRoutes: Seq[NodeRoute],
    obsoleteIds: Seq[Long],
    actual: Map[Long, Int],
    expected: Map[Long, Int]
  ): Seq[NodeRoute] = {
    nodeRoutes.flatMap { nodeRoute =>
      if (obsoleteIds.contains(nodeRoute.id)) {
        None
      }
      else {
        val actualRouteCount = actual.getOrElse(nodeRoute.id, 0)
        val expectedRouteCount = expected.get(nodeRoute.id)
        if (nodeRoute.actualRouteCount == actualRouteCount && nodeRoute.expectedRouteCount == expectedRouteCount) {
          None
        }
        else {
          Some(
            NodeRoute(
              nodeRoute.id,
              networkType,
              actualRouteCount,
              expectedRouteCount
            )
          )
        }
      }
    }
  }

  private def analyzeNodeRoutesAdded(
    networkType: NetworkType,
    nodeRoutes: Seq[NodeRoute],
    actual: Map[Long, Int],
    expected: Map[Long, Int]
  ): Seq[NodeRoute] = {

    val existingRouteNodeIds = nodeRoutes.map(_.id).toSet

    expected.keys.toSeq.sorted.flatMap { nodeId =>

      if (existingRouteNodeIds.contains(nodeId)) {
        None
      }
      else {
        val actualRouteCount = actual.getOrElse(nodeId, 0)
        val expectedRouteCount = expected.get(nodeId)
        Some(
          NodeRoute(
            nodeId,
            networkType,
            actualRouteCount,
            expectedRouteCount
          )
        )
      }
    }
  }

  private def toMap(nodeRouteCounts: Seq[NodeRouteCount]): Map[Long, Int] = {
    nodeRouteCounts.map(nodeRouteCount => nodeRouteCount.nodeId -> nodeRouteCount.routeCount).toMap
  }

}
