package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLon
import kpn.api.common.Poi
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.data.raw.RawElement
import kpn.api.common.data.raw.RawNode
import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.Tags
import kpn.core.poi.PoiConfiguration
import kpn.core.poi.PoiDefinition
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.analyzer.engine.tile.TileCalculator
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.repository.PoiRepository
import kpn.server.repository.TaskRepository
import org.springframework.stereotype.Component

@Component
class PoiChangeAnalyzerImpl(
  knownPoiCache: KnownPoiCache,
  poiRepository: PoiRepository,
  tileCalculator: TileCalculator,
  taskRepository: TaskRepository,
  poiScopeAnalyzer: PoiScopeAnalyzer,
  poiQueryExecutor: PoiQueryExecutor
) extends PoiChangeAnalyzer {

  private val log = Log(classOf[PoiChangeAnalyzerImpl])

  override def analyze(osmChange: OsmChange): Unit = {
    osmChange.actions.foreach { change =>
      change.action match {
        case Create => processElements(change.elements)
        case Modify => processElements(change.elements)
        case Delete => deleteElements(change.elements)
      }
    }
  }

  private def processElements(elements: Seq[RawElement]): Unit = {
    elements.foreach(processElement)
  }

  private def deleteElements(elements: Seq[RawElement]): Unit = {
    elements.foreach { element =>
      deletePoi(PoiRef.of(element), "delete")
    }
  }

  private def processElement(element: RawElement): Unit = {

    val matchingPoiDefinitions = findMatchingPoiDefinitions(element.tags)

    val poiRef = PoiRef.of(element)
    if (knownPoiCache.contains(poiRef) && matchingPoiDefinitions.isEmpty) {
      deletePoi(poiRef, "known poi lost poi tags")
    }
    else {
      if (matchingPoiDefinitions.nonEmpty) {
        element match {
          case node: RawNode => processNodeElement(node, matchingPoiDefinitions)
          case _ => processNonNodeElement(element, matchingPoiDefinitions)
        }
      }
    }
  }

  private def processNodeElement(node: RawNode, poiDefinitions: Seq[PoiDefinition]): Unit = {

    val poiRef = PoiRef.of(node)

    if (!poiScopeAnalyzer.inScope(node)) {
      if (knownPoiCache.contains(poiRef)) {
        deletePoi(poiRef, "known poi no longer in scope")
      }
    }
    else {
      savePoi(poiRef, node, node.tags, poiDefinitions)
    }
  }

  private def processNonNodeElement(element: RawElement, poiDefinitions: Seq[PoiDefinition]): Unit = {

    val poiRef = PoiRef.of(element)
    val center = poiQueryExecutor.center(poiRef)

    if (!poiScopeAnalyzer.inScope(center)) {
      if (knownPoiCache.contains(poiRef)) {
        deletePoi(poiRef, "known poi no longer in scope")
      }
    }
    else {
      savePoi(poiRef, center, element.tags, poiDefinitions)
    }
  }

  private def findMatchingPoiDefinitions(tags: Tags): Seq[PoiDefinition] = {
    PoiConfiguration.instance.groupDefinitions.flatMap(_.definitions).filter { poiDefinition =>
      poiDefinition.expression.evaluate(tags)
    }
  }

  private def tilesFor(poiDefinitions: Seq[PoiDefinition], latLon: LatLon): Seq[Tile] = {
    val minLevel = poiDefinitions.map(_.minLevel).min
    (minLevel.toInt to ZoomLevel.vectorTileMaxZoom).map(z => tileCalculator.tileLonLat(z, latLon.lon, latLon.lat))
  }

  private def deletePoi(poiRef: PoiRef, reason: String): Unit = {
    knownPoiCache.delete(poiRef)
    poiRepository.poi(poiRef) foreach { poi =>
      poiRepository.delete(poiRef)
      poi.tiles.foreach { tileName =>
        taskRepository.add(PoiTileTask.withTileName(tileName))
      }
      val tileNameString = poi.tiles.mkString(", ")
      log.info(s"removed poi ${poiRef.elementType} ${poiRef.elementId} [$reason] (tile(s): $tileNameString)")
    }
  }

  private def savePoi(poiRef: PoiRef, center: LatLon, tags: Tags, poiDefinitions: Seq[PoiDefinition]): Unit = {

    val tiles = tilesFor(poiDefinitions, center)

    val poi = Poi(
      poiRef.elementType,
      poiRef.elementId,
      center.latitude,
      center.longitude,
      poiDefinitions.map(_.name),
      tags,
      tiles.map(_.name)
    )
    poiRepository.save(poi)

    tiles.foreach { tile =>
      taskRepository.add(PoiTileTask.withTile(tile))
    }

    knownPoiCache.add(poiRef)
    log.info("added ")
  }

}
