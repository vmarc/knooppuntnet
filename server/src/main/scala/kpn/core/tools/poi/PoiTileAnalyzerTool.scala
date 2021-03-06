package kpn.core.tools.poi

import kpn.api.common.Poi
import kpn.core.db.couch.Couch
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.poi.PoiConfiguration
import kpn.core.poi.PoiDefinition
import kpn.core.poi.PoiLoader
import kpn.core.poi.PoiLoaderImpl
import kpn.core.poi.PoiLocation
import kpn.core.poi.tags.TagExpressionFormatter
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.poi.PoiScopeAnalyzer
import kpn.server.analyzer.engine.poi.PoiScopeAnalyzerImpl
import kpn.server.analyzer.engine.tile.TileCalculator
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl

object PoiTileAnalyzerTool {

  def main(args: Array[String]): Unit = {

    val exit = PoiTileAnalyzerToolOptions.parse(args) match {
      case Some(options) =>

        Couch.executeIn(options.host, options.poiDatabaseName) { poiDatabase =>
          val poiLoader = {
            val nonCachingExecutor = new OverpassQueryExecutorImpl()
            new PoiLoaderImpl(nonCachingExecutor)
          }
          val poiRepository = new PoiRepositoryImpl(null, poiDatabase, false)
          val poiScopeAnalyzer = {
            val analysisContext = new AnalysisContext()
            val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
            val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
            new PoiScopeAnalyzerImpl(countryAnalyzer)
          }
          val tileCalculator: TileCalculator = new TileCalculatorImpl()
          val tool = new PoiTileAnalyzerTool(
            poiLoader,
            poiScopeAnalyzer,
            poiRepository,
            tileCalculator
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

class PoiTileAnalyzerTool(
  poiLoader: PoiLoader,
  poiScopeAnalyzer: PoiScopeAnalyzer,
  poiRepository: PoiRepository,
  tileCalculator: TileCalculator
) {

  private val log = Log(classOf[PoiTileAnalyzerTool])

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
                      poiRepository.save(poi.copy(layers = layers, tiles = tileNames))
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
