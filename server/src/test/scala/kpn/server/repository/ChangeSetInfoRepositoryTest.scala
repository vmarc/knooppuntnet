package kpn.server.repository

import kpn.core.test.TestSupport.withDatabase
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeSetInfo
import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class ChangeSetInfoRepositoryTest extends FunSuite with Matchers {

  test("changeSetInfo not found") {
    withRepository { repository =>
      repository.get(0L) should equal(None)
    }
  }

  test("changeSetInfo") {

    withRepository { repository =>

      val changeSetId = 11L

      val changeSetInfo = ChangeSetInfo(
        changeSetId,
        Timestamp(2015, 8, 11, 0, 0, 0),
        Some(Timestamp(2015, 8, 11, 0, 1, 0)),
        open = false,
        1,
        Tags.from("comment" -> "bla")
      )

      repository.save(changeSetInfo)

      repository.get(changeSetId) should equal(Some(changeSetInfo))
    }
  }

  private def withRepository(f: ChangeSetInfoRepository => Unit): Unit = {
    withDatabase { database =>
      f(new ChangeSetInfoRepositoryImpl(database))
    }
  }
}
