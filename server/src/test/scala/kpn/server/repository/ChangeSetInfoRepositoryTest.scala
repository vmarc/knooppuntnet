package kpn.server.repository

import kpn.api.common.changes.ChangeSetInfo
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class ChangeSetInfoRepositoryTest extends UnitTest {

  test("OLD changeSetInfo not found") {
    withOldRepository { repository =>
      repository.get(0L) should equal(None)
    }
  }

  test("OLD changeSetInfo") {

    withOldRepository { repository =>

      val changeSetId = 11L

      val changeSetInfo = ChangeSetInfo(
        changeSetId,
        changeSetId,
        Timestamp(2015, 8, 11, 0, 0, 0),
        Some(Timestamp(2015, 8, 11, 0, 1, 0)),
        open = false,
        1,
        Tags.from("comment" -> "bla")
      )

      repository.save(changeSetInfo)

      repository.get(changeSetId).value should matchTo(changeSetInfo)
    }
  }

  test("OLD all") {

    withOldRepository { repository =>

      val changeSetId = 11L

      val changeSetInfo = ChangeSetInfo(
        changeSetId,
        changeSetId,
        Timestamp(2015, 8, 11, 0, 0, 0),
        Some(Timestamp(2015, 8, 11, 0, 1, 0)),
        open = false,
        1,
        Tags.from("comment" -> "bla")
      )

      repository.save(changeSetInfo)

      repository.all(Seq(changeSetId)) should matchTo(Seq(changeSetInfo))
    }
  }

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
        changeSetId,
        Timestamp(2015, 8, 11, 0, 0, 0),
        Some(Timestamp(2015, 8, 11, 0, 1, 0)),
        open = false,
        1,
        Tags.from("comment" -> "bla")
      )

      repository.save(changeSetInfo)

      repository.get(changeSetId).value should matchTo(changeSetInfo)
    }
  }

  test("all") {

    withRepository { repository =>

      val changeSetId = 11L

      val changeSetInfo = ChangeSetInfo(
        changeSetId,
        changeSetId,
        Timestamp(2015, 8, 11, 0, 0, 0),
        Some(Timestamp(2015, 8, 11, 0, 1, 0)),
        open = false,
        1,
        Tags.from("comment" -> "bla")
      )

      repository.save(changeSetInfo)

      repository.all(Seq(changeSetId)) should matchTo(Seq(changeSetInfo))
    }
  }

  private def withOldRepository(f: ChangeSetInfoRepository => Unit): Unit = {
    withCouchDatabase { database =>
      f(new ChangeSetInfoRepositoryImpl(null, database, false))
    }
  }

  private def withRepository(f: ChangeSetInfoRepository => Unit): Unit = {
    withDatabase { database =>
      f(new ChangeSetInfoRepositoryImpl(database, null, true))
    }
  }
}
