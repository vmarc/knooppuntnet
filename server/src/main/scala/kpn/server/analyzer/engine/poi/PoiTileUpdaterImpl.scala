package kpn.server.analyzer.engine.poi

import kpn.core.util.Log
import kpn.server.repository.TaskRepository
import org.springframework.stereotype.Component

@Component
class PoiTileUpdaterImpl(
  poiTileBuilder: PoiTileBuilder,
  taskRepository: TaskRepository
) extends PoiTileUpdater {

  override def update(): Unit = {
    val tasks = taskRepository.all(PoiTileTask.prefix)
    val tasksSize = tasks.size
    tasks.zipWithIndex.foreach { case (task, index) =>
      val tileName = PoiTileTask.tileName(task)
      Log.context(s"${index + 1}/$tasksSize $tileName") {
        poiTileBuilder.build(tileName)
        taskRepository.delete(task)
      }
    }
  }
}
