package kpn.database.actions.statistics

import kpn.api.common.statistics.StatisticValues
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.actions.statistics.StatisticsUpdateSubsetNodeFacts.log
import kpn.database.base.Database
import kpn.database.base.MongoProjections.concat
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

object StatisticsUpdateSubsetNodeFacts {
  private val log = Log(classOf[StatisticsUpdateSubsetNodeFacts])
}

class StatisticsUpdateSubsetNodeFacts(database: Database) {

  def execute(): Unit = {
    log.debugElapsed {

      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            exists("country"),
            exists("facts")
          )
        ),
        unwind("$names"),
        unwind("$facts"),
        group(
          Document(
            "country" -> "$country",
            "networkType" -> "$names.networkType",
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
      val values = database.nodes.aggregate[StatisticValues](pipeline)
      (s"${values.size} values", ())
    }
  }
}
