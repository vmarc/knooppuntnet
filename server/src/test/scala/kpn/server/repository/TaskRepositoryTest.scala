package kpn.server.repository

import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class TaskRepositoryTest extends UnitTest {

  test("add, list and delete tasks") {
    withRepository { repository =>
      repository.add("test1")
      repository.add("test2")
      repository.add("test3")
      repository.add("testa")
      repository.add("testb")

      repository.all("test") should equal(Seq("test1", "test2", "test3", "testa", "testb"))

      assert(repository.exists("test1"))
      assert(repository.exists("test2"))
      assert(repository.exists("test3"))
      assert(repository.exists("testa"))
      assert(repository.exists("testb"))
      assert(!repository.exists("test4"))

      repository.delete("test1")
      repository.all("test") should equal(Seq("test2", "test3", "testa", "testb"))

      repository.delete("test2")
      repository.all("test") should equal(Seq("test3", "testa", "testb"))

      repository.delete("test3")
      repository.all("test") should equal(Seq("testa", "testb"))

    }
  }

  private def withRepository(f: TaskRepository => Unit): Unit = {
    withDatabase { database =>
      f(new TaskRepositoryImpl(database))
    }
  }
}
