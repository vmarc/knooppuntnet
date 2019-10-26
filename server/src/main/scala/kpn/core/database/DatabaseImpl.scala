package kpn.core.database

import kpn.core.database.doc.Doc
import kpn.core.database.implementation._
import kpn.core.db.couch.{OldDatabase, OldDatabaseImpl}
import kpn.core.database.views.common.{Design, View}

class DatabaseImpl(context: DatabaseContext) extends Database {

  override val old: OldDatabase = {
    new OldDatabaseImpl(context.tempCouch, context.databaseName)
  }

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

  override def bulkSave[T](docs: Seq[Doc]): Unit = {
    new DatabaseBulkSave(context).bulkSave[T](docs)
  }

  override def deleteDocWithId(docId: String): Unit = {
    revision(docId) match {
      case Some(rev) => new DatabaseDeleteDocWithId(context).deleteDocWithId(docId, rev)
      case _ => // ignore, do not have to delete document because it does not exist
    }
  }

  override def deleteDocsWithIds(docIds: Seq[String]): Unit = {
    new DatabaseDeleteDocsWithIds(context).deleteDocsWithIds(docIds)
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
