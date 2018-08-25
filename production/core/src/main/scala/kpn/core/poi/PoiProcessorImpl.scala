package kpn.core.poi

import kpn.core.app.ActorSystemConfig
import kpn.core.db.couch.Couch
import kpn.core.db.couch.DatabaseImpl
import kpn.core.overpass.OverpassQueryExecutorImpl
import kpn.core.overpass.OverpassQueryExecutorWithThrotteling
import kpn.core.util.Log

object PoiProcessorImpl {

  def main(args: Array[String]): Unit = {
    val system = ActorSystemConfig.actorSystem()
    val couchConfig = Couch.config
    val couch = new Couch(system, couchConfig)
    val poiDatabase = new DatabaseImpl(couch, "pois")
    val poiRepository = new PoiRepositoryImpl(poiDatabase)
    val nonCachingExecutor = new OverpassQueryExecutorWithThrotteling(system, new OverpassQueryExecutorImpl())
    val poiLoader = new PoiLoaderImpl(nonCachingExecutor)
    new PoiProcessorImpl(poiLoader, poiRepository).process()
  }
}

class PoiProcessorImpl(
  poiLoader: PoiLoader,
  poiRepository: PoiRepository
) {

  private val log = Log(classOf[PoiProcessorImpl])

  def process(): Unit = {
    PoiConfiguration.poiDefinitionGroups.foreach { group =>
      group.definitions.foreach { poiDefinition =>
        val layer = poiDefinition.layerName
        poiDefinition.conditions.zipWithIndex.foreach { case (condition, index) =>
          Seq("node", "way", "relation").foreach { elementType =>
            log.info(s"Load pois $layer ${index + 1} $elementType")
            Log.context(s"$layer ${index + 1} $elementType") {
              val pois = poiLoader.load(elementType, layer, (index + 1).toString, condition)
              log.info(s"Saving ${pois.size} pois $layer ${index + 1} $elementType")
              pois.foreach { poi =>
                poiRepository.save(poi)
              }
            }
          }
        }
      }
    }
    log.info("Done")
  }
}
