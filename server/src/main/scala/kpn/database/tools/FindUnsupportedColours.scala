package kpn.database.tools

import kpn.core.analysis.RouteColour
import kpn.database.base.StringId
import kpn.database.util.Mongo
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

case class RouteColourTagValue(_id: Long, colour: String)

object FindUnsupportedColours {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>

      val pipeline = Seq(
        unwind("$tags.tags"),
        filter(
          equal("tags.tags.key", "colour")
        ),
        project(
          fields(
            computed("colour", "$tags.tags.value")
          )
        )
      )

      val knownColours = RouteColour.all.map(_.name)
      val colourTagValues = database.routes.aggregate[RouteColourTagValue](pipeline)
      colourTagValues.foreach { colourTagValue =>
        val colours = colourTagValue.colour.split(";").toSeq.flatMap(_.split("-")).distinct
        if (colours.exists(c => !knownColours.contains(c))) {
          println(s"|[${colourTagValue._id}](https://knooppuntnet.nl/nl/analysis/route/${colourTagValue._id})|${colourTagValue.colour}|")
        }
      }
    }
  }
}
