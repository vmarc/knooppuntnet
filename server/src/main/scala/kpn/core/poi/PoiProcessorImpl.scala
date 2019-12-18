package kpn.core.poi

import kpn.api.common.Poi
import kpn.api.common.tiles.ZoomLevel
import kpn.core.db.couch.Couch
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.poi.tags.TagExpressionFormatter
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzerImpl
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.TileCalculator
import kpn.server.analyzer.engine.tile.TileCalculatorImpl
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl

object PoiProcessorImpl {

  def main(args: Array[String]): Unit = {
    Couch.executeIn("knooppuntnet.server", "pois4") { poiDatabase =>
      val poiLoader = {
        val nonCachingExecutor = new OverpassQueryExecutorImpl()
        new PoiLoaderImpl(nonCachingExecutor)
      }
      val poiRepository = new PoiRepositoryImpl(poiDatabase)
      val poiLocationFilter = {
        val analysisContext = new AnalysisContext()
        val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
        val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
        new PoiLocationFilterImpl(countryAnalyzer)
      }
      val tileCalculator: TileCalculator = new TileCalculatorImpl()
      val processor = new PoiProcessorImpl(
        poiLoader,
        poiRepository,
        poiLocationFilter,
        tileCalculator
      )
      processor.process()
    }
  }
}

class PoiProcessorImpl(
  poiLoader: PoiLoader,
  poiRepository: PoiRepository,
  poiLocationFilter: PoiLocationFilter,
  tileCalculator: TileCalculator
) {

  private val log = Log(classOf[PoiProcessorImpl])

  def process(): Unit = {
    PoiConfiguration.instance.groupDefinitions.foreach { group =>
      group.definitions.foreach { poiDefinition =>
        val layer = poiDefinition.name
        Seq("node", "way", "relation").foreach { elementType =>
          log.info(s"Load pois $layer $elementType")
          PoiLocation.boundingBoxStrings.foreach { bbox =>
            Log.context(s"$layer $elementType") {
              val condition = new TagExpressionFormatter().format(poiDefinition.expression)
              val pois = poiLoader.load(elementType, layer, bbox, condition)
              log.info(s"Saving ${pois.size} pois $layer $bbox $elementType")
              pois.foreach { poi =>
                if (poiLocationFilter.filter(poi)) {
                  val poiDefinitions = findPoiDefinitions(poi)
                  val layers = poiDefinitions.map(_.name).seq.distinct.sorted
                  if (layers.nonEmpty) {
                    val tileNames = {
                      val minLevel = poiDefinitions.map(_.minLevel).min
                      (minLevel.toInt to ZoomLevel.vectorTileMaxZoom).map(z => tileCalculator.tileLonLat(z, poi.lon, poi.lat))
                    }.map(_.name)
                    poiRepository.save(poi.copy(layers = layers, tiles=tileNames))
                  }
                }
              }
            }
          }
        }
      }
    }
    log.info("Done")
  }

  private def findPoiDefinitions(poi: Poi): Seq[PoiDefinition] = {
    val poiDefinitions = PoiConfiguration.instance.groupDefinitions.flatMap(_.definitions)
    poiDefinitions.filter(_.expression.evaluate(poi.tags))
  }

}
