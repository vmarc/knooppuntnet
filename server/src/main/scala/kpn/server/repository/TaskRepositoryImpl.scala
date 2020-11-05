package kpn.server.repository

import kpn.core.database.Database
import kpn.core.database.doc.StringValueDoc
import kpn.core.database.query.Query
import kpn.core.db.couch.ViewResult2
import org.springframework.stereotype.Component

@Component
class TaskRepositoryImpl(taskDatabase: Database) extends TaskRepository {

  override def add(id: String): Unit = {
    taskDatabase.save(StringValueDoc(id, ""))
  }

  override def delete(id: String): Unit = {
    taskDatabase.deleteDocWithId(id)
  }

  override def exists(id: String): Boolean = {
    taskDatabase.revision(id).isDefined
  }

  override def all(prefix: String): Seq[String] = {
    val startKey = s""""${prefix}""""
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
