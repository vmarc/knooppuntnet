package kpn.core.database.views.location

import kpn.api.custom.NetworkType
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
    NetworkType.all.foreach { networkType =>
      val nodeRoutes = repo.nodeRoutes(networkType)
      nodeRoutes.zipWithIndex.foreach { case (nodeRoute, index) =>
        println(s"${index + 1}/${nodeRoutes.size}")
        repo.delete(nodeRoute.id, nodeRoute.networkType)
      }
    }
    println("Done")
  }
}