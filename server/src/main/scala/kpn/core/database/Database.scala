package kpn.core.database

import kpn.core.database.doc.CouchDoc
import kpn.core.database.query.Query

trait Database {

  def save[T](doc: CouchDoc): Unit

  def deleteDocWithId(docId: String): Unit

  def docWithId[T](docId: String, docType: Class[T]): Option[T]

  def execute[T](query: Query[T]): T

  def post[T](query: Query[T], body: String, docType: Class[T]): T

}
