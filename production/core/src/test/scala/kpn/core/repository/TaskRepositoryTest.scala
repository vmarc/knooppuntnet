package kpn.core.repository

import kpn.core.test.TestSupport.withDatabase
import org.scalatest.FunSuite
import org.scalatest.Matchers

class TaskRepositoryTest extends FunSuite with Matchers {

  test("add, list and delete tasks") {
    withRepository { repository =>
      repository.add("test1")
      repository.add("test2")
      repository.add("test3")

      repository.all("test") should equal(Seq("test1", "test2", "test3"))

      repository.exists("test1") should equal(true)
      repository.exists("test2") should equal(true)
      repository.exists("test3") should equal(true)
      repository.exists("test4") should equal(false)

      repository.delete("test1")
      repository.all("test") should equal(Seq("test2", "test3"))

      repository.delete("test2")
      repository.all("test") should equal(Seq("test3"))

      repository.delete("test3")
      repository.all("test") should equal(Seq())

    }
  }

  private def withRepository(f: TaskRepository => Unit): Unit = {
    withDatabase { database =>
      f(new TaskRepositoryImpl(database))
    }
  }
}
