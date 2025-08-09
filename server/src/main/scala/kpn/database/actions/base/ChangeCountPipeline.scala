package kpn.database.actions.base

import com.mongodb.client.model.Accumulators.sum
import com.mongodb.client.model.Aggregates.facet
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Facet
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Sorts.orderBy
import kpn.core.util.Log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.DatabaseCollection
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import org.mongodb.scala.Document
import org.mongodb.scala.bson.BsonDocument

object ChangeCountPipeline {

  def execute(
    collection: DatabaseCollection[?],
    mainPipeline: MongoPipeline,
    year: Int,
    monthOption: Option[Int],
    log: Log
  ): ChangeSetCounts = {

    val pipeline = monthOption match {
      case None =>
        mainPipeline ++
          Seq(
            facet(
              new Facet("years", years(): _*),
              new Facet("months", months(year): _*),
            )
          )

      case Some(month) =>
        mainPipeline ++
          Seq(
            facet(
              new Facet("years", years(): _*),
              new Facet("months", months(year): _*),
              new Facet("days", days(year, month): _*),
            )
          )
    }

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val counts = collection.aggregate(pipeline, classOf[ChangeSetCounts]).head
      val result = s"year: $year, month: ${monthOption.getOrElse('-')}, results: years: ${counts.years.size}, months: ${counts.months.size}, days: ${counts.days.size}"
      (result, counts)
    }
  }

  private def years(): MongoPipeline = {
    Seq(
      group(
        Document(
          "year" -> "$key.time.year",
          "impact" -> "$impact"
        ),
        sum("count", 1)
      ),
      project(
        fields(
          excludeId(),
          computed("year", "$_id.year"),
          BsonDocument("""{"impact": {"$cond": {"if": "$_id.impact","then": "$count", "else": 0}}}"""),
          computed("total", "$count")
        )
      ),
      group(
        Document(
          "year" -> "$year"
        ),
        sum("impact", "$impact"),
        sum("total", "$total")
      ),
      sort(
        orderBy(
          descending(
            "_id"
          )
        )
      ),
      project(
        fields(
          excludeId(),
          computed("year", "$_id.year"),
          computed("impact", "$impact"),
          computed("total", "$total")
        )
      )
    )
  }

  private def months(year: Int): MongoPipeline = {
    Seq(
      filter(equal("key.time.year", year)),
      group(
        Document(
          "year" -> "$key.time.year",
          "month" -> "$key.time.month",
          "impact" -> "$impact"
        ),
        sum("count", 1)
      ),
      project(
        fields(
          excludeId(),
          computed("year", "$_id.year"),
          computed("month", "$_id.month"),
          BsonDocument("""{"impact": {"$cond": {"if": "$_id.impact","then": "$count", "else": 0}}}"""),
          computed("total", "$count")
        )
      ),
      group(
        Document(
          "year" -> "$year",
          "month" -> "$month",
        ),
        sum("impact", "$impact"),
        sum("total", "$total")
      ),
      sort(
        orderBy(
          descending(
            "_id"
          )
        )
      ),
      project(
        fields(
          excludeId(),
          computed("year", "$_id.year"),
          computed("month", "$_id.month"),
          computed("impact", "$impact"),
          computed("total", "$total")
        )
      )
    )
  }

  private def days(year: Int, month: Int): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("key.time.year", year),
          equal("key.time.month", month)
        )
      ),
      group(
        Document(
          "year" -> "$key.time.year",
          "month" -> "$key.time.month",
          "day" -> "$key.time.day",
          "impact" -> "$impact"
        ),
        sum("count", 1)
      ),
      project(
        fields(
          excludeId(),
          computed("year", "$_id.year"),
          computed("month", "$_id.month"),
          computed("day", "$_id.day"),
          BsonDocument("""{"impact": {"$cond": {"if": "$_id.impact","then": "$count", "else": 0}}}"""),
          computed("total", "$count")
        )
      ),
      group(
        Document(
          "year" -> "$year",
          "month" -> "$month",
          "day" -> "$day",
        ),
        sum("impact", "$impact"),
        sum("total", "$total")
      ),
      sort(
        orderBy(
          descending(
            "_id"
          )
        )
      ),
      project(
        fields(
          excludeId(),
          computed("year", "$_id.year"),
          computed("month", "$_id.month"),
          computed("day", "$_id.day"),
          computed("impact", "$impact"),
          computed("total", "$total")
        )
      )
    )
  }
}
