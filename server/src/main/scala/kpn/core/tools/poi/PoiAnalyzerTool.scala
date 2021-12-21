package kpn.core.tools.poi

import kpn.api.common.Poi
import kpn.api.common.location.Location
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.poi.PoiConfiguration
import kpn.core.poi.PoiDefinition
import kpn.core.poi.PoiLoader
import kpn.core.poi.PoiLoaderImpl
import kpn.core.poi.PoiLocation
import kpn.core.poi.tags.TagExpressionFormatter
import kpn.core.util.Log
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzer
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerImpl
import kpn.server.analyzer.engine.poi.PoiScopeAnalyzer
import kpn.server.analyzer.engine.poi.PoiScopeAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculator
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl

object PoiAnalyzerTool {

  def main(args: Array[String]): Unit = {

    val exit = PoiAnalyzerToolOptions.parse(args) match {
      case Some(options) =>

        Mongo.executeIn(options.poiDatabaseName) { poiDatabase =>
          val poiLoader = {
            val overpassQueryExecutor = new OverpassQueryExecutorImpl()
            new PoiLoaderImpl(overpassQueryExecutor)
          }
          val poiRepository = new PoiRepositoryImpl(poiDatabase)
          val poiScopeAnalyzer = {
            val locationAnalyzer = new LocationAnalyzerImpl(true)
            new PoiScopeAnalyzerImpl(locationAnalyzer)
          }
          val tileCalculator: TileCalculator = new TileCalculatorImpl()
          val locationAnalyzer: LocationAnalyzer = new LocationAnalyzerImpl(true)
          val tool = new PoiAnalyzerTool(
            poiLoader,
            poiScopeAnalyzer,
            poiRepository,
            tileCalculator,
            locationAnalyzer
          )
          tool.analyze()
        }

        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }

    System.exit(exit)
  }
}

class PoiAnalyzerTool(
  poiLoader: PoiLoader,
  poiScopeAnalyzer: PoiScopeAnalyzer,
  poiRepository: PoiRepository,
  tileCalculator: TileCalculator,
  locationAnalyzer: LocationAnalyzer
) {

  private val log = Log(classOf[PoiAnalyzerTool])

  def analyze(): Unit = {
    PoiConfiguration.instance.groupDefinitions.foreach { group =>
      group.definitions.foreach { poiDefinition =>
        val layer = poiDefinition.name
        Seq("node", "way", "relation").foreach { elementType =>
          Log.context(s"$layer $elementType") {
            log.info(s"Load pois")
            PoiLocation.boundingBoxStrings.foreach { bbox =>
              val conditions = new TagExpressionFormatter().format(poiDefinition.expression)
              conditions.foreach { condition =>
                val pois = poiLoader.load(elementType, layer, bbox, condition)
                log.info(s"Saving ${pois.size} pois $layer $bbox $elementType")
                pois.foreach { poi =>
                  if (poiScopeAnalyzer.inScope(poi)) {
                    val poiDefinitions = findPoiDefinitions(poi)
                    val layers = poiDefinitions.map(_.name).distinct.sorted
                    if (layers.nonEmpty) {
                      val tileNames = tileCalculator.poiTiles(poi, poiDefinitions)
                      val location = Location(locationAnalyzer.findLocations(poi.latitude, poi.longitude))
                      poiRepository.save(
                        poi.copy(
                          layers = layers,
                          location = location,
                          tiles = tileNames
                        )
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  private def findPoiDefinitions(poi: Poi): Seq[PoiDefinition] = {
    val poiDefinitions = PoiConfiguration.instance.groupDefinitions.flatMap(_.definitions)
    poiDefinitions.filter(_.expression.evaluate(poi.tags))
  }
}
