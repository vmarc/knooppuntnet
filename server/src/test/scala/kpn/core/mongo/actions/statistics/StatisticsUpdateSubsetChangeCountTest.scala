package kpn.core.mongo.actions.statistics

import kpn.api.common.SharedTestObjects
import kpn.api.common.statistics.StatisticValue
import kpn.api.common.statistics.StatisticValues
import kpn.api.custom.Country.de
import kpn.api.custom.Country.nl
import kpn.api.custom.NetworkType.cycling
import kpn.api.custom.NetworkType.hiking
import kpn.api.custom.Subset
import kpn.api.custom.Subset.deHiking
import kpn.api.custom.Subset.nlBicycle
import kpn.api.custom.Subset.nlHiking
import kpn.core.mongo.Database
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class StatisticsUpdateSubsetChangeCountTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildChangeSetSummary(database, 1L, Seq(nlHiking))
      buildChangeSetSummary(database, 2L, Seq(nlHiking))
      buildChangeSetSummary(database, 3L, Seq(nlHiking))
      buildChangeSetSummary(database, 4L, Seq(nlBicycle))
      buildChangeSetSummary(database, 5L, Seq(nlBicycle))
      buildChangeSetSummary(database, 6L, Seq(deHiking))

      new StatisticsUpdateSubsetChangeCount(database).execute()

      val counts = new MongoQueryStatistics(database).execute()

      counts should equal(
        Seq(
          StatisticValues(
            "ChangeCount",
            Seq(
              StatisticValue(de, hiking, 1),
              StatisticValue(nl, cycling, 2),
              StatisticValue(nl, hiking, 3),
            )
          )
        )
      )
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
