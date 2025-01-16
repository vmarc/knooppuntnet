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

  def nodeRouteReferences(nodeId: Long): Seq[Reference]

  def filterKnown(nodeIds: Set[Long]): Set[Long]

  def tiles(routeType: RouteType): Seq[TileId]

  def tilesWithName(routeType: RouteType, tileId: TileId): Seq[NodeTileInfo]

  def nodeTileInfoByrouteType(routeType: RouteType): Seq[NodeTileInfo]

  def nodeTileInfoById(nodeId: Long): Option[NodeTileInfo]
}
