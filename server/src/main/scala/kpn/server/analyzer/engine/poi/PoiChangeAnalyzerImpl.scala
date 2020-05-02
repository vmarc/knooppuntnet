package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLon
import kpn.api.common.Poi
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.data.raw.RawElement
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Tags
import kpn.core.poi.PoiConfiguration
import kpn.core.poi.PoiDefinition
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.analyzer.engine.tile.TileCalculator
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
        case Create => change.elements.foreach(processElement)
        case Modify => change.elements.foreach(processElement)
        case Delete => change.elements.foreach(element => deletePoi(PoiRef.of(element), "delete"))
      }
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
          case node: RawNode => processPoi(poiRef, node, node, matchingPoiDefinitions)
          case _ =>
            poiQueryExecutor.center(poiRef) match {
              case Some(center) => processPoi(poiRef, center, element, matchingPoiDefinitions)
              case _ => deletePoi(poiRef, "center not found")
            }
        }
      }
    }
  }

  private def processPoi(poiRef: PoiRef, center: LatLon, element: RawElement, poiDefinitions: Seq[PoiDefinition]): Unit = {
    if (!poiScopeAnalyzer.inScope(center)) {
      if (knownPoiCache.contains(poiRef)) {
        deletePoi(poiRef, "known poi no longer in scope")
      }
    }
    else {
      savePoi(poiRef, center, element.tags, poiDefinitions)
    }
  }

  private def deletePoi(poiRef: PoiRef, reason: String): Unit = {
    knownPoiCache.delete(poiRef)
    poiRepository.get(poiRef) foreach { poi =>
      poiRepository.delete(poiRef)
      poi.tiles.foreach(tileName => taskRepository.add(PoiTileTask.withTileName(tileName)))
      logPoi(poi, "remove", Some(reason))
    }
  }

  private def savePoi(poiRef: PoiRef, center: LatLon, tags: Tags, poiDefinitions: Seq[PoiDefinition]): Unit = {

    val oldTileNames = poiRepository.get(poiRef).toSeq.flatMap(_.tiles)
    val newTileNames = tileCalculator.poiTiles(center, poiDefinitions)
    val allTileNames = (oldTileNames ++ newTileNames).sorted.distinct

    val poi = Poi(
      poiRef.elementType,
      poiRef.elementId,
      center.latitude,
      center.longitude,
      poiDefinitions.map(_.name),
      tags,
      newTileNames
    )

    poiRepository.save(poi)
    allTileNames.foreach(tileName => taskRepository.add(PoiTileTask.withTileName(tileName)))
    knownPoiCache.add(poiRef)
    logPoi(poi, "add")
  }

  private def findMatchingPoiDefinitions(tags: Tags): Seq[PoiDefinition] = {
    PoiConfiguration.instance.groupDefinitions.flatMap(_.definitions).filter { poiDefinition =>
      poiDefinition.expression.evaluate(tags)
    }
  }

  private def logPoi(poi: Poi, action: String, reason: Option[String] = None): Unit = {
    val tileString = poi.tiles.mkString("+")
    val layerString = poi.layers.mkString("+")
    val reasonString = reason.map(string => s" [$string]").getOrElse("")
    log.info(s"$action poi ${poi.elementType}:${poi.elementId}$reasonString, layer(s)=$layerString, tile(s)=$tileString")
  }
}
