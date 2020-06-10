package kpn.core.tools.analysis

import kpn.core.database.DatabaseImpl
import kpn.core.database.implementation.DatabaseContextImpl
import kpn.core.db.couch.Couch
import kpn.core.replicate.Oper
import kpn.core.tools.config._
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoApi
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoApiImpl
import kpn.server.json.Json
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetInfoRepositoryImpl
import kpn.server.repository.TaskRepository
import kpn.server.repository.TaskRepositoryImpl

object ChangeSetInfoTool {

  private val log = Log(classOf[ChangeSetInfoTool])

  def main(args: Array[String]): Unit = {

    val exit = ChangeSetInfoToolOptions.parse(args) match {
      case Some(options) =>

        try {

          val taskRepository = {
            val taskDatabase = new DatabaseImpl(DatabaseContextImpl(Couch.config, Json.objectMapper, options.tasksDatabaseName))
            new TaskRepositoryImpl(taskDatabase)
          }

          val changeSetInfoApi = new ChangeSetInfoApiImpl(Dirs().changeSets)

          val changeSetInfoRepository = {
            val changeDatabase = new DatabaseImpl(DatabaseContextImpl(Couch.config, Json.objectMapper, options.changeSetsDatabaseName))
            new ChangeSetInfoRepositoryImpl(changeDatabase)
          }

          new ChangeSetInfoTool(
            taskRepository,
            changeSetInfoApi,
            changeSetInfoRepository
          ).loop()
        }
        catch {
          case e: Throwable => log.fatal("Exception thrown during analysis", e)
        }
        finally {
          log.info(s"Stopped")
          ()
        }

        0

      case None =>
        // arguments are bad, error message will have been displayed
        -1
    }

    System.exit(exit)
  }
}

class ChangeSetInfoTool(
  taskRepository: TaskRepository,
  changeSetInfoApi: ChangeSetInfoApi,
  changeSetInfoRepository: ChangeSetInfoRepository
) {

  private val oper = new Oper()
  private val log = Log(classOf[ChangeSetInfoTool])

  def loop(): Unit = {
    while (oper.isActive) {
      val taskIds = taskRepository.all(TaskRepository.changeSetInfoTask)
      if (taskIds.isEmpty) {
        oper.sleep(30)
      }
      else {
        process(taskIds)
      }
    }
  }

  private def process(taskIds: Seq[String]): Unit = {

    log.debug(s"Processing ${taskIds.size} tasks")

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
