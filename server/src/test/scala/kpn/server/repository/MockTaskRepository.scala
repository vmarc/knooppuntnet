package kpn.server.repository

class MockTaskRepository extends TaskRepository {

  var tasks: Seq[String] = Seq.empty

  override def add(task: String): Unit = {
    tasks = tasks :+ task
  }

  override def delete(task: String): Unit = {
    tasks = tasks.filterNot(_ == task)
  }

  override def exists(task: String): Boolean = {
    tasks.contains(task)
  }

  override def all(prefix: String): Seq[String] = {
    tasks.filter(_.startsWith(prefix))
  }
}
