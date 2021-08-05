package kpn.server.analyzer.engine.poi

import javax.annotation.PostConstruct
import kpn.core.util.Log
import kpn.server.repository.PoiRepository
import org.springframework.stereotype.Component

@Component
class KnownPoiCacheImpl(poiRepository: PoiRepository, analyzerEnabled: Boolean) extends KnownPoiCache {

  private val log = Log(classOf[KnownPoiCacheImpl])
  private var knownPois = KnownPois()

  @PostConstruct
  def loadKnownPois(): Unit = {
    if (analyzerEnabled) {
      log.info("Loading known poi ids")
      val relationIds = loadRelationIds()
      val wayIds = loadWayIds()
      val nodeIds = loadNodeIds()
      knownPois = KnownPois(nodeIds, wayIds, relationIds)
    }
  }

  def contains(poiRef: PoiRef): Boolean = {
    poiRef.elementType match {
      case "node" => knownPois.nodeIds.contains(poiRef.elementId)
      case "way" => knownPois.wayIds.contains(poiRef.elementId)
      case "relation" => knownPois.relationIds.contains(poiRef.elementId)
    }
  }

  def add(poiRef: PoiRef): Unit = {
    knownPois = poiRef.elementType match {
      case "node" => knownPois.copy(nodeIds = knownPois.nodeIds + poiRef.elementId)
      case "way" => knownPois.copy(wayIds = knownPois.wayIds + poiRef.elementId)
      case "relation" => knownPois.copy(relationIds = knownPois.relationIds + poiRef.elementId)
    }
  }

  def delete(poiRef: PoiRef): Unit = {
    knownPois = poiRef.elementType match {
      case "node" => knownPois.copy(nodeIds = knownPois.nodeIds - poiRef.elementId)
      case "way" => knownPois.copy(wayIds = knownPois.wayIds - poiRef.elementId)
      case "relation" => knownPois.copy(relationIds = knownPois.relationIds - poiRef.elementId)
    }
  }

  private def loadRelationIds(): Set[Long] = {
    log.infoElapsed {
      val ids = poiRepository.relationIds(stale = false).toSet
      (s"Loaded ${ids.size} relation ids", ids)
    }
  }

  private def loadWayIds(): Set[Long] = {
    log.infoElapsed {
      val ids = poiRepository.wayIds(stale = false).toSet
      (s"Loaded ${ids.size} way ids", ids)
    }
  }

  private def loadNodeIds(): Set[Long] = {
    log.infoElapsed {
      val ids = poiRepository.nodeIds(stale = false).toSet
      (s"Loaded ${ids.size} node ids", ids)
    }
  }
}
