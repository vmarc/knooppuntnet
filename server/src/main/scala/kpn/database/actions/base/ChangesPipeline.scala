package kpn.database.actions.base

import kpn.api.common.changes.filter.ChangesParameters
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.lookup
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy

object ChangesPipeline {

  def from(elementId: Long, parameters: ChangesParameters): Seq[Bson] = {

    val allFilterElements: Seq[Bson] = Seq(
      Some(equal("key.elementId", elementId)),
      if (parameters.impact) Some(equal("impact", true)) else None,
      parameters.year.map(year => equal("key.time.year", year.toInt)),
      parameters.month.map(month => equal("key.time.month", month.toInt)),
      parameters.day.map(day => equal("key.time.day", day.toInt)),
    ).flatten

    val filterElements = if (allFilterElements.size == 1) {
      allFilterElements.head
    }
    else {
      and(allFilterElements: _*)
    }

    Seq(
      filter(filterElements),
      sort(
        orderBy(
          descending(
            "key.time",
          )
        )
      ),
      skip((parameters.pageSize * parameters.pageIndex).toInt),
      limit(parameters.pageSize.toInt),
      BsonDocument("""{"$set": { "changeSetId": "$key.changeSetId"}}"""),
      lookup(
        "changeset-comments",
        "changeSetId",
        "_id",
        "comments"
      ),
      BsonDocument("""{"$set": { "comment": {$first: "$comments.comment"}}}"""),
      project(
        fields(
          excludeId()
        )
      )
    )
  }
}
