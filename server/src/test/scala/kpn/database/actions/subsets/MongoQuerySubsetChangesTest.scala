package kpn.database.actions.subsets

import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetSummary

class MongoQuerySubsetChangesTest extends MongoTest {

  test("subset changes") {
    changeSet(1, Timestamp(2021, 8, 11), Subset.nlHiking, impact = true)
    changeSet(2, Timestamp(2021, 8, 12), Subset.nlHiking, impact = true)
    changeSet(3, Timestamp(2021, 8, 13), Subset.nlHiking, impact = false)
    changeSet(4, Timestamp(2021, 8, 14), Subset.nlHiking, impact = false)

    query(Subset.nlHiking, ChangesParameters(Some(2021))) should equal(Seq(4, 3, 2, 1))
    query(Subset.nlHiking, ChangesParameters(Some(2021), impact = true)) should equal(Seq(2, 1))
  }

  test("subset changes - subset") {
    changeSet(1, Timestamp(2021, 8, 11), Subset.nlHiking, impact = true)
    changeSet(2, Timestamp(2021, 8, 12), Subset.nlHiking, impact = true)
    changeSet(3, Timestamp(2021, 8, 13), Subset.nlBicycle, impact = true)
    changeSet(4, Timestamp(2021, 8, 14), Subset.nlBicycle, impact = true)

    query(Subset.nlHiking, ChangesParameters(Some(2021))) should equal(Seq(2, 1))
    query(Subset.nlBicycle, ChangesParameters(Some(2021))) should equal(Seq(4, 3))
  }

  test("subset changes - time") {
    changeSet(1, Timestamp(2021, 8, 11), Subset.nlHiking, impact = true)
    changeSet(2, Timestamp(2021, 8, 12), Subset.nlHiking, impact = true)
    changeSet(3, Timestamp(2021, 9, 13), Subset.nlHiking, impact = true)
    changeSet(4, Timestamp(2021, 9, 14), Subset.nlHiking, impact = true)

    query(Subset.nlHiking, ChangesParameters(Some(2021))) should equal(Seq(4, 3, 2, 1))
    query(Subset.nlHiking, ChangesParameters(Some(2021), Some(8))) should equal(Seq(2, 1))
    query(Subset.nlHiking, ChangesParameters(Some(2021), Some(9))) should equal(Seq(4, 3))
    query(Subset.nlHiking, ChangesParameters(Some(2021), Some(8), Some(11))) should equal(Seq(1))
    query(Subset.nlHiking, ChangesParameters(Some(2021), Some(8), Some(12))) should equal(Seq(2))
  }

  private def changeSet(changeSetId: Long, timestamp: Timestamp, subset: Subset, impact: Boolean): Unit = {
    database.changes.save(
      newChangeSetSummary(
        key = newChangeKey(
          changeSetId = changeSetId,
          timestamp = timestamp
        ),
        subsets = Seq(subset),
        happy = impact
      )
    )
  }

  private def query(subset: Subset, parameters: ChangesParameters): Seq[Long] = {
    val changes = new MongoQuerySubsetChanges(database).execute(
      subset,
      parameters
    )
    changes.map(_.key.changeSetId)
  }
}
