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
class NodeRepository(database: Database) {

  private val log = Log(classOf[NodeRepository])

  def allNodeIds(): Seq[Long] = {
    database.nodes.ids(log)
  }

  def activeNodeIds(): Seq[Long] = {
    new MongoQueryNodeIds(database).execute()
  }

  def activeBaseNodeIds(): Seq[Long] = {
    new MongoQueryBaseNodeIds(database).execute()
  }

  def saveBaseNode(baseNode: BaseNodeDoc): Unit = {
    database.baseNodes.save(baseNode)
  }

  def save(nodeDoc: NodeDoc): Unit = {
    database.nodes.save(nodeDoc)
  }

  def bulkSaveBaseNodes(baseNodeDocs: Seq[BaseNodeDoc]): Unit = {
    database.baseNodes.bulkSave(baseNodeDocs)
  }

  def bulkSave(nodeDocs: NodeDoc*): Unit = {
    database.nodes.bulkSave(nodeDocs)
  }

  def delete(nodeId: Long): Unit = {
    database.nodes.delete(nodeId, log)
  }

  def nodeWithId(nodeId: Long): Option[NodeDoc] = {
    database.nodes.findById(nodeId, log)
  }

  def nodesWithIds(nodeIds: Seq[Long]): Seq[NodeDoc] = {
    database.nodes.findByIds(nodeIds, log)
  }

  def activeNodesWithIds(nodeIds: Seq[Long]): Seq[NodeDoc] = {
    new MongoQueryNodes(database: Database).execute(nodeIds)
  }

  def baseNodeWithId(nodeId: Long): Option[BaseNodeDoc] = {
    database.baseNodes.findById(nodeId, log)
  }

  def baseNodesWithIds(nodeIds: Seq[Long]): Seq[BaseNodeDoc] = {
    database.baseNodes.findByIds(nodeIds, log)
  }

  def nodeRouteReferences(nodeId: Long): Seq[Reference] = {
    new MongoQueryNodeBaseRouteReferences(database).execute(nodeId)
  }

  def filterKnown(nodeIds: Set[Long]): Set[Long] = {
    new MongoQueryKnownNodeIds(database).execute(nodeIds.toSeq).toSet
  }

  def tileIds(routeType: RouteType): Seq[TileId] = {
    new MongoQueryNodeTileIds(database).execute(routeType)
  }

  def tileInfosByZoomLevel(routeType: RouteType, zoomLevel: Int): Seq[NodeTileInfo] = {
    new MongoQueryNodeTileInfos(database).byZoomLevel(routeType, zoomLevel)
  }

  def tileInfosByTileId(routeType: RouteType, tileId: TileId): Seq[NodeTileInfo] = {
    new MongoQueryNodeTileInfos(database).byTileId(routeType, tileId)
  }
}
