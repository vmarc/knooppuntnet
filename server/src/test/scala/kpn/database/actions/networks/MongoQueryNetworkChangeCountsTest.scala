package kpn.database.actions.networks

import kpn.api.custom.Timestamp
import kpn.core.test.MongoTest
import kpn.database.actions.statistics.ChangeSetCounts

class MongoQueryNetworkChangeCountsTest extends MongoTest {

  test("execute") {

    change(1, 1, 2020, 1, 1, happy = false)
    change(2, 1, 2021, 1, 1, happy = false)
    change(3, 1, 2021, 1, 2, happy = false)
    change(4, 1, 2021, 1, 3, happy = true)
    change(5, 1, 2021, 2, 1, happy = false)
    change(6, 2, 2021, 2, 1, happy = false)

    val query = new MongoQueryNetworkChangeCounts(database)

    assertEqual(
      query.execute(1L, 2021, None),
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
      query.execute(1L, 2021, Some(1)),
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

  private def change(
    replicationNumber: Int,
    networkId: Long,
    year: Int,
    month: Int,
    day: Int,
    happy: Boolean
  ): Unit = {
    database.networkChanges.save(
      newNetworkChange(
        key = newChangeKey(
          replicationNumber = replicationNumber,
          timestamp = Timestamp(year, month, day),
          elementId = networkId
        ),
        happy = happy,
        impact = happy
      )
    )
  }
}
