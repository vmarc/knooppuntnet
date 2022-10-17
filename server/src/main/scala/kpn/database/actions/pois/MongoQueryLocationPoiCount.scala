package kpn.database.actions.pois

import kpn.core.util.Log
import kpn.database.actions.pois.MongoQueryLocationPoiCount.log
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.mongodb.scala.model.Filters.equal

object MongoQueryLocationPoiCount {
  private val log = Log(classOf[MongoQueryLocationPoiCount])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-experimental") { database =>
      val result = new MongoQueryLocationPoiCount(database).execute("be-2-11016")
      println(result)
    }
  }
}

class MongoQueryLocationPoiCount(database: Database) {
  def execute(locationName: String): Long = {
    log.debugElapsed {
      val locationPoiCount = database.pois.countDocuments(
        equal("location.names", locationName),
        log
      )
      (s"locationPoiCount: $locationPoiCount", locationPoiCount)
    }
  }
}
