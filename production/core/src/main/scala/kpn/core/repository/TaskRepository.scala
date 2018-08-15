package kpn.core.repository

object TaskRepository {
  val changeSetInfoTask = "change-set-info-task:"
}

trait TaskRepository {

  def add(key: String): Unit

  def delete(key: String): Unit

  def exists(id: String): Boolean

  def all(prefix: String): Seq[String]

}
