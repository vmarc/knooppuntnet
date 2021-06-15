package kpn.core.mongo.actions.statistics

import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.core.mongo.actions.statistics.MongoQueryStatistics.pipeline
import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQueryStatistics extends MongoQuery {

  private val pipeline = readPipeline("pipeline")

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
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
  }
}

class MongoQueryStatistics(database: MongoDatabase) {

  private val log = Log(classOf[MongoQueryStatistics])

  def execute(): Seq[StatisticValue] = {
    log.debugElapsed {
      val collection = database.getCollection("statistics-network-count")
      val future = collection.aggregate[StatisticValue](pipeline.stages).toFuture()
      val values = Await.result(future, Duration(30, TimeUnit.SECONDS))
      println(s"values.size: ${values.size}")
      ("values", values)
    }
  }
}
