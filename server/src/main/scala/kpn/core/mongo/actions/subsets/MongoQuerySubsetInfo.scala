package kpn.core.mongo.actions.subsets

import kpn.api.common.statistics.StatisticValues
import kpn.api.common.subset.SubsetInfo
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Filters.in

object MongoQuerySubsetInfo {

  private val log = Log(classOf[MongoQuerySubsetInfo])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      val query = new MongoQuerySubsetInfo(database)
      println(query.execute(Subset.nlBicycle))
      println(query.execute(Subset.deBicycle))
    }
  }
}

class MongoQuerySubsetInfo(database: Database) {

  def execute(subset: Subset, log: Log = MongoQuerySubsetInfo.log): SubsetInfo = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          in(
            "_id",
            Seq(
              "NetworkCount",
              "FactCount",
              "ChangeCount",
              "OrphanNodeCount",
              "OrphanRouteCount",
            )
          )
        )
      )

      val statisticValuess = database.statistics.aggregate[StatisticValues](pipeline)
      val networkCount = extractCount(subset, statisticValuess, "NetworkCount")
      val factCount = extractCount(subset, statisticValuess, "FactCount")
      val changesCount = extractCount(subset, statisticValuess, "ChangeCount")
      val orphanNodeCount = extractCount(subset, statisticValuess, "OrphanNodeCount")
      val orphanRouteCount = extractCount(subset, statisticValuess, "OrphanRouteCount")

      val subsetInfo = SubsetInfo(
        subset.country,
        subset.networkType,
        networkCount,
        factCount,
        changesCount,
        orphanNodeCount,
        orphanRouteCount
      )
      (s"subsetInfo", subsetInfo)
    }
  }

  private def extractCount(subset: Subset, statisticValuess: Seq[StatisticValues], factname: String): Long = {
    statisticValuess.filter(_._id == factname).map { statisticValues =>
      statisticValues.values.filter(statisticValue => statisticValue.country == subset.country && statisticValue.networkType == subset.networkType).map(_.value).sum
    }.sum
  }

}
