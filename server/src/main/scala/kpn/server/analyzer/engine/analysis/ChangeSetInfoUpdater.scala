package kpn.server.analyzer.engine.analysis

import kpn.core.util.IdCache
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.TaskRepository
import org.springframework.stereotype.Component

@Component
class ChangeSetInfoUpdater(
  changeSetInfoRepository: ChangeSetInfoRepository,
  taskRepository: TaskRepository
) {

  private val cache = new IdCache()

  def changeSetInfo(changeSetId: Long): Unit = {

    if (cache.contains(changeSetId)) {
      // the changeset info is already in the database
    }
    else {
      cache.put(changeSetId)
      if (changeSetInfoRepository.exists(changeSetId)) {
        // the changeset info is already in the database
      }
      else {
        val taskId = s"${TaskRepository.changeSetInfoTask}$changeSetId"
        if (taskRepository.exists(taskId)) {
          // there already is a request registered to fetch the changeset info from OSM API
        }
        else {
          taskRepository.add(s"${TaskRepository.changeSetInfoTask}$changeSetId")
        }
      }
    }
  }
}
