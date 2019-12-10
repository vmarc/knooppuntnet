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

  def deleteNodePoi(nodeId: Long): Unit = {
    knownPoiCache.deleteNode(nodeId)
    poiRepository.poi("node", nodeId) foreach { poi =>
      poiRepository.delete("node", nodeId)
      poi.tiles.foreach { tileName =>
        taskRepository.add(PoiTileTask.withTileName(tileName))
      }
      val tileNameString = poi.tiles.mkString(", ")
      log.info(s"removed poi node $nodeId (tile(s): $tileNameString)")
    }
  }

  def deleteWayPoi(wayId: Long): Unit = {
    knownPoiCache.deleteWay(wayId)
    poiRepository.poi("way", wayId) foreach { poi =>
      poiRepository.delete("way", wayId)
      poi.tiles.foreach { tileName =>
        taskRepository.add(PoiTileTask.withTileName(tileName))
      }
      val tileNameString = poi.tiles.mkString(", ")
      log.info(s"removed poi way $wayId (tile(s): $tileNameString)")
    }
  }

  def deleteRelationPoi(relationId: Long): Unit = {
    knownPoiCache.deleteRelation(relationId)
    poiRepository.poi("relation", relationId) foreach { poi =>
      poiRepository.delete("relation", relationId)
      poi.tiles.foreach { tileName =>
        taskRepository.add(PoiTileTask.withTileName(tileName))
      }
      val tileNameString = poi.tiles.mkString(", ")
      log.info(s"removed poi relation $relationId (tile(s): $tileNameString)")
    }
  }
}
