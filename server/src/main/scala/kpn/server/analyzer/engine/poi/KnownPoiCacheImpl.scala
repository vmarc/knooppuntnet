package kpn.server.analyzer.engine.poi

import javax.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class KnownPoiCacheImpl extends KnownPoiCache {

  private var knownPois = KnownPois()

  @PostConstruct
  def loadKnownPois(): Unit = {
  }

  def containsNode(nodeId: Long): Boolean = {
    knownPois.nodeIds.contains(nodeId)
  }

  def containsWay(wayId: Long): Boolean = {
    knownPois.wayIds.contains(wayId)
  }

  def containsRelation(relationId: Long): Boolean = {
    knownPois.relationIds.contains(relationId)
  }

  def addNode(nodeId: Long): Unit = {
    knownPois = knownPois.copy(nodeIds = knownPois.nodeIds + nodeId)
  }

  def addWay(wayId: Long): Unit = {
    knownPois = knownPois.copy(wayIds = knownPois.wayIds + wayId)
  }

  def addRelation(relationId: Long): Unit = {
    knownPois = knownPois.copy(relationIds = knownPois.relationIds + relationId)
  }

  def deleteNode(nodeId: Long): Unit = {
    knownPois = knownPois.copy(nodeIds = knownPois.nodeIds - nodeId)
  }

  def deleteWay(wayId: Long): Unit = {
    knownPois = knownPois.copy(wayIds = knownPois.wayIds - wayId)
  }

  def deleteRelation(relationId: Long): Unit = {
    knownPois = knownPois.copy(relationIds = knownPois.relationIds + relationId)
  }

}
