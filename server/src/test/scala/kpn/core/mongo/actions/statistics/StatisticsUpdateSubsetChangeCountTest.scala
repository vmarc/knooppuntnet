package kpn.core.mongo.actions.statistics

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class StatisticsUpdateSubsetChangeCountTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildChangeSetSummary(database, 1L, Seq(Subset.nlHiking))
      buildChangeSetSummary(database, 2L, Seq(Subset.nlHiking))
      buildChangeSetSummary(database, 3L, Seq(Subset.nlHiking))
      buildChangeSetSummary(database, 4L, Seq(Subset.nlBicycle))
      buildChangeSetSummary(database, 5L, Seq(Subset.nlBicycle))
      buildChangeSetSummary(database, 6L, Seq(Subset.beHiking))

      new StatisticsUpdateSubsetChangeCount(database).execute()

      val counts = new MongoQueryStatistics(database).execute()

      counts.size should equal(3)
      counts should contain(StatisticValue(Country.nl, NetworkType.hiking, "ChangeCount", 3))
      counts should contain(StatisticValue(Country.nl, NetworkType.cycling, "ChangeCount", 2))
      counts should contain(StatisticValue(Country.be, NetworkType.hiking, "ChangeCount", 1))
    }
  }

  private def buildChangeSetSummary(database: Database, changeSetId: Long, subsets: Seq[Subset]): Unit = {
    database.changeSetSummaries.save(
      newChangeSetSummary(
        newChangeKey(changeSetId = changeSetId),
        subsets
      )
    )
  }
}
