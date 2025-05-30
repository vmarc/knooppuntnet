package kpn.server.repository

import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.core.doc.BaseNodeDoc
import kpn.core.doc.NodeDoc
import kpn.core.util.Log
import kpn.database.actions.nodes.MongoQueryBaseNodeIds
import kpn.database.actions.nodes.MongoQueryKnownNodeIds
import kpn.database.actions.nodes.MongoQueryNodeBaseRouteReferences
import kpn.database.actions.nodes.MongoQueryNodeIds
import kpn.database.actions.nodes.MongoQueryNodeTileIds
import kpn.database.actions.nodes.MongoQueryNodeTileInfos
import kpn.database.actions.nodes.MongoQueryNodes
import kpn.database.base.Database
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileId
import org.springframework.stereotype.Component

@Component
class NodeRepositoryImpl(database: Database) extends NodeRepository {

  private val log = Log(classOf[NodeRepositoryImpl])

  override def allNodeIds(): Seq[Long] = {
    database.nodes.ids(log)
  }

  override def activeNodeIds(): Seq[Long] = {
    new MongoQueryNodeIds(database).execute()
  }

  override def activeBaseNodeIds(): Seq[Long] = {
    new MongoQueryBaseNodeIds(database).execute()
  }

  override def saveBaseNode(baseNode: BaseNodeDoc): Unit = {
    database.baseNodes.save(baseNode)
  }

  override def save(nodeDoc: NodeDoc): Unit = {
    database.nodes.save(nodeDoc)
  }

  override def bulkSaveBaseNodes(baseNodeDocs: Seq[BaseNodeDoc]): Unit = {
    database.baseNodes.bulkSave(baseNodeDocs)
  }

  override def bulkSave(nodeDocs: NodeDoc*): Unit = {
    database.nodes.bulkSave(nodeDocs)
  }

  override def delete(nodeId: Long): Unit = {
    database.nodes.delete(nodeId, log)
  }

  override def nodeWithId(nodeId: Long): Option[NodeDoc] = {
    database.nodes.findById(nodeId, log)
  }

  override def nodesWithIds(nodeIds: Seq[Long]): Seq[NodeDoc] = {
    database.nodes.findByIds(nodeIds, log)
  }

  override def activeNodesWithIds(nodeIds: Seq[Long]): Seq[NodeDoc] = {
    new MongoQueryNodes(database: Database).execute(nodeIds)
  }

  override def baseNodeWithId(nodeId: Long): Option[BaseNodeDoc] = {
    database.baseNodes.findById(nodeId, log)
  }

  override def baseNodesWithIds(nodeIds: Seq[Long]): Seq[BaseNodeDoc] = {
    database.baseNodes.findByIds(nodeIds, log)
  }

  override def nodeRouteReferences(nodeId: Long): Seq[Reference] = {
    new MongoQueryNodeBaseRouteReferences(database).execute(nodeId)
  }

  override def filterKnown(nodeIds: Set[Long]): Set[Long] = {
    new MongoQueryKnownNodeIds(database).execute(nodeIds.toSeq).toSet
  }

  override def tileIds(routeType: RouteType): Seq[TileId] = {
    new MongoQueryNodeTileIds(database).execute(routeType)
  }

  override def tileInfosByZoomLevel(routeType: RouteType, zoomLevel: Int): Seq[NodeTileInfo] = {
    new MongoQueryNodeTileInfos(database).byZoomLevel(routeType, zoomLevel)
  }

  override def tileInfosByTile(routeType: RouteType, tileId: TileId): Seq[NodeTileInfo] = {
    new MongoQueryNodeTileInfos(database).byTileId(routeType, tileId)
  }
}
