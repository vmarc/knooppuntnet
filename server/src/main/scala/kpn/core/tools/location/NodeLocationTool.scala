package kpn.core.tools.location

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.views.ViewRow
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import kpn.server.analyzer.engine.analysis.location.NodeLocationAnalyzerImpl
import kpn.server.repository.NodeRepositoryImpl
import spray.json.JsValue

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

    val nodeIds = readNodeIds()
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

  private def readNodeIds(): Seq[Long] = {
    def toNodeId(row: JsValue): Long = {
      val docId = new ViewRow(row).id.toString
      val nodeId = docId.drop("node:".length + 1).dropRight(1)
      nodeId.toLong
    }

    val request = """_design/AnalyzerDesign/_view/DocumentView?startkey="node"&endkey="node:a"&reduce=false&stale=ok"""
    database.getRows(request).map(toNodeId).distinct.sorted
  }

}
