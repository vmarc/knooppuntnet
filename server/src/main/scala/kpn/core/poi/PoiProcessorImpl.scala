package kpn.core.poi

import kpn.core.app.ActorSystemConfig
import kpn.core.changes.RelationAnalyzerImpl
import kpn.core.db.couch.Couch
import kpn.core.db.couch.DatabaseImpl
import kpn.core.engine.analysis.country.CountryAnalyzerImpl
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorWithThrotteling
import kpn.core.poi.tags.TagExpressionFormatter
import kpn.core.tools.analyzer.AnalysisContext
import kpn.core.util.Log
import kpn.server.repository.PoiRepository
import kpn.server.repository.PoiRepositoryImpl
import kpn.shared.Poi

object PoiProcessorImpl {

  def main(args: Array[String]): Unit = {
    val system = ActorSystemConfig.actorSystem()
    val couchConfig = Couch.config
    val couch = new Couch(system, couchConfig)
    val poiDatabase = new DatabaseImpl(couch, "pois3")
    val poiRepository = new PoiRepositoryImpl(poiDatabase)
    val nonCachingExecutor = new OverpassQueryExecutorWithThrotteling(system, new OverpassQueryExecutorImpl())
    val poiLoader = new PoiLoaderImpl(nonCachingExecutor)
    val analysisContext = new AnalysisContext()
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    val countryAnalyzer = new CountryAnalyzerImpl(relationAnalyzer)
    val poiLocationFilter = new PoiLocationFilterImpl(countryAnalyzer)
    val processor = new PoiProcessorImpl(
      poiLoader,
      poiRepository,
      poiLocationFilter
    )
    processor.process()
  }
}

class PoiProcessorImpl(
  poiLoader: PoiLoader,
  poiRepository: PoiRepository,
  poiLocationFilter: PoiLocationFilter
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
                  val allLayers = findLayers(poi)
                  if (allLayers.nonEmpty) {
                    poiRepository.save(poi.copy(layers = allLayers))
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

  private def findLayers(poi: Poi): Seq[String] = {
    val poiDefinitions = PoiConfiguration.instance.groupDefinitions.flatMap(_.definitions)
    poiDefinitions.filter(_.expression.evaluate(poi.tags)).map(_.name).seq.distinct.sorted
  }

}
