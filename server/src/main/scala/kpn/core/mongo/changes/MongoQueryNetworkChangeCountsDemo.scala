package kpn.core.mongo.changes

import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery

object MongoQueryNetworkChangeCountsDemo extends MongoQuery {

  def main(args: Array[String]): Unit = {
    println("MongoQueryNetworkChangeCountsDemo")
    val mongoClient = Mongo.client
    try {
      val database = Mongo.database(mongoClient, "tryout")
      val query = new MongoQueryNetworkChangeCounts(database)
      query.execute(1066154L, 2020, Some(1))
      query.execute(1066154L, 2020, Some(1))

      query.execute(1059986L, 2020, Some(1))
      query.execute(1059986L, 2020, Some(1))

      query.execute(1595466L, 2020, Some(1))
      val result = query.execute(1595466L, 2020, Some(1))

      println("Years")
      result.years.foreach(println)
      println("Months")
      result.months.foreach(println)
      println("Days")
      result.days.foreach(println)
    }
    finally {
      mongoClient.close()
    }
  }
}
