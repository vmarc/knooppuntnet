package kpn.core.tools.next.support

import kpn.api.common.data.Tagable
import kpn.api.custom.Tag
import kpn.core.doc.Label
import kpn.database.util.Mongo
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields

case class TagsDoc(_id: Long, tags: Seq[Tag]) extends Tagable

object RouteNameTool {
  def main(args: Array[String]): Unit = {

    Mongo.executeIn("kpn-next") { database =>
      val pipeline = Seq(
        filter(
          and(
            equal("labels", Label.active),
            equal("summary.nodeNetwork", true),
          )
        ),
        project(
          fields(
            computed("tags", "$summary.tags")
          )
        )
      )
      val docs = database.routes.aggregate[TagsDoc](pipeline)

      val docCount = docs.length
      println(s"$docCount routes")
      docs.zipWithIndex.foreach { case (doc, index) =>
        if ((index % 1000) == 0) {
          println(s"$index/$docCount")
        }
        if (doc.hasTag("name") && doc.hasTag("ref")) {
          val name = doc.tagValue("name").get
          val ref = doc.tagValue("ref").get
          if (name != "ref") {
            println(s"routeId=${doc._id}, ref=$ref, name=$name")
          }
        }
      }
    }
  }
}
