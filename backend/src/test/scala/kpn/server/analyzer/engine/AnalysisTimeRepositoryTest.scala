package kpn.server.analyzer.engine

import kpn.core.util.UnitTest

import java.io.File

class AnalysisTimeRepositoryTest extends UnitTest {

  test("return None when no file exists") {
    withRepository { repo =>
      repo.get should equal(None)
    }
  }

  test("put time") {
    withRepository { repo =>
      repo.put("12:34")
      repo.get should equal(Some("12:34"))
    }
  }

  private def withRepository(fn: AnalysisTimeRepository => Unit): Unit = {
    val repositoryFile = "/tmp/repository"
    new File(repositoryFile).delete()
    val repo = new AnalysisTimeRepository(repositoryFile)
    fn(repo)
  }
}
