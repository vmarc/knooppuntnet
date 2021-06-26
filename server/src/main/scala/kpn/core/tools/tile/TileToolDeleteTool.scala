package kpn.core.tools.tile

import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.tile.TileTask
import kpn.server.repository.TaskRepositoryImpl

object TileToolDeleteTool {

  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "tasks-test") { database =>
      val repo = new TaskRepositoryImpl(null, database, false)
      val tasks = repo.all(TileTask.prefix)
      println(s"Deleting ${tasks.size} tile tasks")
      tasks.zipWithIndex.foreach { case (task, index) =>
        if ((index + 1) % 100 == 0) {
          println(s"${index + 1}/${tasks.size}")
        }
        repo.delete(task)
      }
      println("Done")
    }
  }
}
