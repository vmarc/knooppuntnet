package kpn.server.analyzer.analyzer

import kpn.server.analyzer.engine.AnalysisTimeRepositoryImpl
import org.scalatest.FunSuite
import org.scalatest.Matchers
import scalax.file.Path

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
    Path.fromString(repositoryFile).delete()
    val repo = new AnalysisTimeRepositoryImpl(repositoryFile)
    fn(repo)
  }
}
