package kpn.database.actions.changes

import kpn.database.base.Database
import kpn.database.util.Mongo

object MongoQueryChangeSetCountsDemo {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>
      val year: Int = 2021
      val month: Option[Int] = Some(10)
      val demo = new MongoQueryChangeSetCountsDemo(database)
      demo.directExecute(year, month)
      demo.all()
      demo.materializedExecute(year, month)
    }
  }
}

class MongoQueryChangeSetCountsDemo(database: Database) {

  def materializedExecute(year: Int, month: Option[Int]): Unit = {
    val materializedCollectionQuery = new MongoQueryChangeSetStatsCounts(database)
    val result = materializedCollectionQuery.execute(year, month) // uses the materialized table
    println("Years")
    result.years.foreach(println)
    println("Months")
    result.months.foreach(println)
    println("Days")
    result.days.foreach(println)
  }

  def all(): Unit = {
    val query = new MongoQueryChangeSetStatsCounts(database)
    query.allDays()
  }

  def directExecute(year: Int, month: Option[Int]): Unit = {
    val materializedCollectionQuery = new MongoQueryChangeSetCounts(database)
    val result = materializedCollectionQuery.execute(None, year, month) // uses the materialized table
    println("Years")
    result.years.foreach(println)
    println("Months")
    result.months.foreach(println)
    if (result.days != null) {
      println("Days")
      result.days.foreach(println)
    }
  }
}
