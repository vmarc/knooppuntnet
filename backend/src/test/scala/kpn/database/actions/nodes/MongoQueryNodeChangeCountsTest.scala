package kpn.database.actions.nodes

import kpn.api.custom.Timestamp
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetCount
import kpn.core.test.TestObjects.newNodeChange
import kpn.database.actions.statistics.ChangeSetCounts

class MongoQueryNodeChangeCountsTest extends MongoTest {

  test("execute") {

    change(1, 1001, 2020, 1, 1, happy = false)
    change(2, 1001, 2021, 1, 1, happy = false)
    change(3, 1001, 2021, 1, 2, happy = false)
    change(4, 1001, 2021, 1, 3, happy = true)
    change(5, 1001, 2021, 2, 1, happy = false)
    change(6, 1002, 2021, 2, 1, happy = false)

    val query = new MongoQueryNodeChangeCounts(database)

    assertEqual(
      query.execute(1001L, 2021, None),
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
      query.execute(1001L, 2021, Some(1)),
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
    nodeId: Long,
    year: Int,
    month: Int,
    day: Int,
    happy: Boolean
  ): Unit = {
    database.nodeChanges.save(
      newNodeChange(
        key = newChangeKey(
          replicationNumber = replicationNumber,
          timestamp = Timestamp(year, month, day),
          elementId = nodeId
        ),
        happy = happy,
        impact = happy
      )
    )
  }
}
