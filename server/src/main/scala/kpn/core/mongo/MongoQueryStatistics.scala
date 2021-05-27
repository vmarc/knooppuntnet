package kpn.core.mongo

import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryStatistics {
  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    try {
      val database = Mongo.database(mongoClient, "tryout")
      val query = new MongoQueryStatistics(database)
      query.execute() // initial call to warm up, so that next query timing makes sense
      val statisticValues = query.execute()
      Country.all.foreach { country =>
        NetworkType.all.foreach { networkType =>
          val subsetValues = statisticValues.filter(v => v.country == country && v.networkType == networkType)
          if (subsetValues.nonEmpty) {
            println(s"${country.domain} ${networkType.name}")
            subsetValues.sortBy(_.name).foreach { value =>
              println(s"  ${value.name} ${value.value}")
            }
          }
        }
      }
    }
    finally {
      mongoClient.close()
    }
  }
}

class MongoQueryStatistics(database: MongoDatabase) {

  private val log = Log(classOf[MongoQueryStatistics])

  def execute(): Seq[StatisticValue] = {
    log.debugElapsed {
      val future = database.getCollection("counts").find[Count]().toFuture()
      val counts = Await.result(future, Duration(30, TimeUnit.SECONDS))
      val values = counts.map(count => StatisticValue(count.country, count.networkType, count.name, count.count))
      ("values", values)
    }
  }
}
