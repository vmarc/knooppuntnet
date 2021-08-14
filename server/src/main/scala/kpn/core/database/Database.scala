package kpn.core.database

import kpn.core.database.doc.CouchDoc
import kpn.core.database.query.Query

trait Database {

  def save[T](doc: CouchDoc): Unit

  def bulkSave[T](docs: Seq[CouchDoc]): Unit

  def deleteDocWithId(docId: String): Unit

  def deleteDocsWithIds(docIds: Seq[String]): Unit

  def docWithId[T](docId: String, docType: Class[T]): Option[T]

  def docsWithIds[T](docIds: Seq[String], docType: Class[T]): T

  def allIds(): Seq[String]

  def keysWithIds(docIds: Seq[String]): Seq[String]

  def revision(docId: String): Option[String]

  def execute[T](query: Query[T]): T

  def post[T](query: Query[T], body: String, docType: Class[T]): T

}
