package kpn.core.tools.support

import kpn.core.database.Database
import kpn.core.db.couch.Couch
import kpn.server.repository.NodeRepositoryImpl

object DoubleQuoteNodesTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "attic-analysis") { database =>
      new DoubleQuoteNodesTool(database).find()
    }
  }
}

class DoubleQuoteNodesTool(database: Database) {
  private val nodeRepository = new NodeRepositoryImpl(database, false, null)

  def find(): Unit = {
    val nodeIds = nodeRepository.allNodeIds()
    nodeIds.zipWithIndex.foreach { case (nodeId, index) =>
      if ((index % 100) == 0) {
        println(s"$index/${nodeIds.size}")
      }
      nodeRepository.nodeWithId(nodeId) match {
        case None =>
        case Some(node) =>
          if (node.name == """"""") {
            println("node " + nodeId)
          }
      }
    }
  }
}
