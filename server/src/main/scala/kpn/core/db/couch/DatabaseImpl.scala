package kpn.core.db.couch

import kpn.core.db.Doc
import kpn.core.db.views.Design
import kpn.core.db.views.View

class DatabaseImpl(context: DatabaseContext) extends Database {

  override def exists: Boolean = {
    new DatabaseExists(context).exists
  }

  def create(): Unit = {
    new DatabaseCreate(context).create()
  }

  def delete(): Unit = {
    new DatabaseDelete(context).delete()
  }

  override def docWithId[T](docId: String, docType: Class[T]): Option[T] = {
    new DatabaseDocWithId(context).docWithId(docId, docType)
  }

  override def docsWithIds[T](docIds: Seq[String], docType: Class[T], stale: Boolean = true): T = {
    new DatabaseDocsWithIds(context).docsWithIds(docIds, docType, stale)
  }

  override def save[T](doc: Doc): Unit = {
    new DatabaseSave(context).save(doc)
  }

  override def revision(docId: String): Option[String] = {
    new DatabaseRevision(context).revision(docId)
  }

  override def query[T](design: Design, view: View, docType: Class[T], stale: Boolean = true)(args: Any*): T = {
    new DatabaseQuery(context).query(design, view, docType, stale)(args: _*)
  }

  override def keysWithIds(docIds: Seq[String], stale: Boolean): Seq[String] = {
    new DatabaseKeysWithIds(context).keysWithIds(docIds, stale)
  }
}
