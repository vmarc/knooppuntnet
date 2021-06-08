package kpn.core.mongo.changes

import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object ChangeCountPipeline {
  def from(elementType: String, elementId: Long): Seq[Bson] = {
    Seq(
      filter(equal(s"$elementType.key.elementId", elementId)),
      group(
        s"$$$elementType.key.elementId",
        sum("count", 1)
      ),
      project(
        fields(
          excludeId(),
          include("count")
        )
      )
    )
  }
}
