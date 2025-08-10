package kpn.database.actions.base

import com.mongodb.client.model.Aggregates.limit
import com.mongodb.client.model.Aggregates.lookup
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.skip
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.common.changes.filter.ChangesParameters
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import org.bson.BsonDocument

object ChangesPipeline {

  def from(elementId: Long, parameters: ChangesParameters): MongoPipeline = {

    val allFilterElements: MongoPipeline = Seq(
      Some(equal("key.elementId", elementId)),
      if (parameters.impact) Some(equal("impact", true)) else None,
      parameters.year.map(year => equal("key.time.year", year.toInt)),
      parameters.month.map(month => equal("key.time.month", month.toInt)),
      parameters.day.map(day => equal("key.time.day", day.toInt)),
    ).flatten

    val filterElements = if (allFilterElements.sizeIs == 1) {
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
      BsonDocument.parse("""{"$set": { "changeSetId": "$key.changeSetId"}}"""),
      lookup(
        "changeset-comments",
        "changeSetId",
        "_id",
        "comments"
      ),
      BsonDocument.parse("""{"$set": { "comment": {$first: "$comments.comment"}}}"""),
      project(
        fields(
          excludeId()
        )
      )
    )
  }
}
