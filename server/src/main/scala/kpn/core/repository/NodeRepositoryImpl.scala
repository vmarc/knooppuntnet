package kpn.core.repository

import akka.util.Timeout
import kpn.core.db.KeyPrefix
import kpn.core.db.NodeDoc
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.json.JsonFormats.nodeDocFormat
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.NodeNetworkReferenceView
import kpn.core.db.views.NodeOrphanRouteReferenceView
import kpn.core.util.Log
import kpn.shared.NodeInfo
import kpn.shared.node.NodeNetworkReference
import kpn.shared.node.NodeOrphanRouteReference

class NodeRepositoryImpl(database: Database) extends NodeRepository {

  private val log = Log(classOf[NodeRepository])

  override def save(nodes: NodeInfo*): Boolean = {

    log.debugElapsed {
      val nodeIds = nodes.map(node => docId(node.id))
      val nodeDocs = database.objectsWithIds(nodeIds, Couch.batchTimeout, stale = false).map(jsValue => nodeDocFormat.read(jsValue))
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

      val newDocs = newNodes.map(node => nodeDocFormat.write(NodeDoc(docId(node.id), node, None)))

      val updateDocs = updatedNodes.map { node =>
        val fromDb = nodeDocs.find(doc => doc.node.id == node.id)
        val rev = fromDb.get._rev
        nodeDocFormat.write(NodeDoc(docId(node.id), node, rev))
      }

      val docs = newDocs ++ updateDocs

      if (newNodes.nonEmpty) {
        log.info("Adding new node docs " + newNodes.map(_.id).mkString(","))
      }
      if (updatedNodes.nonEmpty) {
        log.info("Udating node docs " + updatedNodes.map(_.id).mkString(","))
      }

      if (docs.nonEmpty) {
        database.bulkSave(docs)
      }

      (s"save ${nodes.size} nodes (new=${newDocs.size}, updated=${updateDocs.size})", docs.nonEmpty)
    }
  }

  override def delete(nodeId: Long): Unit = {
    database.delete(docId(nodeId))
  }

  override def nodeWithId(nodeId: Long, timeout: Timeout): Option[NodeInfo] = {
    database.optionGet(docId(nodeId), timeout).map(nodeDocFormat.read).map(_.node)
  }

  override def nodesWithIds(nodeIds: Seq[Long], timeout: Timeout, stale: Boolean): Seq[NodeInfo] = {
    val ids = nodeIds.map(id => docId(id))
    database.objectsWithIds(ids, timeout, stale).map(doc => nodeDocFormat.read(doc)).map(_.node)
  }

  override def nodeNetworkReferences(nodeId: Long, timeout: Timeout, stale: Boolean = true): Seq[NodeNetworkReference] = {
    database.query(AnalyzerDesign, NodeNetworkReferenceView, timeout, stale)(nodeId).map(NodeNetworkReferenceView.convert)
  }

  override def nodeOrphanRouteReferences(nodeId: Long, timeout: Timeout, stale: Boolean = true): Seq[NodeOrphanRouteReference] = {
    database.query(AnalyzerDesign, NodeOrphanRouteReferenceView, timeout, stale)(nodeId).map(NodeOrphanRouteReferenceView.convert)
  }

  override def filterKnown(nodeIds: Set[Long]): Set[Long] = {
    log.debugElapsed {
      val existingNodeIds = nodeIds.sliding(50, 50).flatMap { nodeIdsSubset =>
        val nodeDocIds = nodeIdsSubset.map(docId).toSeq
        val existingNodeDocIds = database.keysWithIds(nodeDocIds)
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

  private def docId(nodeId: Long): String = {
    s"${KeyPrefix.Node}:$nodeId"
  }
}
