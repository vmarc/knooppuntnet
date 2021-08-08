package kpn.server.repository

import kpn.api.common.common.Reference
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.database.views.analyzer.NodeNetworkReferenceView
import kpn.core.database.views.analyzer.NodeRouteReferenceView
import kpn.core.db.KeyPrefix
import kpn.core.mongo.Database
import kpn.core.mongo.actions.nodes.MongoQueryKnownNodeIds
import kpn.core.mongo.actions.nodes.MongoQueryNodeIds
import kpn.core.mongo.actions.nodes.MongoQueryNodeNetworkReferences
import kpn.core.mongo.doc.NodeDoc
import kpn.core.util.Log
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy
import org.springframework.stereotype.Component

@Component
class NodeRepositoryImpl(
  database: Database,
  // old
  analysisDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends NodeRepository {

  private val log = Log(classOf[NodeRepositoryImpl])

  override def allNodeIds(): Seq[Long] = {
    if (mongoEnabled) {
      database.nodes.ids(log)
    }
    else {
      DocumentView.allNodeIds(analysisDatabase)
    }
  }

  override def activeNodeIds(): Seq[Long] = {
    new MongoQueryNodeIds(database).execute()
  }

  override def save(nodeDoc: NodeDoc): Unit = {
    if (mongoEnabled) {
      database.nodes.save(nodeDoc)
    }
    else {
      // bulkSave(nodeDoc)
      throw new RuntimeException("not supported anymore")
    }
  }

  override def bulkSave(nodeDocs: NodeDoc*): Unit = {
    if (mongoEnabled) {
      // https://docs.mongodb.com/manual/core/bulk-write-operations/
      nodeDocs.foreach { nodeInfo =>
        database.nodes.save(nodeInfo)
      }
    }
    else {
      throw new RuntimeException("not supported anymore")
      //      log.debugElapsed {
      //
      //        val nodeIds = nodeDocs.map(nodeInfo => docId(nodeInfo.id))
      //        val nodeDocViewResult = analysisDatabase.docsWithIds(nodeIds, classOf[NodeDocViewResult], stale = false)
      //        val nodeDocs = nodeDocViewResult.rows.flatMap(_.doc)
      //        val nodeDocIds = nodeDocs.map(_.node.id)
      //        val (existingNodes, newNodes) = nodeDocs.partition(node => nodeDocIds.contains(node.id))
      //
      //        val updatedNodes = existingNodes.filter { node =>
      //          val fromDb = nodeDocs.find(doc => doc.node.id == node.id)
      //          if (fromDb.get.node != node) {
      //            //noinspection SideEffectsInMonadicTransformation
      //            log.debug("NODE CHANGED: before=" + fromDb.get.node + ", after=" + node)
      //            true
      //          }
      //          else {
      //            false
      //          }
      //        }
      //
      //        val newDocs = newNodes.map(node => kpn.core.database.doc.CouchNodeDoc(docId(node.id), node, None))
      //
      //        val updateDocs = updatedNodes.map { node =>
      //          val fromDb = nodeDocs.find(doc => doc.node.id == node.id)
      //          val rev = fromDb.get._rev
      //          kpn.core.database.doc.CouchNodeDoc(docId(node.id), node, rev)
      //        }
      //
      //        val docs = newDocs ++ updateDocs
      //
      //        if (newNodes.nonEmpty) {
      //          log.info("Adding new node docs " + newNodes.map(_.id).mkString(","))
      //        }
      //        if (updatedNodes.nonEmpty) {
      //          log.info("Udating node docs " + updatedNodes.map(_.id).mkString(","))
      //        }
      //
      //        if (docs.nonEmpty) {
      //          val groupSize = 50
      //          docs.sliding(groupSize, groupSize).toSeq.foreach { docsGroup =>
      //            analysisDatabase.bulkSave(docsGroup)
      //          }
      //        }
      //
      //        (s"save ${nodeDocs.size} nodes (new=${newDocs.size}, updated=${updateDocs.size})", ())
      //      }
    }
  }

  override def delete(nodeId: Long): Unit = {
    if (mongoEnabled) {
      database.nodes.delete(nodeId, log)
    }
    else {
      analysisDatabase.deleteDocWithId(docId(nodeId))
    }
  }

  override def nodeWithId(nodeId: Long): Option[NodeDoc] = {
    if (mongoEnabled) {
      database.nodes.findById(nodeId, log)
    }
    else {
      // analysisDatabase.docWithId(docId(nodeId), classOf[kpn.core.database.doc.CouchNodeDoc]).map(_.node)
      throw new RuntimeException("not supported anymore")
    }
  }

  override def nodesWithIds(nodeIds: Seq[Long], stale: Boolean): Seq[NodeDoc] = {
    if (mongoEnabled) {
      database.nodes.findByIds(nodeIds, log)
    }
    else {
      //  val ids = nodeIds.map(id => docId(id))
      //  val nodeDocViewResult = analysisDatabase.docsWithIds(ids, classOf[NodeDocViewResult], stale)
      //  nodeDocViewResult.rows.flatMap(r => r.doc.map(_.node))
      Seq.empty
    }
  }

  override def nodeNetworkReferences(nodeId: Long, stale: Boolean = true): Seq[Reference] = {
    if (mongoEnabled) {
      new MongoQueryNodeNetworkReferences(database).execute(nodeId)
    }
    else {
      NodeNetworkReferenceView.query(analysisDatabase, nodeId, stale)
    }
  }

  override def nodeRouteReferences(nodeId: Long, stale: Boolean = true): Seq[Reference] = {
    if (mongoEnabled) {
      database.routes.aggregate[Reference](routeReferencesPipeline(nodeId))
    }
    else {
      NodeRouteReferenceView.query(analysisDatabase, nodeId, stale)
    }
  }

  override def filterKnown(nodeIds: Set[Long]): Set[Long] = {
    if (mongoEnabled) {
      new MongoQueryKnownNodeIds(database).execute(nodeIds.toSeq).toSet
    }
    else {
      log.debugElapsed {
        val existingNodeIds = nodeIds.sliding(50, 50).flatMap { nodeIdsSubset =>
          val nodeDocIds = nodeIdsSubset.map(docId).toSeq
          val existingNodeDocIds = analysisDatabase.keysWithIds(nodeDocIds)
          existingNodeDocIds.flatMap { nodeDocId =>
            try {
              Some(java.lang.Long.parseLong(nodeDocId.substring(KeyPrefix.Node.length + 1)))
            }
            catch {
              case e: NumberFormatException => None
            }
          }
        }.toSet
        (s"${existingNodeIds.size}/${nodeIds.size} existing nodes", existingNodeIds)
      }
    }
  }

  private def routeReferencesPipeline(nodeId: Long): Seq[Bson] = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("nodeRefs", nodeId)
        )
      ),
      project(
        fields(
          excludeId(),
          computed("networkType", "$summary.networkType"),
          computed("networkScope", "$summary.networkScope"),
          computed("id", "$summary.id"),
          computed("name", "$summary.name")
        )
      ),
      sort(orderBy(ascending("networkType", "networkScope", "routeName")))
    )
  }

  private def docId(nodeId: Long): String = {
    s"${KeyPrefix.Node}:$nodeId"
  }
}
