package kpn.core.tools.location

import kpn.core.database.Database
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import kpn.server.analyzer.engine.analysis.location.NodeLocationAnalyzerImpl
import kpn.server.repository.NodeRepositoryImpl

/*
  Performs node location analysis for all nodes in the database.

  This is for testing purposes during development only.  This code can/should be
  removed once all location analysis logic is fully in place.
 */
object NodeLocationTool {

  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      println("Usage: NodeLocationTool host masterDbName")
      System.exit(-1)
    }
    val host = args(0)
    val masterDbName = args(1)

    Couch.executeIn(host, masterDbName) { database =>
      new NodeLocationTool(database).run()
    }
  }

}

class NodeLocationTool(database: Database) {

  def run(): Unit = {

    println("Start")

    val analyzer = {
      val configuration = new LocationConfigurationReader().read()
      new NodeLocationAnalyzerImpl(configuration)
    }

    val nodeIds = DocumentView.allNodeIds(database)
    println(s"Updating ${nodeIds.size} node definitions")
    val repo = new NodeRepositoryImpl(database)
    nodeIds.zipWithIndex.foreach { case (nodeId, index) =>
      if ((index + 1) % 100 == 0) {
        println(s"${index + 1}/${nodeIds.size}")
      }
      repo.nodeWithId(nodeId, Couch.uiTimeout).foreach { nodeInfo =>
        nodeInfo.location match {
          case None =>
            analyzer.locate(nodeInfo.latitude, nodeInfo.longitude) match {
              case Some(location) =>
                repo.save(nodeInfo.copy(location = Some(location)))
              case None =>
                println(s"Could not determine location of node $nodeId")
            }
          case _ =>
        }
      }
    }
  }

}
