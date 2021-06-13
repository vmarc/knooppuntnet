package kpn.core.mongo.changes

import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery
import org.mongodb.scala.MongoDatabase

object MongoQueryChangeSetCountsDemo extends MongoQuery {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      val year: Int = 2020
      val month: Option[Int] = Some(1)
      val demo = new MongoQueryChangeSetCountsDemo(database)
      demo.directAll()
      demo.directAll()
      demo.directExecute(year, month)
      demo.all()
      demo.materializedExecute(year, month)
    }
  }
}

class MongoQueryChangeSetCountsDemo(database: MongoDatabase) {

  def materializedExecute(year: Int, month: Option[Int]): Unit = {
    val materializedCollectionQuery = new MongoQueryChangeSetCounts(database)
    val result = materializedCollectionQuery.execute(year, month) // uses the materialized table
    println("Years")
    result.years.foreach(println)
    println("Months")
    result.months.foreach(println)
    println("Days")
    result.days.foreach(println)
  }

  def all(): Unit = {
    val query = new MongoQueryChangeSetCounts(database)
    query.allDays()
  }

  def directExecute(year: Int, month: Option[Int]): Unit = {
    val materializedCollectionQuery = new MongoQueryChangeSetDirectCounts(database)
    val result = materializedCollectionQuery.execute(year, month) // uses the materialized table
    println("Years")
    result.years.foreach(println)
    println("Months")
    result.months.foreach(println)
    if (result.days != null) {
      println("Days")
      result.days.foreach(println)
    }
  }

  def directAll(): Unit = {
    val query = new MongoQueryChangeSetDirectCounts(database)
    query.allDays()
  }
}
