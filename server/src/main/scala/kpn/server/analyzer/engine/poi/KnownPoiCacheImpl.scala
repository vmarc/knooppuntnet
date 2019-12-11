package kpn.server.analyzer.engine.poi

import javax.annotation.PostConstruct
import kpn.server.repository.PoiRepository
import org.springframework.stereotype.Component

@Component
class KnownPoiCacheImpl(poiRepository: PoiRepository) extends KnownPoiCache {

  private var knownPois = KnownPois()

  @PostConstruct
  def loadKnownPois(): Unit = {
    val poiInfos = poiRepository.allPois()
    val nodeIds = poiInfos.filter(_.elementType == "node").map(_.elementId).toSet
    val wayIds = poiInfos.filter(_.elementType == "way").map(_.elementId).toSet
    val relationIds = poiInfos.filter(_.elementType == "relation").map(_.elementId).toSet
    knownPois = KnownPois(nodeIds, wayIds, relationIds)
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

}
