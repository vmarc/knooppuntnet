package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.db.couch.Couch
import kpn.server.repository.NodeRepositoryImpl

object NodeNameDateTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "attic-analysis") { database =>
      new NodeNameTool(database).analyze()
    }
  }
}

class NodeNameTool(database: Database) {

  def analyze(): Unit = {

    val nodeRepository = new NodeRepositoryImpl(null, database, false)

    println(s"Reading nodeIds")
    val nodeIds = DocumentView.allNodeIds(database) //.take(5)

    println(s"Processing ${nodeIds.size} nodes")
    nodeIds.zipWithIndex.foreach { case (nodeId, index) =>

      if (((index + 1) % 100) == 0) {
        println(s"${index + 1}/${nodeIds.size}")
      }

      nodeRepository.nodeWithId(nodeId) match {
        case None =>
        case Some(node) =>

          if (node.active) {
            node.tags("rwn:name") match {
              case None =>
                node.tags("rcn:name") match {
                  case None =>
                  case Some(name) =>
                    node.tags("rcn_ref") match {
                      case None =>
                      case Some(ref) =>
                        println(s"rcn $nodeId\t$ref\t$name")
                    }
                }
              case Some(name) =>
                node.tags("rwn_ref") match {
                  case None =>
                  case Some(ref) =>
                    println(s"rwn $nodeId\t$ref\t$name")
                }
            }
          }
      }
    }

    println("Done")
  }

}
