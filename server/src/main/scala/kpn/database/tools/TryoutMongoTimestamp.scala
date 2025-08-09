package kpn.database.tools

import com.mongodb.client.model.Accumulators.sum
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Sorts.orderBy
import kpn.api.base.WithStringId
import kpn.api.custom.Timestamp2
import kpn.database.base.Database
import kpn.database.base.DatabaseCollectionImpl
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import org.mongodb.scala.bson.BsonDocument

case class Period(year: Option[Long], month: Option[Long], day: Option[Long], count: Long)

case class TestDoc(
  _id: String,
  timestamp: Timestamp2
) extends WithStringId

object TimestampDemo {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>
      // new TimestampDemo(database).populateCollection()
      new TimestampDemo(database).periods()
    }
  }
}

class TimestampDemo(database: Database) {

  private val collection = new DatabaseCollectionImpl(database.getCollection[TestDoc]("test"))

  def populateCollection(): Unit = {
    2015 to 2020 foreach { year =>
      1 to 12 foreach { month =>
        1 to month foreach { day =>
          val timestamp = Timestamp2(year, month, day, 1, 2, 3)
          collection.save(TestDoc(timestamp.iso, timestamp))
        }
      }
    }
  }

  def periods(): Unit = {

    val years = collection.aggregate(pipelineYears(), classOf[Period])
    val months = collection.aggregate(pipelineMonths(), classOf[Period])
    val days = collection.aggregate(pipelineDays(), classOf[Period])

    println("Years")
    years.foreach(println)

    println("Months")
    months.foreach(println)

    println("Days")
    days.foreach(println)
  }

  private def pipelineYears(): MongoPipeline = {
    val groupId =
      """{
        |  year: {$year: { date: "$timestamp" }}
        |}""".stripMargin

    Seq(
      group(
        BsonDocument(groupId),
        sum("count", 1)
      ),
      sort(orderBy(descending("_id"))),
      project(
        fields(
          excludeId(),
          computed("year", "$_id.year"),
          include("count")
        )
      )
    )
  }

  private def pipelineMonths(): MongoPipeline = {
    val groupId =
      """{
        |  year: {$year: { date: "$timestamp" }},
        |  month: {$month: { date: "$timestamp" }},
        |}""".stripMargin

    Seq(
      group(
        BsonDocument(groupId),
        sum("count", 1)
      ),
      sort(orderBy(descending("_id"))),
      project(
        fields(
          excludeId(),
          computed("year", "$_id.year"),
          computed("month", "$_id.month"),
          include("count")
        )
      )
    )
  }

  private def pipelineDays(): MongoPipeline = {
    val groupId =
      """{
        |  year: {$year: { date: "$timestamp" }},
        |  month: {$month: { date: "$timestamp" }},
        |  day: {$dayOfMonth: { date: "$timestamp" }}
        |}""".stripMargin

    Seq(
      group(
        BsonDocument(groupId),
        sum("count", 1)
      ),
      sort(orderBy(descending("_id"))),
      project(
        fields(
          excludeId(),
          computed("year", "$_id.year"),
          computed("month", "$_id.month"),
          computed("day", "$_id.day"),
          include("count")
        )
      )
    )
  }
}
