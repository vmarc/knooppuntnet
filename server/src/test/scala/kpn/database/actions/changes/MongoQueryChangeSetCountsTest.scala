package kpn.database.actions.changes

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Timestamp
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.Database

class MongoQueryChangeSetCountsTest extends UnitTest with SharedTestObjects {

  test("execute") {

    withDatabase { database =>

      change(database, 1, 2020, 1, 1, happy = false)
      change(database, 2, 2021, 1, 1, happy = false)
      change(database, 3, 2021, 1, 2, happy = false)
      change(database, 4, 2021, 1, 3, happy = true)
      change(database, 5, 2021, 2, 1, happy = false)

      val query = new MongoQueryChangeSetCounts(database)

      query.execute(None, 2021, None) should matchTo(
        ChangeSetCounts(
          years = Seq(
            newChangeSetCount(2021)(1, 4),
            newChangeSetCount(2020)(0, 1),
          ),
          months = Seq(
            newChangeSetCount(2021, 2)(0, 1),
            newChangeSetCount(2021, 1)(1, 3),
          )
        )
      )

      query.execute(None, 2021, Some(1)) should matchTo(
        ChangeSetCounts(
          years = Seq(
            newChangeSetCount(2021)(1, 4),
            newChangeSetCount(2020)(0, 1),
          ),
          months = Seq(
            newChangeSetCount(2021, 2)(0, 1),
            newChangeSetCount(2021, 1)(1, 3),
          ),
          days = Seq(
            newChangeSetCount(2021, 1, 3)(1, 1),
            newChangeSetCount(2021, 1, 2)(0, 1),
            newChangeSetCount(2021, 1, 1)(0, 1),
          )
        )
      )
    }
  }

  private def change(database: Database, replicationNumber: Int, year: Int, month: Int, day: Int, happy: Boolean): Unit = {
    database.changes.save(
      newChangeSetSummary(
        key = newChangeKey(
          replicationNumber = replicationNumber,
          timestamp = Timestamp(year, month, day)
        ),
        happy = happy
      )
    )
  }
}
