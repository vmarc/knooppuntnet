package kpn.server.repository

import kpn.database.base.Database
import kpn.core.doc.Task
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class TaskRepositoryImpl(database: Database) extends TaskRepository {

  private val log = Log(classOf[TaskRepositoryImpl])

  override def add(id: String): Unit = {
    database.tasks.save(Task(id), log)
  }

  override def delete(id: String): Unit = {
    database.tasks.deleteByStringId(id, log)
  }

  override def exists(id: String): Boolean = {
    database.tasks.findByStringId(id, log).isDefined
  }

  override def all(prefix: String): Seq[String] = {
    database.tasks.findAll(log).filter(_._id.startsWith(prefix)).map(_._id)
  }
}
