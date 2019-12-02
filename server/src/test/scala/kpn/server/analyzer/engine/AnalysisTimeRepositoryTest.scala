package kpn.server.analyzer.engine

import java.io.File

import org.scalatest.FunSuite
import org.scalatest.Matchers

class AnalysisTimeRepositoryTest extends FunSuite with Matchers {

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

  private def withRepository(fn: (AnalysisTimeRepositoryImpl) => Unit): Unit = {
    val repositoryFile = "/tmp/repository"
    new File(repositoryFile).delete()
    val repo = new AnalysisTimeRepositoryImpl(repositoryFile)
    fn(repo)
  }
}
