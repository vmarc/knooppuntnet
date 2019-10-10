package kpn.core.tools

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.views.ViewRow
import kpn.core.repository.NodeRepositoryImpl
import kpn.core.repository.RouteRepositoryImpl
import spray.json.JsValue

/*
  Collects counts for each of the tags that are used in node and route definitions.
 */
object TagUsageTool {

  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Usage: TagUsageTool host masterDbName")
      System.exit(-1)
    }
    val host = args(0)
    val masterDbName = args(1)
    Couch.executeIn(host, masterDbName) { database =>
      new TagUsageTool(database).run()
    }
  }
}

class TagUsageTool(database: Database) {

  def run(): Unit = {
    println("Start")
    analyzeNodeTags()
    analyzeRouteTags()
    println("Done")
  }

  private def analyzeNodeTags(): Unit = {

    val counts = scala.collection.mutable.Map[String, Int]()

    val nodeIds = readNodeIds()
    println(s"Collecting tag information from ${nodeIds.size} node definitions")
    val repo = new NodeRepositoryImpl(database)
    nodeIds.zipWithIndex.foreach { case (nodeId, index) =>
      if ((index + 1) % 1000 == 0) {
        println(s"${index + 1}/${nodeIds.size}")
      }
      repo.nodeWithId(nodeId, Couch.uiTimeout).foreach { nodeInfo =>
        nodeInfo.tags.keys.foreach { key =>
          counts.put(key, counts.getOrElse(key, 0) + 1)
          if ("survey:date" == key) {
            println(s"node $nodeId ${nodeInfo.tags(key)}")
          }
        }
      }
    }
    counts.keys.toSeq.sorted.foreach { key =>
      println(s"$key\t${counts(key)}")
    }
  }

  private def analyzeRouteTags(): Unit = {
    val counts = scala.collection.mutable.Map[String, Int]()

    val routeIds = readRouteIds()
    println(s"Collecting tag information from ${routeIds.size} route definitions")

    val repo = new RouteRepositoryImpl(database)
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if ((index + 1) % 1000 == 0) {
        println(s"${index + 1}/${routeIds.size}")
      }
      repo.routeWithId(routeId, Couch.uiTimeout).foreach { routeInfo =>
        routeInfo.tags.keys.foreach { key =>
          counts.put(key, counts.getOrElse(key, 0) + 1)
          if ("survey:date" == key) {
            println(s"route $routeId ${routeInfo.tags(key)}")
          }
        }
      }
    }
    counts.keys.toSeq.sorted.foreach { key =>
      println(s"$key\t${counts(key)}")
    }
  }

  private def readNodeIds(): Seq[Long] = {
    def toNodeId(row: JsValue): Long = {
      val docId = new ViewRow(row).id.toString
      val nodeId = docId.drop("node:".length + 1).dropRight(1)
      nodeId.toLong
    }

    val request = """_design/AnalyzerDesign/_view/DocumentView?startkey="node"&endkey="node:a"&reduce=false&stale=ok"""
    database.getRows(request).map(toNodeId).distinct
  }

  private def readRouteIds(): Seq[Long] = {
    def toNodeId(row: JsValue): Long = {
      val docId = new ViewRow(row).id.toString
      val routeId = docId.drop("route:".length + 1).dropRight(1)
      routeId.toLong
    }

    val request = """_design/AnalyzerDesign/_view/DocumentView?startkey="route"&endkey="route:a"&reduce=false&stale=ok"""
    database.getRows(request).map(toNodeId).distinct
  }

}
