package kpn.server.repository

import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.core.doc.BaseNodeDoc
import kpn.core.doc.NodeDoc
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileId

trait NodeRepository {

  def allNodeIds(): Seq[Long]

  def activeNodeIds(): Seq[Long]

  def activeBaseNodeIds(): Seq[Long]

  def saveBaseNode(baseNode: BaseNodeDoc): Unit

  def save(node: NodeDoc): Unit

  def bulkSaveBaseNodes(baseNodeDocs: Seq[BaseNodeDoc]): Unit

  def bulkSave(nodes: NodeDoc*): Unit

  def delete(nodeId: Long): Unit

  def nodeWithId(nodeId: Long): Option[NodeDoc]

  def baseNodeWithId(nodeId: Long): Option[BaseNodeDoc]

  def baseNodesWithIds(nodeIds: Seq[Long]): Seq[BaseNodeDoc]

  def nodesWithIds(nodeIds: Seq[Long]): Seq[NodeDoc]

  def activeNodesWithIds(nodeIds: Seq[Long]): Seq[NodeDoc]

  def nodeRouteReferences(nodeId: Long): Seq[Reference]

  def filterKnown(nodeIds: Set[Long]): Set[Long]

  def tileIds(routeType: RouteType): Seq[TileId]

  def tileInfosByZoomLevel(routeType: RouteType, zoomLevel: Int): Seq[NodeTileInfo]

  def tileInfosByTile(routeType: RouteType, tileId: TileId): Seq[NodeTileInfo]
}
