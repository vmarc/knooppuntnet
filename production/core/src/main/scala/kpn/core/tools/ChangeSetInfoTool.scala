package kpn.core.tools

import akka.io.IO
import akka.pattern.ask
import kpn.core.app.ActorSystemConfig
import kpn.core.changes.ChangeSetInfoApi
import kpn.core.changes.ChangeSetInfoApiImpl
import kpn.core.db.couch.Couch
import kpn.core.db.couch.DatabaseImpl
import kpn.core.repository.ChangeSetInfoRepository
import kpn.core.repository.ChangeSetInfoRepositoryImpl
import kpn.core.repository.TaskRepository
import kpn.core.repository.TaskRepositoryImpl
import kpn.core.tools.config._
import kpn.core.util.Log
import spray.can.Http
import spray.util.pimpFuture

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration.DurationInt

object ChangeSetInfoTool {

  def main(args: Array[String]): Unit = {

    val system = ActorSystemConfig.actorSystem()
    try {

      val couchConfig = Couch.config
      val couch = new Couch(system, couchConfig)

      val taskRepository = {
        val taskDatabase = new DatabaseImpl(couch, couchConfig.taskDbname)
        new TaskRepositoryImpl(taskDatabase)
      }

      val changeSetInfoApi = new ChangeSetInfoApiImpl(Dirs().changeSets, system)

      val changeSetInfoRepository = {
        val changeDatabase = new DatabaseImpl(couch, couchConfig.changeDbname)
        new ChangeSetInfoRepositoryImpl(changeDatabase)
      }

      new ChangeSetInfoTool(
        taskRepository,
        changeSetInfoApi,
        changeSetInfoRepository
      ).loop()
    }
    finally {
      IO(Http)(system).ask(Http.CloseAll)(15.second).await
      Await.result(system.terminate(), Duration.Inf)
      ()
    }
  }
}

class ChangeSetInfoTool(
  taskRepository: TaskRepository,
  changeSetInfoApi: ChangeSetInfoApi,
  changeSetInfoRepository: ChangeSetInfoRepository
) {

  private val log = Log(classOf[ChangeSetInfoTool])

  def loop(): Unit = {
    while (true) {
      val taskIds = taskRepository.all(TaskRepository.changeSetInfoTask)
      if (taskIds.isEmpty) {
        log.debug("Nothing to process, sleep")
        Thread.sleep(30000)
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
