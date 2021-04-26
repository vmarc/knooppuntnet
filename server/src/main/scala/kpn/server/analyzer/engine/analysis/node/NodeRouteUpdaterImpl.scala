package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeRoute
import kpn.api.common.common.NodeRouteExpectedCount
import kpn.api.custom.ScopedNetworkType
import kpn.core.util.Log
import kpn.server.repository.NodeRouteRepository
import org.springframework.stereotype.Component

@Component
class NodeRouteUpdaterImpl(nodeRouteRepository: NodeRouteRepository) extends NodeRouteUpdater {

  private val log = Log(classOf[NodeRouteUpdaterImpl])

  override def update(): Unit = {
    log.debugElapsed {
      ScopedNetworkType.all.foreach { scopedNetworkType =>
        Log.context(s"node-route/${scopedNetworkType.key}") {
          log.debugElapsed {
            updateScopedNetworkType(scopedNetworkType)
            (s"update ${scopedNetworkType.key}", ())
          }
        }
      }
      ("complete update", ())
    }
  }

  private def updateScopedNetworkType(scopedNetworkType: ScopedNetworkType): Unit = {

    val actual = actualNodeRouteCounts(scopedNetworkType)
    val expected = expectedNodeRouteCounts(scopedNetworkType)
    val nodeRoutes = readNodeRoutes(scopedNetworkType)

    val obsoleteIds = analyzeObsoleteNodeRoutes(expected, nodeRoutes)
    val nodeRoutesChanged = analyzeNodeRoutesChanged(scopedNetworkType, nodeRoutes, obsoleteIds, actual, expected)
    val nodeRoutesAdded = analyzeNodeRoutesAdded(scopedNetworkType, nodeRoutes, actual, expected)

    log.debug(s"obsolete=${obsoleteIds.size}, changed=${nodeRoutesChanged.size}, added=${nodeRoutesAdded.size}")

    deleteObsoleteNodeRoutes(scopedNetworkType, obsoleteIds)
    updateNodeRoutes(nodeRoutesChanged ++ nodeRoutesAdded)
  }

  private def actualNodeRouteCounts(scopedNetworkType: ScopedNetworkType): Map[Long, Int] = {
    log.debugElapsed {
      val counts = nodeRouteRepository.actualNodeRouteCounts(scopedNetworkType)
      val map = counts.map(nodeRouteCount => nodeRouteCount.nodeId -> nodeRouteCount.routeCount).toMap
      (s"read actual node route counts (${counts.size} nodes)", map)
    }
  }

  private def expectedNodeRouteCounts(scopedNetworkType: ScopedNetworkType): Map[Long, NodeRouteExpectedCount] = {
    log.debugElapsed {
      val counts = nodeRouteRepository.expectedNodeRouteCounts(scopedNetworkType)
      val map = counts.map(nodeRouteCount => nodeRouteCount.nodeId -> nodeRouteCount).toMap
      (s"read expected node route counts (${counts.size} nodes)", map)
    }
  }

  private def readNodeRoutes(scopedNetworkType: ScopedNetworkType): Seq[NodeRoute] = {
    log.debugElapsed {
      val nodeRoutes = nodeRouteRepository.nodeRoutes(scopedNetworkType)
      (s"read node routes (${nodeRoutes.size} nodes)", nodeRoutes)
    }
  }

  private def updateNodeRoutes(modifiedNodeRoutes: Seq[NodeRoute]): Unit = {
    modifiedNodeRoutes.zipWithIndex.foreach { case (nodeRoute, index) =>
      log.debugElapsed {
        nodeRouteRepository.save(nodeRoute)
        (s"${index + 1}/${modifiedNodeRoutes.size} save ${nodeRoute.id}", ())
      }
    }
  }

  private def deleteObsoleteNodeRoutes(scopedNetworkType: ScopedNetworkType, obsoleteIds: Seq[Long]): Unit = {
    obsoleteIds.zipWithIndex.foreach { case (id, index) =>
      log.debugElapsed {
        nodeRouteRepository.delete(id, scopedNetworkType)
        (s"${index + 1}/${obsoleteIds.size} delete $id", ())
      }
    }
  }

  private def analyzeObsoleteNodeRoutes(expected: Map[Long, NodeRouteExpectedCount], nodeRoutes: Seq[NodeRoute]): Seq[Long] = {
    (nodeRoutes.map(_.id).toSet -- expected.keys).toSeq.sorted
  }

  private def analyzeNodeRoutesChanged(
    scopedNetworkType: ScopedNetworkType,
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
                  scopedNetworkType.networkType,
                  scopedNetworkType.networkScope,
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
    scopedNetworkType: ScopedNetworkType,
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
                scopedNetworkType.networkType,
                scopedNetworkType.networkScope,
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
