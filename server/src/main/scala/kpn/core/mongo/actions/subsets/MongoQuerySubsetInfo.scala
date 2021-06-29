package kpn.core.mongo.actions.subsets

import kpn.api.common.subset.SubsetInfo
import kpn.api.custom.Subset
import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticValue
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unionWith
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

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

      val pipeline = Seq(
        networkCountPipeline,
        Seq(unionWith("statistics-route-orphan-count", orphanRouteCountPipeline: _*)),
        Seq(unionWith("statistics-node-orphan-count", orphanNodeCountPipeline: _*)),
      ).flatten

      //      println(Mongo.pipelineString(pipeline))

      val statisticValues = {
        val collection = database.getCollection[StatisticValue]("statistics-network-count")
        val future = collection.aggregate[StatisticValue](pipeline).toFuture()
        Await.result(future, Duration(30, TimeUnit.SECONDS))
      }
      val subsetInfo = SubsetInfo(
        subset.country,
        subset.networkType,
        networkCount = statisticValues.filter(_.name == "NetworkCount").map(_.value).sum,
        factCount = 0,
        changesCount = 0,
        orphanNodeCount = statisticValues.filter(_.name == "OrphanNodeCount").map(_.value).sum,
        orphanRouteCount = statisticValues.filter(_.name == "OrphanRouteCount").map(_.value).sum
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
