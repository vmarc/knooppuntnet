package kpn.core.mongo.actions.statistics

import kpn.api.common.statistics.StatisticValues
import kpn.core.mongo.Database
import kpn.core.mongo.actions.statistics.StatisticsUpdateSubsetRouteFacts.log
import kpn.core.mongo.util.MongoProjections.concat
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.Document
import org.mongodb.scala.model.Accumulators.push
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.merge
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.MergeOptions
import org.mongodb.scala.model.MergeOptions.WhenMatched
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object StatisticsUpdateSubsetRouteFacts extends MongoQuery {
  private val log = Log(classOf[StatisticsUpdateSubsetRouteFacts])
}

class StatisticsUpdateSubsetRouteFacts(database: Database) {
  def execute(): Unit = {
    log.debugElapsed {

      // TODO MONGO also include node and network facts !!!

      val pipeline = Seq(
        filter(
          and(
            equal("labels", "active"),
            exists("summary.country"),
            exists("facts")
          )
        ),
        unwind("$facts"),
        group(
          Document(
            "country" -> "$summary.country",
            "networkType" -> "$summary.networkType",
            "factName" -> "$facts"
          ),
          sum("value", 1)
        ),
        sort(orderBy(ascending("_id"))),
        project(
          fields(
            concat("factName", "$_id.factName", "Count"),
            computed(
              "values",
              fields(
                computed("country", "$_id.country"),
                computed("networkType", "$_id.networkType"),
                computed("value", "$value")
              )
            )
          )
        ),
        group(
          "$factName",
          push("values", "$values")
        ),
        merge(
          database.statistics.name,
          new MergeOptions()
            .uniqueIdentifier("_id")
            .whenMatched(WhenMatched.REPLACE)
        )
      )
      val values = database.routes.aggregate[StatisticValues](pipeline)
      (s"${values.size} values", ())
    }
  }
}
