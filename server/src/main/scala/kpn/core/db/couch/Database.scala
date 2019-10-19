package kpn.core.db.couch

trait Database {

  def docWithId[T](id: String, docType: Class[T]): Option[T]

}
