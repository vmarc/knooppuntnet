package kpn.server.analyzer.engine.changes

import scala.collection.concurrent.TrieMap

class ElementChangeMap {
  private val relationMap: scala.collection.concurrent.Map[Long, RawRelationChange] = TrieMap()

  def relationAdd(rawRelationChange: RawRelationChange): Unit = {
    relationMap.put(rawRelationChange.id, rawRelationChange)
  }

  def relationGet(id: Long): Option[RawRelationChange] = {
    relationMap.get(id)
  }
}
