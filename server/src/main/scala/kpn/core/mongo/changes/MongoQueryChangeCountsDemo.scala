package kpn.core.mongo.changes

import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery
import org.mongodb.scala.MongoDatabase

object MongoQueryChangeCountsDemo extends MongoQuery {

  def main(args: Array[String]): Unit = {
    println("MongoQueryChangeCountsDemo")
    val mongoClient = Mongo.client
    try {
      val database = Mongo.database(mongoClient, "kpn-test")

      val demo = new MongoQueryChangeCountsDemo(database)
      demo.networkChangeCounts()
      demo.routeChangeCounts()
      demo.nodeChangeCounts()
    }
    finally {
      mongoClient.close()
    }
  }
}

class MongoQueryChangeCountsDemo(database: MongoDatabase) {

  def networkChangeCounts(): Unit = {
    val query = new MongoQueryNetworkChangeCounts(database)
    query.execute(1066154L, 2020, Some(1))
    val result = query.execute(1066154L, 2020, Some(1))

    println("Years")
    result.years.foreach(println)
    println("Months")
    result.months.foreach(println)
    println("Days")
    result.days.foreach(println)
  }

  def routeChangeCounts(): Unit = {
    val query = new MongoQueryRouteChangeCounts(database)
    query.execute(1212043L, 2020, Some(1))
    query.execute(1212043L, 2020, Some(1))
    val result = query.execute(1212043L, 2020, Some(1))

    println("Years")
    result.years.foreach(println)
    println("Months")
    result.months.foreach(println)
    println("Days")
    result.days.foreach(println)
  }

  def nodeChangeCounts(): Unit = {
    val query = new MongoQueryNodeChangeCounts(database)
    query.execute(257728810L, 2020, Some(6))
    val result = query.execute(4759485214L, 2020, Some(6))
    println("Years")
    result.years.foreach(println)
    println("Months")
    result.months.foreach(println)
    println("Days")
    result.days.foreach(println)
  }
}
