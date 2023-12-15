package kpn.core.tools.poi

import kpn.api.common.location.Location
import kpn.api.common.poi.Poi
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
import kpn.server.analyzer.engine.tile.OldTileCalculator
import kpn.server.analyzer.engine.tile.OldTileCalculatorImpl
import kpn.server.api.analysis.pages.poi.MasterPoiAnalyzer
import kpn.server.api.analysis.pages.poi.MasterPoiAnalyzerImpl
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
          val locationAnalyzer = new LocationAnalyzerImpl(true)
          val poiScopeAnalyzer = new PoiScopeAnalyzerImpl(locationAnalyzer)
          val tileCalculator: OldTileCalculator = new OldTileCalculatorImpl()
          val masterPoiAnalyzer = new MasterPoiAnalyzerImpl()
          val tool = new PoiAnalyzerTool(
            poiLoader,
            poiScopeAnalyzer,
            poiRepository,
            tileCalculator,
            locationAnalyzer,
            masterPoiAnalyzer
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
  tileCalculator: OldTileCalculator,
  locationAnalyzer: LocationAnalyzer,
  masterPoiAnalyzer: MasterPoiAnalyzer
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

                      val tileNames = tileCalculator.poiTiles(poi, poiDefinitions)
                      val location = Location(locationAnalyzer.findLocations(poi.latitude, poi.longitude))

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

                      poiRepository.save(
                        poi.copy(
                          layers = layers,
                          location = location,
                          tiles = tileNames,
                          description = description,
                          address = address,
                          link = link,
                          image = image
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
