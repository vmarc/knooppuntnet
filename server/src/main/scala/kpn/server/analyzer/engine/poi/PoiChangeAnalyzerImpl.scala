package kpn.server.analyzer.engine.poi

import javax.annotation.PostConstruct
import kpn.api.common.Poi
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.data.raw.RawElement
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.core.poi.KnownPois
import kpn.core.poi.PoiConfiguration
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.repository.PoiRepository
import org.springframework.stereotype.Component

@Component
class PoiChangeAnalyzerImpl(poiRepository: PoiRepository) extends PoiChangeAnalyzer {

  private val log = Log(classOf[PoiChangeAnalyzerImpl])
  var knownPois = KnownPois()

  @PostConstruct
  def loadKnownPois(): Unit = {
  }

  override def analyze(osmChange: OsmChange): Unit = {
    osmChange.actions.foreach { change =>
      change.action match {
        case Create => processElements(change.elements)
        case Modify => processElements(change.elements)
        case Delete => processDelete(change.elements)
      }
    }
  }

  private def processElements(elements: Seq[RawElement]): Unit = {
    elements.foreach { element =>
      val poiDefinitions = PoiConfiguration.instance.groupDefinitions.flatMap(_.definitions)
      val layers = poiDefinitions.filter { poiDefinition =>
        poiDefinition.expression.evaluate(element.tags)
      }.map(_.name)

      if (layers.nonEmpty) {
        element match {
          case node: RawNode => processNodeElement(node, layers)
          case way: RawWay => processWayElement(way, layers)
          case relation: RawRelation => processRelationElement(relation, layers)
        }
      }
    }
  }

  private def processNodeElement(node: RawNode, layers: Seq[String]): Unit = {
    if (isNodeInScope(node)) {
      // TODO calculate tiles
      val poi = Poi(
        "node",
        node.id,
        node.latitude,
        node.longitude,
        layers,
        node.tags
      )
      poiRepository.save(poi)
      // TODO add poi-tile tasks
      knownPois = knownPois.copy(nodeIds = knownPois.nodeIds + node.id)
      log.info("added ")
    }
  }

  private def processWayElement(way: RawWay, layers: Seq[String]): Unit = {
    if (isWayInScope(way)) {
      val poi: Poi = null
      poiRepository.save(poi)
      knownPois = knownPois.copy(wayIds = knownPois.wayIds + way.id)
    }
  }

  private def processRelationElement(relation: RawRelation, layers: Seq[String]): Unit = {
    if (isRelationInScope(relation)) {
      val poi: Poi = null
      poiRepository.save(poi)
      knownPois = knownPois.copy(relationIds = knownPois.relationIds + relation.id)
    }
  }

  private def processDelete(elements: Seq[RawElement]): Unit = {
    elements.foreach { element =>
      if (element.isNode) {
        if (knownPois.nodeIds.contains(element.id)) {
          poiRepository.delete("node", element.id)
          knownPois = knownPois.copy(nodeIds = knownPois.nodeIds - element.id)
          log.info("removed ")
        }
      }
      else if (element.isWay) {
        if (knownPois.wayIds.contains(element.id)) {
          poiRepository.delete("way", element.id)
          knownPois = knownPois.copy(wayIds = knownPois.wayIds - element.id)
        }
      }
      else if (element.isRelation) {
        if (knownPois.relationIds.contains(element.id)) {
          poiRepository.delete("relation", element.id)
          knownPois = knownPois.copy(relationIds = knownPois.relationIds - element.id)
        }
      }
    }
  }

  private def isNodeInScope(node: RawNode): Boolean = {
    true
  }

  private def isWayInScope(way: RawWay): Boolean = {
    true
  }

  private def isRelationInScope(relation: RawRelation): Boolean = {
    true
  }

}
