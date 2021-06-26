package kpn.server.repository

import kpn.core.database.doc.StringValueDoc
import kpn.core.database.query.Query
import kpn.core.db.couch.ViewResult2
import kpn.core.mongo.Database
import kpn.core.mongo.Task
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class TaskRepositoryImpl(
  database: Database,
  // old:
  taskDatabase: kpn.core.database.Database,
  mongoEnabled: Boolean
) extends TaskRepository {

  private val log = Log(classOf[TaskRepositoryImpl])

  override def add(id: String): Unit = {
    if (mongoEnabled) {
      database.tasks.save(Task(id), log)
    }
    else {
      taskDatabase.save(StringValueDoc(id, ""))
    }
  }

  override def delete(id: String): Unit = {
    if (mongoEnabled) {
      database.tasks.deleteByStringId(id, log)
    }
    else {
      taskDatabase.deleteDocWithId(id)
    }
  }

  override def exists(id: String): Boolean = {
    if (mongoEnabled) {
      database.tasks.findByStringId(id, log).isDefined
    }
    else {
      taskDatabase.revision(id).isDefined
    }
  }

  override def all(prefix: String): Seq[String] = {
    if (mongoEnabled) {
      database.tasks.findAll(log).filter(_._id.startsWith(prefix)).map(_._id)
    }
    else {
      val startKey = s""""$prefix""""
      val endKey = s""""${prefix}z""""
      val query = Query("_all_docs", classOf[ViewResult2])
        .startKey(startKey)
        .endKey(endKey)
        .reduce(false)
        .stale(false)
      val result = taskDatabase.execute(query)
      result.rows.map(_.key)
    }
  }
}
