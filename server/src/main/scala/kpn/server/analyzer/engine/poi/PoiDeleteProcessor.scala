package kpn.server.analyzer.engine.poi

trait PoiDeleteProcessor {

  def deleteNodePoi(nodeId: Long): Unit

  def deleteWayPoi(wayId: Long): Unit

  def deleteRelationPoi(relationId: Long): Unit
}
