package kpn.core.tools

import kpn.core.db.couch.Couch
import kpn.server.repository.ChangeSetRepositoryImpl
import kpn.server.repository.TaskRepository
import kpn.server.repository.TaskRepositoryImpl

/*
  Creates tasks to retrieve changeset info's for all changesets in the database.
*/
object ChangeSetTaskTool {

  def main(args: Array[String]): Unit = {

    if (args.length < 2) {
      println("Usage: ChangeSetTaskTool host tasksDbName changesDbName")
      System.exit(-1)
    }
    val host = args(0)
    val taskDatabaseName = args(1)
    val changeDatabaseName = args(2)

    Couch.executeIn(host, taskDatabaseName) { taskDatabase =>
      Couch.executeIn(host, changeDatabaseName) { changeDatabase =>
        val changeSetRepository = new ChangeSetRepositoryImpl(changeDatabase)
        val taskRepository = new TaskRepositoryImpl(taskDatabase)
        changeSetRepository.allChangeSetIds().foreach { changeSetId =>
          taskRepository.add(TaskRepository.changeSetInfoTask + changeSetId)
        }
      }
      println("Ready")
    }
  }
}
