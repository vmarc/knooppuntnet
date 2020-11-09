package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.couch.Couch
import kpn.server.repository.NodeRepositoryImpl

object NodesSurveyDateTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "attic-analysis") { database =>
      new NodesSurveyDateTool(database).analyze()
    }
  }
}

class NodesSurveyDateTool(database: Database) {

  def analyze(): Unit = {

    val nodeRepository = new NodeRepositoryImpl(database)

    val counts = new scala.collection.mutable.HashMap[String, Seq[Long]]

    println(s"reading nodeIds")
    val nodeIds = DocumentView.allNodeIds(database) //.take(5)

    println(s"processing ${nodeIds.size} nodes")
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
