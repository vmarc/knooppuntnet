package kpn.database.actions.statistics

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country.de
import kpn.api.custom.Country.nl
import kpn.api.custom.NetworkType.cycling
import kpn.api.custom.NetworkType.hiking
import kpn.api.custom.Subset
import kpn.api.custom.Subset.deHiking
import kpn.api.custom.Subset.nlBicycle
import kpn.api.custom.Subset.nlHiking
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetChangeCountTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>

      buildChangeSetSummary(database, 1L, Seq(nlHiking))
      buildChangeSetSummary(database, 2L, Seq(nlHiking))
      buildChangeSetSummary(database, 3L, Seq(nlHiking))
      buildChangeSetSummary(database, 4L, Seq(nlBicycle))
      buildChangeSetSummary(database, 5L, Seq(nlBicycle))
      buildChangeSetSummary(database, 6L, Seq(deHiking))

      new StatisticsUpdater(database).execute()
      val counts = new MongoQueryStatistics(database).execute()

      counts should contain(
        StatisticLongValues(
          "ChangeCount",
          Seq(
            StatisticLongValue(de, hiking, 1L),
            StatisticLongValue(nl, cycling, 2L),
            StatisticLongValue(nl, hiking, 3L),
          )
        )
      )
    }
  }

  private def buildChangeSetSummary(database: Database, changeSetId: Long, subsets: Seq[Subset]): Unit = {
    database.changes.save(
      newChangeSetSummary(
        newChangeKey(changeSetId = changeSetId),
        subsets
      )
    )
  }
}
