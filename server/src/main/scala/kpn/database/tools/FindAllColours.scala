package kpn.database.tools

import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.unwind
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.StringId
import kpn.database.util.Mongo

object FindAllColours {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>

      val pipeline = Seq(
        unwind("$tags.tags"),
        filter(
          equal("summary.tags.tags.key", "colour")
        ),
        group(
          "$tags.tags.value"
        )
      )

      val colourTagValues = database.routes.aggregate(pipeline, classOf[StringId])
      val values = colourTagValues.map(_._id).flatMap(_.split(";").toSeq.flatMap(_.split("-"))).sorted.distinct
      values.foreach(println)
    }
  }
}
