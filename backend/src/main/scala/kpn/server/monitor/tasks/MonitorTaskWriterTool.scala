package kpn.server.monitor.tasks

import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.monitor.domain.MonitorTask
import org.bson.types.ObjectId

object MonitorTaskWriterTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-monitor-2") { database =>
      new MonitorTaskWriterTool(database).writeTasks()
    }
  }
}

class MonitorTaskWriterTool(database: Database) {
  def writeTasks(): Unit = {
    database.monitorTasks.save(MonitorTask(ObjectId.get(), 1, "one"))
    database.monitorTasks.save(MonitorTask(ObjectId.get(), 2, "three"))
    database.monitorTasks.save(MonitorTask(ObjectId.get(), 1, "two"))
  }
}
