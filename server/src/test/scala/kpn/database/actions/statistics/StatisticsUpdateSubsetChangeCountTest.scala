package kpn.database.actions.statistics

import kpn.api.common.Country.de
import kpn.api.common.Country.nl
import kpn.api.common.RouteType.cycling
import kpn.api.common.RouteType.hiking
import kpn.api.custom.Subset
import kpn.api.custom.Subset.deHiking
import kpn.api.custom.Subset.nlBicycle
import kpn.api.custom.Subset.nlHiking
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater

class StatisticsUpdateSubsetChangeCountTest extends MongoTest {

  test("execute") {
    buildChangeSetSummary(1L, Seq(nlHiking))
    buildChangeSetSummary(2L, Seq(nlHiking))
    buildChangeSetSummary(3L, Seq(nlHiking))
    buildChangeSetSummary(4L, Seq(nlBicycle))
    buildChangeSetSummary(5L, Seq(nlBicycle))
    buildChangeSetSummary(6L, Seq(deHiking))

    new StatisticsUpdater(database).execute()
    val counts = new MongoQueryStatistics(database).execute()

    assertEqual(
      counts.find(_._id == "ChangeCount").get.values.toSet,
      Set(
        StatisticLongValue(de, hiking, 1L),
        StatisticLongValue(nl, cycling, 2L),
        StatisticLongValue(nl, hiking, 3L),
      )
    )
  }

  private def buildChangeSetSummary(changeSetId: Long, subsets: Seq[Subset]): Unit = {
    database.changes.save(
      newChangeSetSummary(
        newChangeKey(changeSetId = changeSetId),
        subsets
      )
    )
  }
}
