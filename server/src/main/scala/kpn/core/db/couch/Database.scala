package kpn.core.db.couch

trait Database {

  def name: String

  def exists: Boolean

  def create(): Unit

  def delete(): Unit

  def docWithId[T](id: String, docType: Class[T]): Option[T]

}
