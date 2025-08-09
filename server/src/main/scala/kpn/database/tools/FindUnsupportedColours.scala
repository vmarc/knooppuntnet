package kpn.database.tools

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import kpn.core.analysis.RouteColour
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.util.Mongo

object FindUnsupportedColours {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>

      val pipeline = Seq(
        unwind("$tags.tags"),
        filter(
          equal("summary.tags.tags.key", "colour")
        ),
        project(
          fields(
            computed("colour", "$tags.tags.value")
          )
        )
      )

      val knownColours = RouteColour.all.map(_.name)
      val colourTagValues = database.routes.aggregate(pipeline, classOf[RouteColourTagValue])
      colourTagValues.foreach { colourTagValue =>
        val colours = colourTagValue.colour.split(";").toSeq.flatMap(_.split("-")).distinct
        if (colours.exists(c => !knownColours.contains(c))) {
          println(s"|[${colourTagValue._id}](https://knooppuntnet.nl/nl/analysis/route/${colourTagValue._id})|${colourTagValue.colour}|")
        }
      }
    }
  }
}
