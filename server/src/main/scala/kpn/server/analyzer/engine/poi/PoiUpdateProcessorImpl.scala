package kpn.server.analyzer.engine.poi

import kpn.api.common.Poi
import kpn.api.common.data.raw.RawElement
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.Tags
import kpn.core.poi.PoiConfiguration
import kpn.core.poi.PoiDefinition
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.tile.TileCalculator
import kpn.server.repository.PoiRepository
import kpn.server.repository.TaskRepository
import org.springframework.stereotype.Component

@Component
class PoiUpdateProcessorImpl(
  knownPoiCache: KnownPoiCache,
  poiRepository: PoiRepository,
  countryAnalyzer: CountryAnalyzer,
  tileCalculator: TileCalculator,
  taskRepository: TaskRepository
) extends PoiUpdateProcessor {

  private val log = Log(classOf[PoiUpdateProcessorImpl])

  def update(element: RawElement): Unit = {
    val matchingPoiDefinitions = findMatchingPoiDefinitions(element.tags)
    if (matchingPoiDefinitions.nonEmpty) {
      element match {
        case node: RawNode => processNodeElement(node, matchingPoiDefinitions)
        case way: RawWay => processWayElement(way, matchingPoiDefinitions)
        case relation: RawRelation => processRelationElement(relation, matchingPoiDefinitions)
      }
    }
  }

  private def processNodeElement(node: RawNode, poiDefinitions: Seq[PoiDefinition]): Unit = {

    if (isNodeInScope(node)) {

      val tiles = {
        val minLevel = poiDefinitions.map(_.minLevel).min
        (minLevel.toInt to ZoomLevel.vectorTileMaxZoom).map(z => tileCalculator.get(z, node.lon, node.lat))
      }

      val poi = Poi(
        "node",
        node.id,
        node.latitude,
        node.longitude,
        poiDefinitions.map(_.name),
        node.tags,
        tiles.map(_.name)
      )
      poiRepository.save(poi)

      tiles.foreach { tile =>
        taskRepository.add(PoiTileTask.withTile(tile))
      }

      knownPoiCache.add(PoiRef.of(node))
      log.info("added ")
    }
  }

  private def processWayElement(way: RawWay, poiDefinitions: Seq[PoiDefinition]): Unit = {
    if (isWayInScope(way)) {
      val poi: Poi = null
      poiRepository.save(poi)
      knownPoiCache.add(PoiRef.of(way))
    }
  }

  private def processRelationElement(relation: RawRelation, poiDefinitions: Seq[PoiDefinition]): Unit = {
    if (isRelationInScope(relation)) {
      val poi: Poi = null
      poiRepository.save(poi)
      knownPoiCache.add(PoiRef.of(relation))
    }
  }

  private def isNodeInScope(node: RawNode): Boolean = {
    knownPoiCache.contains(PoiRef.of(node)) || countryAnalyzer.countries(node).nonEmpty
  }

  private def isWayInScope(way: RawWay): Boolean = {
    true
  }

  private def isRelationInScope(relation: RawRelation): Boolean = {
    true
  }

  private def findMatchingPoiDefinitions(tags: Tags): Seq[PoiDefinition] = {
    PoiConfiguration.instance.groupDefinitions.flatMap(_.definitions).filter { poiDefinition =>
      poiDefinition.expression.evaluate(tags)
    }
  }

}
