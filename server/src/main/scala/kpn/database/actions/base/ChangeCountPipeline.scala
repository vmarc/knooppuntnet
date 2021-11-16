package kpn.database.actions.base

import kpn.core.util.Log
import kpn.database.actions.statistics.ChangeSetCounts
import kpn.database.base.DatabaseCollection
import kpn.database.util.Mongo
import org.mongodb.scala.Document
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.facet
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Facet
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy

object ChangeCountPipeline {

  def execute(collection: DatabaseCollection[_], mainFilter: Option[Bson], year: Int, monthOption: Option[Int], log: Log): ChangeSetCounts = {

    val pipeline = monthOption match {
      case None =>
        mainFilter.toSeq ++
          Seq(
            facet(
              Facet("years", years(): _*),
              Facet("months", months(year): _*),
            )
          )

      case Some(month) =>
        mainFilter.toSeq ++
          Seq(
            facet(
              Facet("years", years(): _*),
              Facet("months", months(year): _*),
              Facet("days", days(year, month): _*),
            )
          )
    }

    if (log.isTraceEnabled) {
      log.trace(Mongo.pipelineString(pipeline))
    }

    log.debugElapsed {
      // collection.native.aggregate[ChangeSetCounts](pipeline).allowDiskUse(true).first().toFuture()
      val counts = collection.aggregate[ChangeSetCounts](pipeline).head
      val result = s"year: $year, month: ${monthOption.getOrElse('-')}, results: years: ${counts.years.size}, months: ${counts.months.size}, days: ${counts.days.size}"
      (result, counts)
    }
  }

  private def years(): Seq[Bson] = {
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

  private def months(year: Int): Seq[Bson] = {
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

  private def days(year: Int, month: Int): Seq[Bson] = {
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
