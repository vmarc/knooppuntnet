package kpn.core.repository

import kpn.core.db.json.JsonFormats._
import kpn.core.db.StringValueDoc
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.views.ViewRow
import spray.http.Uri
import spray.json.JsString

class TaskRepositoryImpl(database: Database) extends TaskRepository {

  override def add(id: String): Unit = {
    database.currentRevision(id, Couch.defaultTimeout) match {
      case None => database.save(id, stringValueDocFormat.write(StringValueDoc(id, "")))
      case Some(revision) => // do nothing
    }
  }

  override def delete(id: String): Unit = {
    database.delete(id)
  }

  override def exists(id: String): Boolean = {
    database.currentRevision(id, Couch.defaultTimeout).isDefined
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

    val rows = database.getRows(uri.toString(), Couch.defaultTimeout)
    rows.flatMap { row =>
      new ViewRow(row).key match {
        case JsString(key) => Some(key)
        case _ => None
      }
    }
  }
}
