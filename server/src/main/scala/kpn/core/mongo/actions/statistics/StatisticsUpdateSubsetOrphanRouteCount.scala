package kpn.core.mongo.actions.statistics

import kpn.api.common.statistics.StatisticValue
import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetOrphanRouteCount.log
import kpn.core.mongo.util.MongoProjections.concat
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.Document
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.merge
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.MergeOptions
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

object StatisticsUpdateSubsetOrphanRouteCount extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetOrphanRouteCount])
}

class StatisticsUpdateSubsetOrphanRouteCount(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {
      val pipeline = Seq(
        group(
          Document(
            "country" -> "$country",
            "networkType" -> "$networkType"
          ),
          sum("routeCount", 1)
        ),
        project(
          fields(
            concat("_id", "$_id.country", ":", "$_id.networkType", ":OrphanRouteCount"),
            computed("country", "$_id.country"),
            computed("networkType", "$_id.networkType"),
            computed("name", "OrphanRouteCount"),
            computed("value", "$routeCount")
          )
        ),
        merge(
          database.statisticsSubsetOrphanRouteCount.name,
          new MergeOptions().uniqueIdentifier("_id")
        )
      )
      val values = database.orphanRoutes.aggregate[StatisticValue](pipeline)
      (s"${values.size} values", ())
    }
  }
}
