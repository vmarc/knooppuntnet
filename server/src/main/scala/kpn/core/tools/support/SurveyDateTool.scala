package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.couch.Couch
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

object SurveyDateTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "analysis") { database =>
      new SurveyDateTool(database).analyzeNodes()
    }
  }
}

class SurveyDateTool(database: Database) {

  def analyze(): Unit = {

    val routeRepository = new RouteRepositoryImpl(null, database, false)

    val counts = new scala.collection.mutable.HashMap[String, Seq[Long]]

    val routeIds = DocumentView.allRouteIds(database) //.take(5)
    routeIds.foreach { routeId =>

      routeRepository.routeWithId(routeId) match {
        case Some(route) =>
          route.tags.tags.foreach { tag =>
            val key = tag.key.toLowerCase()
            if (key.contains("survey") || key.contains("check") || key.contains("source")) {
              val value = tag.key + "=" + tag.value
              println(value + " -> " + routeId)
              if (counts.contains(value)) {
                counts.put(value, counts(value) :+ routeId)
              }
              else {
                counts.put(value, Seq(routeId))
              }
            }
          }

        case None =>
      }
    }

    println("result")
    counts.keys.toSeq.sorted.foreach { key =>
      val routeIds = counts(key)
      println(key + "\t" + routeIds.size + "\t" + routeIds)
    }
  }

  def analyzeNodes(): Unit = {

    val nodeRepository = new NodeRepositoryImpl(null, database, false)

    val counts = new scala.collection.mutable.HashMap[String, Seq[Long]]

    val nodeIds = DocumentView.allNodeIds(database)
    nodeIds.zipWithIndex.foreach { case (nodeId, index) =>

      if (((index + 1) % 100) == 0) {
        println(s"${index + 1}/${nodeIds.size}")
      }

      nodeRepository.nodeWithId(nodeId) match {
        case Some(node) =>
          node.tags.tags.foreach { tag =>
            val key = tag.key.toLowerCase()
            if (key.contains("survey") || key.contains("check") || key.contains("source")) {
              val value = tag.key + "=" + tag.value
              println(value + " -> " + nodeId)
              if (counts.contains(value)) {
                counts.put(value, counts(value) :+ nodeId)
              }
              else {
                counts.put(value, Seq(nodeId))
              }
            }
          }

        case None =>
      }
    }

    println("result")
    counts.keys.toSeq.sorted.foreach { key =>
      val nodeIds = counts(key)
      println(key + "\t" + nodeIds.size + "\t" + nodeIds)
    }
  }
}
