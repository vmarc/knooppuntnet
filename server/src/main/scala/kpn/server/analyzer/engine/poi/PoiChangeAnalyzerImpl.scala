package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLon
import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.data.raw.RawElement
import kpn.api.common.data.raw.RawNode
import kpn.api.common.location.Location
import kpn.api.common.poi.Poi
import kpn.api.custom.Tags
import kpn.core.poi.PoiConfiguration
import kpn.core.poi.PoiDefinition
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.analyzer.engine.tile.TileCalculator
import kpn.server.api.analysis.pages.poi.MasterPoiAnalyzer
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
  poiQueryExecutor: PoiQueryExecutor,
  locationAnalyzer: LocationAnalyzer,
  masterPoiAnalyzer: MasterPoiAnalyzer
) extends PoiChangeAnalyzer {

  private val log = Log(classOf[PoiChangeAnalyzerImpl])

  override def analyze(osmChange: OsmChange): Unit = {

    val modifiedElements = modifiedElementsIn(osmChange)
    val poiChangeAnalyses = analyzePoiChanges(modifiedElements)

    val nodeBasedPoiAnalyses = withCenters("node", poiChangeAnalyses.filter(_.element.isNode))
    val wayBasedPoiAnalyses = withCenters("way", poiChangeAnalyses.filter(_.element.isWay))
    val relationBasedPoiAnalyses = withCenters("relation", poiChangeAnalyses.filter(_.element.isRelation))

    val analyses = nodeBasedPoiAnalyses ++ wayBasedPoiAnalyses ++ relationBasedPoiAnalyses
    analyses.foreach(processPoiChangeAnalysis)

    val deletedPois = osmChange.actions.filter(_.action == Delete).flatMap(_.elements)
    deletedPois.foreach(element => deletePoi(PoiRef.of(element), "delete"))

    log.info(s"Analyzed ${nodeBasedPoiAnalyses.size} poi nodes, ${wayBasedPoiAnalyses.size} poi ways, ${relationBasedPoiAnalyses.size} poi relations, ${deletedPois.size} poi deletes")
  }

  private def modifiedElementsIn(osmChange: OsmChange): Seq[RawElement] = {
    val elements = osmChange.actions.filter(action => action.action == Create || action.action == Modify).flatMap(_.elements)
    val elementCount = elements.size
    val nodeCount = elements.count(_.isNode)
    val wayCount = elements.count(_.isWay)
    val relationCount = elements.count(_.isRelation)
    log.info(s"$elementCount modified elements ($nodeCount nodes, $wayCount ways, $relationCount relations)")
    elements
  }

  private def analyzePoiChanges(elements: Seq[RawElement]): Seq[PoiChangeAnalysis] = {
    elements.flatMap { element =>
      val matchingPoiDefinitions = findMatchingPoiDefinitions(element.tags)
      val poiRef = PoiRef.of(element)
      if (knownPoiCache.contains(poiRef) && matchingPoiDefinitions.isEmpty) {
        deletePoi(poiRef, "known poi lost poi tags")
        None
      }
      else {
        if (matchingPoiDefinitions.nonEmpty) {
          Some(
            PoiChangeAnalysis(element, matchingPoiDefinitions)
          )
        }
        else {
          None
        }
      }
    }
  }

  private def withCenters(elementType: String, analyses: Seq[PoiChangeAnalysis]): Seq[PoiChangeAnalysis] = {
    if (analyses.nonEmpty) {
      if (elementType == "node") {
        analyses.map { analysis =>
          analysis.copy(center = analysis.element match {
            case node: RawNode => Some(node)
            case _ => None
          })
        }
      }
      else {
        val centers = poiQueryExecutor.centers(elementType, analyses.map(_.element.id))
        analyses.map { analysis =>
          centers.find(_.id == analysis.element.id) match {
            case Some(center) => analysis.copy(center = Some(center.latLon))
            case None => analysis
          }
        }
      }
    }
    else {
      Seq.empty
    }
  }

  private def processPoiChangeAnalysis(poiChangeAnalysis: PoiChangeAnalysis): Unit = {
    val poiRef = PoiRef.of(poiChangeAnalysis.element)
    poiChangeAnalysis.center match {
      case None => deletePoi(poiRef, "could not determine center")
      case Some(center) =>
        if (!poiScopeAnalyzer.inScope(center)) {
          if (knownPoiCache.contains(poiRef)) {
            deletePoi(poiRef, "known poi no longer in scope")
          }
        }
        else {
          savePoi(
            poiRef,
            center,
            poiChangeAnalysis.element.tags,
            poiChangeAnalysis.poiDefinitions
          )
        }
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

    val location = Location(locationAnalyzer.findLocations(center.latitude, center.longitude))

    val oldTileNames = poiRepository.get(poiRef).toSeq.flatMap(_.tiles)
    val newTileNames = tileCalculator.poiTiles(center, poiDefinitions)
    val allTileNames = (oldTileNames ++ newTileNames).sorted.distinct

    val poi = Poi(
      poiRef.toId,
      poiRef.elementType,
      poiRef.elementId,
      center.latitude,
      center.longitude,
      poiDefinitions.map(_.name),
      tags,
      location,
      newTileNames,
      None,
      None,
      link = false,
      image = false
    )

    val context = masterPoiAnalyzer.analyze(poi)

    val link = context.analysis.facebook.isDefined ||
      context.analysis.twitter.isDefined ||
      context.analysis.website.isDefined ||
      context.analysis.wikidata.isDefined ||
      context.analysis.wikipedia.isDefined ||
      context.analysis.molenDatabase.isDefined ||
      context.analysis.hollandscheMolenDatabase.isDefined ||
      context.analysis.onroerendErfgoed.isDefined

    val image = context.analysis.image.isDefined ||
      context.analysis.imageLink.isDefined ||
      context.analysis.imageThumbnail.isDefined ||
      context.analysis.mapillary.isDefined

    val description = context.analysis.name match {
      case Some(name) => Some(name)
      case None => context.analysis.description
    }

    val address = context.analysis.addressLine1 match {
      case None => context.analysis.addressLine2
      case Some(addressLine1) =>
        context.analysis.addressLine2 match {
          case Some(addressLine2) => Some(addressLine1 + ", " + addressLine2)
          case None => Some(addressLine1)
        }
    }

    val enrichedPoi = poi.copy(
      description = description,
      address = address,
      link = link,
      image = image
    )

    poiRepository.save(enrichedPoi)
    allTileNames.foreach(tileName => taskRepository.add(PoiTileTask.withTileName(tileName)))
    knownPoiCache.add(poiRef)
    logPoi(poi, "add/update")
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
