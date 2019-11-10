package kpn.server.repository

import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.api.common.changes.ChangeSetInfo
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

  test("all") {

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

      repository.all(Seq(changeSetId)) should equal(Seq(changeSetInfo))
    }
  }

  private def withRepository(f: ChangeSetInfoRepository => Unit): Unit = {
    withDatabase { database =>
      f(new ChangeSetInfoRepositoryImpl(database))
    }
  }
}
