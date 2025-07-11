package kpn.database.actions.changes

import kpn.api.custom.Timestamp
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetCount
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.database.actions.statistics.ChangeSetCounts

class MongoQueryChangeSetCountsTest extends MongoTest {

  test("execute") {

    change(1, 2020, 1, 1, happy = false)
    change(2, 2021, 1, 1, happy = false)
    change(3, 2021, 1, 2, happy = false)
    change(4, 2021, 1, 3, happy = true)
    change(5, 2021, 2, 1, happy = false)

    val query = new MongoQueryChangeSetCounts(database)

    assertEqual(
      query.execute(None, 2021, None),
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

    assertEqual(
      query.execute(None, 2021, Some(1)),
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

  private def change(replicationNumber: Int, year: Int, month: Int, day: Int, happy: Boolean): Unit = {
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
