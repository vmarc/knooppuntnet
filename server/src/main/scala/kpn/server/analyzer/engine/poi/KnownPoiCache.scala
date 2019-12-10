package kpn.server.analyzer.engine.poi

trait KnownPoiCache {

  def containsNode(nodeId: Long): Boolean

  def containsWay(wayId: Long): Boolean

  def containsRelation(relationId: Long): Boolean

  def addNode(nodeId: Long): Unit

  def addWay(wayId: Long): Unit

  def addRelation(relationId: Long): Unit

  def deleteNode(nodeId: Long): Unit

  def deleteWay(wayId: Long): Unit

  def deleteRelation(relationId: Long): Unit

}
