package kpn.database.tools

import kpn.api.base.WithStringId
import kpn.api.custom.Timestamp2
import kpn.database.base.Database
import kpn.database.base.DatabaseCollectionImpl
import kpn.database.util.Mongo
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy

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

    val years = collection.aggregate[Period](pipelineYears())
    val months = collection.aggregate[Period](pipelineMonths())
    val days = collection.aggregate[Period](pipelineDays())

    println("Years")
    years.foreach(println)

    println("Months")
    months.foreach(println)

    println("Days")
    days.foreach(println)
  }

  private def pipelineYears(): Seq[Bson] = {
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

  private def pipelineMonths(): Seq[Bson] = {
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


  private def pipelineDays(): Seq[Bson] = {
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
