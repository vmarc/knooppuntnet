package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.couch.Couch
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

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

    val nodeIds = DocumentView.allNodeIds(database)
    println(s"Collecting tag information from ${nodeIds.size} node definitions")
    val repo = new NodeRepositoryImpl(database)
    nodeIds.zipWithIndex.foreach { case (nodeId, index) =>
      if ((index + 1) % 1000 == 0) {
        println(s"${index + 1}/${nodeIds.size}")
      }
      repo.nodeWithId(nodeId).foreach { nodeInfo =>
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

    val routeIds = DocumentView.allRouteIds(database)
    println(s"Collecting tag information from ${routeIds.size} route definitions")

    val repo = new RouteRepositoryImpl(database)
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if ((index + 1) % 1000 == 0) {
        println(s"${index + 1}/${routeIds.size}")
      }
      repo.routeWithId(routeId).foreach { routeInfo =>
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
}
