package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeRoute
import kpn.api.common.common.NodeRouteExpectedCount
import kpn.api.custom.NetworkType
import kpn.core.util.Log
import kpn.server.repository.NodeRouteRepository
import org.springframework.stereotype.Component

@Component
class NodeRouteUpdaterImpl(nodeRouteRepository: NodeRouteRepository) extends NodeRouteUpdater {

  private val log = Log(classOf[NodeRouteUpdaterImpl])

  override def update(): Unit = {
    NetworkType.all.foreach { networkType =>
      Log.context(s"node-route/${networkType.name}") {
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
    log.debugElapsed {
      val counts = nodeRouteRepository.actualNodeRouteCounts(networkType)
      val map = counts.map(nodeRouteCount => nodeRouteCount.nodeId -> nodeRouteCount.routeCount).toMap
      ("read actual node route counts", map)
    }
  }

  private def expectedNodeRouteCounts(networkType: NetworkType): Map[Long, NodeRouteExpectedCount] = {
    log.debugElapsed {
      val counts = nodeRouteRepository.expectedNodeRouteCounts(networkType)
      val map = counts.map(nodeRouteCount => nodeRouteCount.nodeId -> nodeRouteCount).toMap
      ("read expected node route counts", map)
    }
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

  private def analyzeObsoleteNodeRoutes(expected: Map[Long, NodeRouteExpectedCount], nodeRoutes: Seq[NodeRoute]): Seq[Long] = {
    (nodeRoutes.map(_.id).toSet -- expected.keys).toSeq.sorted
  }

  private def analyzeNodeRoutesChanged(
    networkType: NetworkType,
    nodeRoutes: Seq[NodeRoute],
    obsoleteIds: Seq[Long],
    actual: Map[Long, Int],
    expected: Map[Long, NodeRouteExpectedCount]
  ): Seq[NodeRoute] = {
    nodeRoutes.flatMap { nodeRoute =>
      if (obsoleteIds.contains(nodeRoute.id)) {
        None
      }
      else {
        expected.get(nodeRoute.id) match {
          case None => None
          case Some(nodeRouteExpectedCount) =>
            val actualRouteCount = actual.getOrElse(nodeRoute.id, 0)
            if (nodeRoute.actualRouteCount == actualRouteCount &&
              nodeRoute.expectedRouteCount == nodeRouteExpectedCount.routeCount &&
              nodeRoute.name == nodeRouteExpectedCount.nodeName &&
              nodeRoute.locationNames == nodeRouteExpectedCount.locationNames) {
              None
            }
            else {
              Some(
                NodeRoute(
                  nodeRoute.id,
                  nodeRouteExpectedCount.nodeName,
                  networkType,
                  nodeRouteExpectedCount.locationNames,
                  nodeRouteExpectedCount.routeCount,
                  actualRouteCount
                )
              )
            }
        }
      }
    }
  }

  private def analyzeNodeRoutesAdded(
    networkType: NetworkType,
    nodeRoutes: Seq[NodeRoute],
    actual: Map[Long, Int],
    expected: Map[Long, NodeRouteExpectedCount]
  ): Seq[NodeRoute] = {

    val existingRouteNodeIds = nodeRoutes.map(_.id).toSet

    expected.keys.toSeq.sorted.flatMap { nodeId =>

      if (existingRouteNodeIds.contains(nodeId)) {
        None
      }
      else {
        expected.get(nodeId) match {
          case None => None
          case Some(nodeRouteExpectedCount) =>
            val actualRouteCount = actual.getOrElse(nodeId, 0)
            Some(
              NodeRoute(
                nodeId,
                nodeRouteExpectedCount.nodeName,
                networkType,
                nodeRouteExpectedCount.locationNames,
                nodeRouteExpectedCount.routeCount,
                actualRouteCount
              )
            )
        }
      }
    }
  }

}
