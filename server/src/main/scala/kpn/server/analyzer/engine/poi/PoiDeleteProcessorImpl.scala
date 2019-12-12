package kpn.server.analyzer.engine.poi

import kpn.core.util.Log
import kpn.server.repository.PoiRepository
import kpn.server.repository.TaskRepository
import org.springframework.stereotype.Component

@Component
class PoiDeleteProcessorImpl(
  poiRepository: PoiRepository,
  knownPoiCache: KnownPoiCache,
  taskRepository: TaskRepository
) extends PoiDeleteProcessor {

  private val log = Log(classOf[PoiChangeAnalyzerImpl])

  def delete(poiRef: PoiRef): Unit = {
    knownPoiCache.delete(poiRef)
    poiRepository.poi(poiRef) foreach { poi =>
      poiRepository.delete(poiRef)
      poi.tiles.foreach { tileName =>
        taskRepository.add(PoiTileTask.withTileName(tileName))
      }
      val tileNameString = poi.tiles.mkString(", ")
      log.info(s"removed poi ${poiRef.elementType} ${poiRef.elementId} (tile(s): $tileNameString)")
    }
  }

}
