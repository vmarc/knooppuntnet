package kpn.core.tools.support


import kpn.core.database.Database
import kpn.core.database.doc.CouchNodeDoc
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.couch.Couch

object UpdateOrphanNodesTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "analysis") { database =>
      new UpdateOrphanNodesTool(database).update()
    }
  }
}

class UpdateOrphanNodesTool(database: Database) {

  def update(): Unit = {

    println(s"Reading nodeIds")
    val nodeIds = DocumentView.allNodeIds(database)

    println(s"Processing ${nodeIds.size} nodes")
    nodeIds.zipWithIndex.foreach { case (nodeId, index) =>

      if (((index + 1) % 100) == 0) {
        println(s"${index + 1}/${nodeIds.size}")
      }

      val docId = s"node:$nodeId"
      database.docWithId(docId, classOf[CouchNodeDoc]) match {
        case None =>
        case Some(nodeDoc) =>
          if (nodeDoc.node.active && nodeDoc.node.orphan) {
            println(s"update node $nodeId")
            database.save(nodeDoc)
          }
      }
    }

    println("Done")
  }
}
