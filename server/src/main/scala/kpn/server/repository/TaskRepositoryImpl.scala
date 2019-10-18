package kpn.server.repository

import kpn.core.db.json.JsonFormats._
import kpn.core.db.StringValueDoc
import kpn.core.db.couch.Couch
import kpn.core.db.couch.OldDatabase
import kpn.core.db.views.ViewRow
import org.springframework.stereotype.Component
import spray.http.Uri
import spray.json.JsString

@Component
class TaskRepositoryImpl(oldTaskDatabase: OldDatabase) extends TaskRepository {

  override def add(id: String): Unit = {
    oldTaskDatabase.currentRevision(id, Couch.defaultTimeout) match {
      case None => oldTaskDatabase.save(id, stringValueDocFormat.write(StringValueDoc(id, "")))
      case Some(revision) => // do nothing
    }
  }

  override def delete(id: String): Unit = {
    oldTaskDatabase.delete(id)
  }

  override def exists(id: String): Boolean = {
    oldTaskDatabase.currentRevision(id, Couch.defaultTimeout).isDefined
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

    val rows = oldTaskDatabase.getRows(uri.toString(), Couch.defaultTimeout)
    rows.flatMap { row =>
      new ViewRow(row).key match {
        case JsString(key) => Some(key)
        case _ => None
      }
    }
  }
}
