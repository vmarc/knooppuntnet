package kpn.database.tools

import kpn.database.base.StringId
import kpn.database.util.Mongo
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal

object FindAllColours {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>

      val pipeline = Seq(
        unwind("$tags.tags"),
        filter(
          equal("tags.tags.key", "colour")
        ),
        group(
          "$tags.tags.value"
        )
      )

      val colourTagValues = database.routes.aggregate[StringId](pipeline)
      val values = colourTagValues.map(_._id).flatMap(_.split(";").toSeq.flatMap(_.split("-"))).sorted.distinct
      values.foreach(println)
    }
  }
}
