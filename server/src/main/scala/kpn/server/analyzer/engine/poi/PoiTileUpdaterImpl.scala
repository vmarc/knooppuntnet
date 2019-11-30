package kpn.server.analyzer.engine.poi

import kpn.server.repository.TaskRepository
import org.springframework.stereotype.Component

@Component
class PoiTileUpdaterImpl(taskRepository: TaskRepository) extends PoiTileUpdater {
  override def update(): Unit = {
    val tasks = taskRepository.all("poi-tile-task:")

  }

}
