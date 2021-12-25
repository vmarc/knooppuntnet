package kpn.core.tools.support

import kpn.api.custom.Subset
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.OrphanRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

object FindOrphanNodesTool {
  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Usage: FindOrphanNodesTool host analysisDatabaseName")
      System.exit(-1)
    }
    val host = args(0)
    val analysisDatabaseName = args(1)
    Couch.executeIn(host, analysisDatabaseName) { database =>
      new FindOrphanNodesTool(database).report()
    }
  }
}

class FindOrphanNodesTool(database: Database) {

  private val referencedNodeIds: scala.collection.mutable.Set[Long] = scala.collection.mutable.Set.empty

  private val nodeRepository = new NodeRepositoryImpl(null, database, false)
  private val routeRepository = new RouteRepositoryImpl(null, database, false)
  private val networkRepository = new NetworkRepositoryImpl(null, database, false)
  private val orphanNodeRepository = new OrphanRepositoryImpl(null, database, false)

  def report(): Unit = {
    collectNetworkNodeReferences()
    collectRouteNodeReferences()

    val knownOrphanNodeIds = Subset.all.flatMap { subset =>
      println(s"collect orphan node ids ${subset.name}")
      orphanNodeRepository.orphanNodes(subset).filter(_.active).map(_.id)
    }.toSet

    println("Collecting node ids")
    val allNodeIds = nodeRepository.allNodeIds().toSet

    val orphanNodeIds = allNodeIds -- referencedNodeIds -- knownOrphanNodeIds
    orphanNodeIds.foreach { nodeId =>
      nodeRepository.nodeWithId(nodeId) match {
        case None =>
        case Some(node) =>
          if (node.active) {
            println(nodeId)
          }
      }
    }
    println("done")
  }

  private def collectNetworkNodeReferences(): Unit = {
    val networkIds = networkRepository.allNetworkIds()
    networkIds.zipWithIndex.foreach { case (networkId, index) =>
      println(s"network ${index + 1}/${networkIds.size}")
      networkRepository.network(networkId) match {
        case None =>
        case Some(network) =>
          if (network.active) {
            referencedNodeIds.addAll(network.nodeRefs)
            network.detail match {
              case Some(detail) => referencedNodeIds.addAll(detail.nodes.map(_.id))
              case None =>
            }
          }
      }
    }
  }

  private def collectRouteNodeReferences(): Unit = {
    val routeIds = routeRepository.allRouteIds()
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if (((index + 1) % 100) == 0) {
        println(s"route ${index + 1}/${routeIds.size}")
      }
      routeRepository.routeWithId(routeId) match {
        case None =>
        case Some(route) =>
          if (route.active) {
            referencedNodeIds.addAll(route.nodeRefs)
          }
      }
    }
  }
}
