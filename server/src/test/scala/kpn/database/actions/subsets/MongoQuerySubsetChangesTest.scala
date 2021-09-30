package kpn.database.actions.subsets

import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.database.base.Database
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQuerySubsetChangesTest extends UnitTest with SharedTestObjects {

  test("subset changes") {
    withDatabase { database =>

      changeSet(database, 1, Timestamp(2021, 8, 11), Subset.nlHiking, impact = true)
      changeSet(database, 2, Timestamp(2021, 8, 12), Subset.nlHiking, impact = true)
      changeSet(database, 3, Timestamp(2021, 8, 13), Subset.nlHiking, impact = false)
      changeSet(database, 4, Timestamp(2021, 8, 14), Subset.nlHiking, impact = false)

      query(database, Subset.nlHiking, ChangesParameters(Some("2021"))) should equal(Seq(4, 3, 2, 1))
      query(database, Subset.nlHiking, ChangesParameters(Some("2021"), impact = true)) should equal(Seq(2, 1))
    }
  }

  test("subset changes - subset") {
    withDatabase { database =>

      changeSet(database, 1, Timestamp(2021, 8, 11), Subset.nlHiking, impact = true)
      changeSet(database, 2, Timestamp(2021, 8, 12), Subset.nlHiking, impact = true)
      changeSet(database, 3, Timestamp(2021, 8, 13), Subset.nlBicycle, impact = true)
      changeSet(database, 4, Timestamp(2021, 8, 14), Subset.nlBicycle, impact = true)

      query(database, Subset.nlHiking, ChangesParameters(Some("2021"))) should equal(Seq(2, 1))
      query(database, Subset.nlBicycle, ChangesParameters(Some("2021"))) should equal(Seq(4, 3))
    }
  }

  test("subset changes - time") {
    withDatabase { database =>

      changeSet(database, 1, Timestamp(2021, 8, 11), Subset.nlHiking, impact = true)
      changeSet(database, 2, Timestamp(2021, 8, 12), Subset.nlHiking, impact = true)
      changeSet(database, 3, Timestamp(2021, 9, 13), Subset.nlHiking, impact = true)
      changeSet(database, 4, Timestamp(2021, 9, 14), Subset.nlHiking, impact = true)

      query(database, Subset.nlHiking, ChangesParameters(Some("2021"))) should equal(Seq(4, 3, 2, 1))
      query(database, Subset.nlHiking, ChangesParameters(Some("2021"), Some("08"))) should equal(Seq(2, 1))
      query(database, Subset.nlHiking, ChangesParameters(Some("2021"), Some("09"))) should equal(Seq(4, 3))
      query(database, Subset.nlHiking, ChangesParameters(Some("2021"), Some("08"), Some("11"))) should equal(Seq(1))
      query(database, Subset.nlHiking, ChangesParameters(Some("2021"), Some("08"), Some("12"))) should equal(Seq(2))
    }
  }

  private def changeSet(database: Database, changeSetId: Long, timestamp: Timestamp, subset: Subset, impact: Boolean): Unit = {
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

  private def query(database: Database, subset: Subset, parameters: ChangesParameters): Seq[Long] = {
    val changes = new MongoQuerySubsetChanges(database).execute(
      subset,
      parameters
    )
    changes.map(_.key.changeSetId)
  }
}
