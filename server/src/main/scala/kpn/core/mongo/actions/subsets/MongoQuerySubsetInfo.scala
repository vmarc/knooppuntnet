package kpn.core.mongo.actions.subsets

import kpn.api.common.statistics.StatisticValue
import kpn.api.common.subset.SubsetInfo
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unionWith
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

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

      val networkCountPipeline = buildPipeline(subset, "NetworkCount")
      val orphanNodeCountPipeline = buildPipeline(subset, "OrphanNodeCount")
      val orphanRouteCountPipeline = buildPipeline(subset, "OrphanRouteCount")
      val factCountPipeline = buildPipeline(subset, "FactCount")
      val changeCountPipeline = buildPipeline(subset, "ChangeCount")

      val pipeline = Seq(
        networkCountPipeline,
        Seq(unionWith(database.statisticsSubsetOrphanNodeCount.name, orphanNodeCountPipeline: _*)),
        Seq(unionWith(database.statisticsSubsetOrphanRouteCount.name, orphanRouteCountPipeline: _*)),
        Seq(unionWith(database.statisticsSubsetFactCount.name, factCountPipeline: _*)),
        Seq(unionWith(database.statisticsSubsetChangeCount.name, changeCountPipeline: _*)),
      ).flatten

      val statisticValues = database.statisticsSubsetNetworkCount.aggregate[StatisticValue](pipeline)
      val networkCount = statisticValues.filter(_.name == "NetworkCount").map(_.value).sum
      val factCount = statisticValues.filter(_.name == "FactCount").map(_.value).sum
      val changesCount = statisticValues.filter(_.name == "ChangeCount").map(_.value).sum
      val orphanNodeCount = statisticValues.filter(_.name == "OrphanNodeCount").map(_.value).sum
      val orphanRouteCount = statisticValues.filter(_.name == "OrphanRouteCount").map(_.value).sum

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

  private def buildPipeline(subset: Subset, name: String): Seq[Bson] = {
    Seq(
      filter(
        equal("_id", s"${subset.country.domain}:${subset.networkType.name}:$name"),
      ),
      project(
        fields(
          excludeId(),
        )
      )
    )
  }
}
