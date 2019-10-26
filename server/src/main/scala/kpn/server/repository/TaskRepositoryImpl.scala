package kpn.server.repository

import kpn.core.database.Database
import kpn.core.database.doc.StringValueDoc
import kpn.core.database.views.common.ViewRow
import kpn.core.db.couch.Couch
import org.springframework.stereotype.Component
import spray.http.Uri
import spray.json.JsString

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

    val startKey = {
      s""""$prefix""""
    }
    val endKey = {
      s""""${prefix}999999999999999""""
    }

    val uriHead = Uri("_all_docs")
    val uri = uriHead.withQuery(
      "startkey" -> startKey,
      "endkey" -> endKey,
      "include_docs" -> "false",
      "limit" -> "50",
      "reduce" -> "false"
    )

    val rows = taskDatabase.old.getRows(uri.toString(), Couch.defaultTimeout)
    rows.flatMap { row =>
      new ViewRow(row).key match {
        case JsString(key) => Some(key)
        case _ => None
      }
    }
  }
}
