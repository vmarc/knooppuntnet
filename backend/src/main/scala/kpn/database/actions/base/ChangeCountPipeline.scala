package kpn.database.actions.base

import com.mongodb.client.model.Accumulators.sum
import com.mongodb.client.model.Aggregates.facet
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
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
import kpn.database.base.MongoAggregates.ffacet
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import org.bson.BsonDocument
import org.bson.Document

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
              ffacet("years", years()),
              ffacet("months", months(year)),
            )
          )

      case Some(month) =>
        mainPipeline ++
          Seq(
            facet(
              ffacet("years", years()),
              ffacet("months", months(year)),
              ffacet("days", days(year, month)),
            )
          )
    }

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      val counts = collection.aggregate(pipeline, classOf[ChangeSetCounts]).head
      val updatedCounts = if (counts.days == null) {
        counts.copy(days = Seq.empty)
      }
      else {
        counts
      }
      val result = s"year: $year, month: ${monthOption.getOrElse('-')}, results: years: ${updatedCounts.years.size}, months: ${updatedCounts.months.size}, days: ${updatedCounts.days.size}"
      (result, updatedCounts)
    }
  }

  private def years(): MongoPipeline = {
    Seq(
      group(
        new Document(
          java.util.Map.of(
            "year", "$key.time.year",
            "impact", "$impact"
          )
        ),
        sum("count", 1)
      ),
      project(
        fields(
          excludeId(),
          computed("year", "$_id.year"),
          BsonDocument.parse("""{"impact": {"$cond": {"if": "$_id.impact","then": "$count", "else": 0}}}"""),
          computed("total", "$count")
        )
      ),
      group(
        new Document(
          java.util.Map.of(
            "year", "$year"
          )
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
        new Document(
          java.util.Map.of(
            "year", "$key.time.year",
            "month", "$key.time.month",
            "impact", "$impact"
          )
        ),
        sum("count", 1)
      ),
      project(
        fields(
          excludeId(),
          computed("year", "$_id.year"),
          computed("month", "$_id.month"),
          BsonDocument.parse("""{"impact": {"$cond": {"if": "$_id.impact","then": "$count", "else": 0}}}"""),
          computed("total", "$count")
        )
      ),
      group(
        new Document(
          java.util.Map.of(
            "year", "$year",
            "month", "$month",
          )
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
        new Document(
          java.util.Map.of(
            "year", "$key.time.year",
            "month", "$key.time.month",
            "day", "$key.time.day",
            "impact", "$impact"
          )
        ),
        sum("count", 1)
      ),
      project(
        fields(
          excludeId(),
          computed("year", "$_id.year"),
          computed("month", "$_id.month"),
          computed("day", "$_id.day"),
          BsonDocument.parse("""{"impact": {"$cond": {"if": "$_id.impact","then": "$count", "else": 0}}}"""),
          computed("total", "$count")
        )
      ),
      group(
        new Document(
          java.util.Map.of(
            "year", "$year",
            "month", "$month",
            "day", "$day",
          )
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
