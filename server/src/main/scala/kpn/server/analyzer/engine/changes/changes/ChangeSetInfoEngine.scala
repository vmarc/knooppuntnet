package kpn.server.analyzer.engine.changes.changes

import kpn.core.util.Log
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.TaskRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ChangeSetInfoEngine(
  changeSetInfoEngineEnabled: Boolean,
  taskRepository: TaskRepository,
  changeSetInfoRepository: ChangeSetInfoRepository,
  changeSetInfoApi: ChangeSetInfoApi
) {

  private val log = Log(classOf[ChangeSetInfoEngine])

  @Scheduled(initialDelay = 30 * 1000, fixedDelay = 30 * 1000)
  def process(): Unit = {
    if (changeSetInfoEngineEnabled) {
      val taskIds = taskRepository.all(TaskRepository.changeSetInfoTask)
      log.debug(s"Processing ${taskIds.size} tasks")
      processTasks(taskIds)
    }
  }

  private def processTasks(taskIds: Seq[String]): Unit = {
    taskIds.zipWithIndex.foreach { case (taskId, index) =>
      val changeSetId = taskIdToChangeSetId(taskId)
      log.debug(s"Index=$index, changesetId=$changeSetId")
      if (changeSetInfoRepository.exists(changeSetId)) {
        log.debug(s"Info for change set $changeSetId already in database")
        taskRepository.delete(taskId)
      }
      else {
        changeSetInfoApi.get(changeSetId) match {
          case None => log.debug(s"Could not retrieve info for change set $changeSetId from OSM API, continue with next")
          case Some(changeSetInfo) =>
            changeSetInfoRepository.save(changeSetInfo)
            taskRepository.delete(taskId)
        }
      }
    }
  }

  private def taskIdToChangeSetId(taskId: String): Long = {
    if (taskId.startsWith(TaskRepository.changeSetInfoTask)) {
      taskId.drop(TaskRepository.changeSetInfoTask.length).toLong // TODO make more safe ???
    }
    else {
      throw new RuntimeException("Unexpected taskId: " + taskId)
    }
  }
}
