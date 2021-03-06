package kpn.core.database

import kpn.core.database.doc.Doc
import kpn.core.database.query.Query

trait Database {

  def exists: Boolean

  def create(): Unit

  def delete(): Unit

  def save[T](doc: Doc): Unit

  def bulkSave[T](docs: Seq[Doc]): Unit

  def deleteDocWithId(docId: String): Unit

  def deleteDocsWithIds(docIds: Seq[String]): Unit

  def docWithId[T](docId: String, docType: Class[T]): Option[T]

  def docsWithIds[T](docIds: Seq[String], docType: Class[T], stale: Boolean = true): T

  def allIds(stale: Boolean = true): Seq[String]

  def keysWithIds(docIds: Seq[String], stale: Boolean = true): Seq[String]

  def revision(docId: String): Option[String]

  def execute[T](query: Query[T]): T

  def post[T](query: Query[T], body: String, docType: Class[T]): T

}
