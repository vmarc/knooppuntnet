package kpn.core.tools.poi

import kpn.database.base.StringId
import kpn.database.util.Mongo
import org.mongodb.scala.model.Aggregates
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Updates.set

object PoiTagsUpdateTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-next") { database =>

      println("loading poi ids")

      val pipeline = Seq(
        Aggregates.filter(
          exists("tags.tags.0"),
        ),
        project(
          fields(
            include("_id")
          )
        )
      )

      val poiIds = database.pois.aggregate[StringId](pipeline).map(_._id)
      val poiCount = poiIds.size

      println(s"$poiCount poi ids loaded")

      poiIds.zipWithIndex.foreach { case (poiId, index) =>
        if ((index % 100) == 0) {
          println(s"$index / $poiCount  $poiId")
        }
        val filter = and(
          equal("_id", poiId)
        )
        val update = Seq(
          set("tags", "$tags.tags")
        )
        database.pois.updateOne(filter, update)
      }
    }
  }
}
