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
    val tasksDbName = args(1)
    val changesDbName = args(2)

    Couch.oldExecuteIn(host, changesDbName) { oldChangeDatabase =>
      Couch.oldExecuteIn(host, tasksDbName) { tasksDatabase =>
        val changeSetRepository = new ChangeSetRepositoryImpl(oldChangeDatabase)
        val tasksRepository = new TaskRepositoryImpl(tasksDatabase)
        changeSetRepository.allChangeSetIds().foreach { changeSetId =>
          tasksRepository.add(TaskRepository.changeSetInfoTask + changeSetId)
        }
      }
      println("Ready")
    }
  }
}
