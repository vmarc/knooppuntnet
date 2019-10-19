package kpn.core.db.couch

trait Database {

  def exists: Boolean

  def docWithId[T](id: String, docType: Class[T]): Option[T]

}
