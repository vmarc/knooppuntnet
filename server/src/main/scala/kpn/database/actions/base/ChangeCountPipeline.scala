package kpn.database.actions.base

import org.mongodb.scala.Document
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy

object ChangeCountPipeline {

  def pipelineYears(filter: Option[Bson]): Seq[Bson] = {
    filter.toSeq ++
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

  def pipelineMonths(filter: Bson): Seq[Bson] = {
    Seq(
      filter,
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

  def pipelineDays(filter: Bson): Seq[Bson] = {
    Seq(
      filter,
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
