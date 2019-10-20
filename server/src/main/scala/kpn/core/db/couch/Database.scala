package kpn.core.db.couch

import kpn.core.db.Doc
import kpn.core.db.views.Design
import kpn.core.db.views.View

trait Database {

  def exists: Boolean

  def create(): Unit

  def delete(): Unit

  def save[T](doc: Doc): Unit

  def docWithId[T](docId: String, docType: Class[T]): Option[T]

  def docsWithIds[T](docIds: Seq[String], docType: Class[T], stale: Boolean = true): T

  def revision(docId: String): Option[String]

  def query[T](design: Design, view: View, docType: Class[T], stale: Boolean = true)(args: Any*): T

}
