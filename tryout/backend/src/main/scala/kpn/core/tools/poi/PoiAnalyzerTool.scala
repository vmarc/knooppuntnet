package kpn.core.tools.poi

import kpn.api.common.location.Location
import kpn.api.common.poi.Poi
import kpn.core.overpass.OverpassQueryExecutorLocalImpl
import kpn.core.poi.PoiConfiguration
import kpn.core.poi.PoiDefinition
import kpn.core.poi.PoiGroupDefinition
import kpn.core.poi.PoiLoader
import kpn.core.poi.PoiLocation
import kpn.core.poi.tags.TagExpressionFormatter
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Options
import kpn.database.base.Tool
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerImpl
import kpn.server.analyzer.engine.poi.PoiScopeAnalyzer
import kpn.server.analyzer.engine.tile.PoiTileCalculator
import kpn.server.api.analysis.pages.poi.MasterPoiAnalyzer
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl

object PoiAnalyzerTool extends Tool[PoiAnalyzerToolOptions] {
  private val log = Log(classOf[PoiAnalyzerTool])

  override def options: Options[PoiAnalyzerToolOptions] = PoiAnalyzerToolOptions

  override def execute(options: PoiAnalyzerToolOptions): Unit = {
    Mongo.executeIn(options.poiDatabaseName) { database =>
      val tool = buildTool(options, database)
      tool.analyze()
    }
  }

  private def buildTool(options: PoiAnalyzerToolOptions, database: Database): PoiAnalyzerTool = {
    val poiLoader = {
      val overpassQueryExecutor = new OverpassQueryExecutorLocalImpl()
      new PoiLoader(overpassQueryExecutor)
    }
    val poiRepository = new PoiRepositoryImpl(database)
    val locationAnalyzer = new LocationAnalyzerImpl(true, false)
    val poiScopeAnalyzer = new PoiScopeAnalyzer(locationAnalyzer)
    val poiTileCalculator: PoiTileCalculator = new PoiTileCalculator()
    val masterPoiAnalyzer = new MasterPoiAnalyzer()
    new PoiAnalyzerTool(
      poiLoader,
      poiScopeAnalyzer,
      poiRepository,
      poiTileCalculator,
      locationAnalyzer,
      masterPoiAnalyzer
    )
  }
}

class PoiAnalyzerTool(
  poiLoader: PoiLoader,
  poiScopeAnalyzer: PoiScopeAnalyzer,
  poiRepository: PoiRepository,
  poiTileCalculator: PoiTileCalculator,
  locationAnalyzer: LocationAnalyzer,
  masterPoiAnalyzer: MasterPoiAnalyzer
) {

  private val log = Log(classOf[PoiAnalyzerTool])

  def analyze(): Unit = {
    PoiConfiguration.instance.groupDefinitions.foreach { group =>
      analyzeGroup(group)
    }
  }

  private def analyzeGroup(group: PoiGroupDefinition): Unit = {
    group.definitions.foreach { poiDefinition =>
      analyzePoi(poiDefinition)
    }
  }

  private def analyzePoi(poiDefinition: PoiDefinition): Unit = {
    val layer = poiDefinition.name
    Seq("node", "way", "relation").foreach { elementType =>
      Log.context(s"$layer $elementType") {
        log.info(s"Load pois")
        PoiLocation.boundingBoxStrings.foreach { bbox =>
          analyzePoiBbox(poiDefinition, layer, elementType, bbox)
        }
      }
    }
  }

  private def analyzePoiBbox(poiDefinition: PoiDefinition, layer: String, elementType: String, bbox: String): Unit = {
    val conditions = new TagExpressionFormatter().format(poiDefinition.expression)
    conditions.foreach { condition =>
      val pois = poiLoader.load(elementType, layer, bbox, condition)
      log.info(s"Saving ${pois.size} pois $layer $bbox $elementType")
      pois.foreach { poi =>
        if (poiScopeAnalyzer.inScope(poi)) {
          analyzePoi(poi)
        }
      }
    }
  }

  private def analyzePoi(poi: Poi): Unit = {
    val poiDefinitions = findPoiDefinitions(poi)
    val layers = poiDefinitions.map(_.name).distinct.sorted
    if (layers.nonEmpty) {
      val poiAnalysisContext = masterPoiAnalyzer.analyze(poi)
      val tileNames = poiTileCalculator.poiTiles(poi, poiDefinitions)
      val location = Location(locationAnalyzer.findLocations(poi.latitude, poi.longitude))
      poiRepository.save(
        poi.copy(
          layers = layers,
          location = location,
          tiles = tileNames,
          description = poiAnalysisContext.analysis.nameDescription,
          address = poiAnalysisContext.analysis.address,
          link = poiAnalysisContext.analysis.hasLink,
          image = poiAnalysisContext.analysis.hasImage
        )
      )
    }
  }

  private def findPoiDefinitions(poi: Poi): Seq[PoiDefinition] = {
    val poiDefinitions = PoiConfiguration.instance.groupDefinitions.flatMap(_.definitions)
    poiDefinitions.filter(_.expression.evaluate(poi.tags))
  }
}
