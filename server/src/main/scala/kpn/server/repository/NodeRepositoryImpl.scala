package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.node.NodeNetworkReference
import kpn.api.common.node.NodeOrphanRouteReference
import kpn.core.database.Database
import kpn.core.database.doc.NodeDoc
import kpn.core.database.views.analyzer.DocumentView
import kpn.core.database.views.analyzer.NodeNetworkReferenceView
import kpn.core.database.views.analyzer.NodeOrphanRouteReferenceView
import kpn.core.db.KeyPrefix
import kpn.core.db.NodeDocViewResult
import kpn.core.mongo.actions.base.MongoQueryIds
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpServerErrorException

@Component
class NodeRepositoryImpl(
  // old
  analysisDatabase: Database,
  // new
  mongoEnabled: Boolean,
  mongoDatabase: MongoDatabase
) extends NodeRepository {

  private val log = Log(classOf[NodeRepositoryImpl])

  override def allNodeIds(): Seq[Long] = {
    if (mongoEnabled) {
      new MongoQueryIds(mongoDatabase).execute("nodes")
    }
    else {
      DocumentView.allNodeIds(analysisDatabase)
    }
  }

  override def save(nodes: NodeInfo*): Boolean = {
    if (mongoEnabled) {
      // new MongoSave(mongoDatabase).execute("nodes", node)
      ??? // TODO MONGO
    }
    else {
      var result = false
      var retry = true
      var retryCount = 0

      while (retry && retryCount < 3) {
        try {
          result = doSave(nodes)
          retry = false
        }
        catch {
          case e: HttpServerErrorException =>
            if (e.getStatusCode.value() == 404) {
              retryCount = retryCount + 1
            }
            else {
              throw new IllegalStateException(e)
            }
        }
      }
      result
    }
  }

  private def doSave(nodes: Seq[NodeInfo]): Boolean = {
    log.debugElapsed {

      val nodeIds = nodes.map(node => docId(node.id))
      val nodeDocViewResult = analysisDatabase.docsWithIds(nodeIds, classOf[NodeDocViewResult], stale = false)
      val nodeDocs = nodeDocViewResult.rows.flatMap(_.doc)
      val nodeDocIds = nodeDocs.map(_.node.id)
      val (existingNodes, newNodes) = nodes.partition(node => nodeDocIds.contains(node.id))

      val updatedNodes = existingNodes.filter { node =>
        val fromDb = nodeDocs.find(doc => doc.node.id == node.id)
        if (fromDb.get.node != node) {
          //noinspection SideEffectsInMonadicTransformation
          log.debug("NODE CHANGED: before=" + fromDb.get.node + ", after=" + node)
          true
        }
        else {
          false
        }
      }

      val newDocs = newNodes.map(node => NodeDoc(docId(node.id), node, None))

      val updateDocs = updatedNodes.map { node =>
        val fromDb = nodeDocs.find(doc => doc.node.id == node.id)
        val rev = fromDb.get._rev
        NodeDoc(docId(node.id), node, rev)
      }

      val docs = newDocs ++ updateDocs

      if (newNodes.nonEmpty) {
        log.info("Adding new node docs " + newNodes.map(_.id).mkString(","))
      }
      if (updatedNodes.nonEmpty) {
        log.info("Udating node docs " + updatedNodes.map(_.id).mkString(","))
      }

      if (docs.nonEmpty) {
        val groupSize = 50
        docs.sliding(groupSize, groupSize).toSeq.foreach { docsGroup =>
          analysisDatabase.bulkSave(docsGroup)
        }
      }

      (s"save ${nodes.size} nodes (new=${newDocs.size}, updated=${updateDocs.size})", docs.nonEmpty)
    }
  }

  override def delete(nodeId: Long): Unit = {
    if (mongoEnabled) {
      ??? // TODO MONGO
    }
    else {
      analysisDatabase.deleteDocWithId(docId(nodeId))
    }
  }

  override def nodeWithId(nodeId: Long): Option[NodeInfo] = {
    if (mongoEnabled) {
      ??? // TODO MONGO
    }
    else {
      analysisDatabase.docWithId(docId(nodeId), classOf[NodeDoc]).map(_.node)
    }
  }

  override def nodesWithIds(nodeIds: Seq[Long], stale: Boolean): Seq[NodeInfo] = {
    if (mongoEnabled) {
      ??? // TODO MONGO
    }
    else {
      val ids = nodeIds.map(id => docId(id))
      val nodeDocViewResult = analysisDatabase.docsWithIds(ids, classOf[NodeDocViewResult], stale)
      nodeDocViewResult.rows.flatMap(r => r.doc.map(_.node))
    }
  }

  override def nodeNetworkReferences(nodeId: Long, stale: Boolean = true): Seq[NodeNetworkReference] = {
    if (mongoEnabled) {
      ??? // TODO MONGO
    }
    else {
      NodeNetworkReferenceView.query(analysisDatabase, nodeId, stale)
    }
  }

  override def nodeOrphanRouteReferences(nodeId: Long, stale: Boolean = true): Seq[NodeOrphanRouteReference] = {
    if (mongoEnabled) {
      ??? // TODO MONGO
    }
    else {
      NodeOrphanRouteReferenceView.query(analysisDatabase, nodeId, stale)
    }
  }

  override def filterKnown(nodeIds: Set[Long]): Set[Long] = {
    if (mongoEnabled) {
      ??? // TODO MONGO
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

  private def docId(nodeId: Long): String = {
    s"${KeyPrefix.Node}:$nodeId"
  }
}
