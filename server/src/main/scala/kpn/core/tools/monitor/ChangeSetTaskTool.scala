package kpn.core.tools.monitor

import kpn.core.db.couch.Couch
import kpn.server.repository.TaskRepository
import kpn.server.repository.TaskRepositoryImpl

import java.io.File

object ChangeSetTaskTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "tasks-test") { database =>
      val taskRepository = new TaskRepositoryImpl(database)
      val files = new File("/kpn/wrk").list().filterNot(_ == "begin").toSeq.sorted
      files.foreach { changeSetId =>
        taskRepository.add(TaskRepository.changeSetInfoTask + changeSetId)
      }
    }
  }
}
