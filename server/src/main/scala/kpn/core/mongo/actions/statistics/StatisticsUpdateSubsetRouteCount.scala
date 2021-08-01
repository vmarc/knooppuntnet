package kpn.core.mongo.actions.statistics

import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetRouteCount.log
import kpn.core.mongo.util.MongoProjections.concat
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.Document
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.merge
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.MergeOptions
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

object StatisticsUpdateSubsetRouteCount extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetRouteCount])
}

class StatisticsUpdateSubsetRouteCount(database: Database) {

  def execute(): Unit = {
    log.debugElapsed {
      val pipeline = Seq(
        filter(
          and(
            equal("labels", "active"),
            exists("summary.country")
          )
        ),
        group(
          Document(
            "country" -> "$summary.country",
            "networkType" -> "$summary.networkType"
          ),
          sum("routeCount", 1)
        ),
        project(
          fields(
            concat("_id", "$_id.country", ":", "$_id.networkType", ":RouteCount"),
            computed("country", "$_id.country"),
            computed("networkType", "$_id.networkType"),
            computed("name", "RouteCount"),
            computed("value", "$routeCount")
          )
        ),
        merge(
          database.statisticsSubsetRouteCount.name,
          new MergeOptions().uniqueIdentifier("_id")
        )
      )
      val values = database.routes.aggregate[StatisticValue](pipeline)
      (s"${values.size} values", ())
    }
  }
}
