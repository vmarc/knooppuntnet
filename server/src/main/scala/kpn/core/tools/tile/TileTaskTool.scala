package kpn.core.tools.tile

import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.tile.TileTask
import kpn.server.repository.TaskRepositoryImpl

object TileTaskTool {

  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "tasks-test") { database =>
      val repo = new TaskRepositoryImpl(database)
      val tasks = repo.all(TileTask.prefix)
      val ridingTasks = tasks.filter(task => task.contains("riding"))
      println(ridingTasks.size)
      ridingTasks.foreach(println)
    }
  }

}
