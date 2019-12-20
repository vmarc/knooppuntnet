package kpn.server.repository

import kpn.core.database.Database
import kpn.core.database.doc.StringValueDoc
import kpn.core.database.query.Query
import kpn.core.db.couch.ViewResult2
import org.springframework.stereotype.Component

@Component
class TaskRepositoryImpl(taskDatabase: Database) extends TaskRepository {

  override def add(id: String): Unit = {
    taskDatabase.revision(id) match {
      case None => taskDatabase.save(StringValueDoc(id, ""))
      case Some(revision) => // do nothing
    }
  }

  override def delete(id: String): Unit = {
    taskDatabase.deleteDocWithId(id)
  }

  override def exists(id: String): Boolean = {
    taskDatabase.revision(id).isDefined
  }

  override def all(prefix: String): Seq[String] = {
    val startKey = s""""$prefix""""
    val endKey = s""""${prefix}999999999999999""""
    val query = Query("_all_docs", classOf[ViewResult2]).startKey(startKey).endKey(endKey).reduce(false).stale(false)
    val result = taskDatabase.execute(query)
    result.rows.map(_.key)
  }
}
