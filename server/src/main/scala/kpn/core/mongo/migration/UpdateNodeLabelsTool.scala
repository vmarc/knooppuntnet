package kpn.core.mongo.migration

import kpn.core.mongo.Database
import kpn.core.mongo.DatabaseCollectionImpl
import kpn.core.mongo.NodeDoc
import kpn.core.mongo.migration.UpdateNodeLabelsTool.log
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log

object UpdateNodeLabelsTool {

  private val log = Log(classOf[UpdateNodeLabelsTool])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      new UpdateNodeLabelsTool(database).migrate()
    }
    log.info("Done")
  }
}

class UpdateNodeLabelsTool(database: Database) {

  val newNodes = new DatabaseCollectionImpl(database.getCollection[NodeDoc]("nodes-new"))

  def migrate(): Unit = {
    val allNodeIds = database.nodes.ids(log)
    val batchSize = 250
    allNodeIds.sliding(batchSize, batchSize).zipWithIndex.foreach { case (nodeIds, index) =>
      log.info(s"${index * batchSize}/${allNodeIds.size}")
      migrateNodes(nodeIds)
    }
  }

  private def migrateNodes(nodeIds: Seq[Long]): Unit = {
    val nodeDocs = database.nodes.findByIds(nodeIds)
    val migratedNodeDocs = nodeDocs.map { doc =>
      doc.copy(
        labels = doc.attributes,
        attributes = null
      )
    }
    newNodes.insertMany(migratedNodeDocs)
  }
}
