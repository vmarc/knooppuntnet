package kpn.core.tools.poi

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Updates.set
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.StringId
import kpn.database.util.Mongo

object PoiTagsUpdateTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-next") { database =>

      println("loading poi ids")

      val pipeline = Seq(
        filter(
          Filters.exists("tags.tags.0"),
        ),
        project(
          fields(
            include("_id")
          )
        )
      )

      val poiIds = database.pois.aggregate(pipeline, classOf[StringId]).map(_._id)
      val poiCount = poiIds.size

      println(s"$poiCount poi ids loaded")

      poiIds.zipWithIndex.foreach { case (poiId, index) =>
        if ((index % 100) == 0) {
          println(s"$index / $poiCount  $poiId")
        }
        val filter = and(
          equal("_id", poiId)
        )
        val update = set("tags", "$tags.tags")
        database.pois.updateOne(filter, update)
      }
    }
  }
}
