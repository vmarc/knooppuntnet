package kpn.server.monitor.tasks

import com.mongodb.client.model.Aggregates.limit
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.Exit
import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorTask

import java.lang.Thread.sleep

object MonitorTaskLoopTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-monitor-2") { database =>
      new MonitorTaskLoopTool(database).taskProcessingLoop()
    }
  }
}

class MonitorTaskLoopTool(database: Database) {

  private val log = Log(classOf[MonitorTaskLoopTool])
  private var savedObserver: Option[MonitorTaskObserver] = None
  private var abort = false

  //noinspection LoopVariableNotUpdated
  def taskProcessingLoop(): Unit = {
    simulateExternalMonitorShutdown()
    log.info("start task processing loop")
    while (!abort) {
      processAllTasks()
      waitForNewTask()
    }

    log.info(s"end of task processing loop")
    System.exit(Exit.Success)
  }

  private def processAllTasks(): Unit = {
    log.info(s"processing pending tasks")
    while (processTask()) {}
  }

  private def processTask(): Boolean = {
    val pipeline = Seq(
      sort(orderBy(ascending("priority"), ascending("_id"))),
      limit(1)
    )
    val taskOption = database.monitorTasks.optionAggregate(pipeline, classOf[MonitorTask], log)
    taskOption match {
      case None => false // no more tasks to process
      case Some(task) =>
        log.info(s"  process task: ${task.message}")
        database.monitorTasks.deleteByObjectId(task._id)
        true // task done, can continue with next task
    }
  }

  private def waitForNewTask(): Unit = {
    val observer = new MonitorTaskObserver()
    savedObserver = Some(observer)
    try {
      ???
      // database.monitorTasks.native.watch().first().subscribe(observer)
      //      observer.await()
    }
    finally {
      savedObserver = None
    }
  }

  private def simulateExternalMonitorShutdown(): Unit = {
    new Thread() {
      override def run(): Unit = {
        sleep(60 * 1000)
        log.info("abort request after 1 minute")
        abort = true
        savedObserver match {
          case None => log.info("abort: no saved observer!")
          case Some(observer) => // observer.cancel()
        }
      }
    }.start()
  }
}
