package kpn.core.database.views.location

import kpn.api.custom.ScopedNetworkType
import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.analysis.node.NodeRouteUpdaterImpl
import kpn.server.repository.NodeRouteRepositoryImpl

object NodeRouteReferenceDemo {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("localhost", "analysis") { database =>
      new NodeRouteReferenceDemo(database).update()
    }
  }
}

class NodeRouteReferenceDemo(database: Database) {

  val repo = new NodeRouteRepositoryImpl(database)

  def update(): Unit = {
    val updater = new NodeRouteUpdaterImpl(repo)
    updater.update()
    println("Done")
  }

  def deleteRouteNodes(): Unit = {
    ScopedNetworkType.all.foreach { scopedNetworkType =>
      val nodeRoutes = repo.nodeRoutes(scopedNetworkType)
      nodeRoutes.zipWithIndex.foreach { case (nodeRoute, index) =>
        println(s"${index + 1}/${nodeRoutes.size}")
        repo.delete(nodeRoute.id, nodeRoute.scopedNetworkType)
      }
    }
    println("Done")
  }
}