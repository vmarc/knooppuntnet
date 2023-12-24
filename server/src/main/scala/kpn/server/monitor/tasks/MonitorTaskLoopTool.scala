package kpn.server.monitor.tasks

import kpn.api.base.ObjectId
import kpn.core.doc.NetworkInfoDoc
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorTask
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

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

  def taskProcessingLoop(): Unit = {
    simulateExternalMonitorShutdown()
    log.info("start task processing loop")
    do {
      processAllTasks()
      waitForNewTask()
    } while (abort == false)

    log.info(s"end of task processing loop")
    System.exit(0)
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
    val task = database.monitorTasks.optionAggregate[MonitorTask](pipeline, log)
    task match {
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
      database.monitorTasks.native.watch().first().subscribe(observer)
      observer.await()
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
          case Some(observer) => observer.cancel()
        }
      }
    }.start()
  }
}
