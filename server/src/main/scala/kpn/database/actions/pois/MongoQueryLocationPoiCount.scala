package kpn.database.actions.pois

import com.mongodb.client.model.Filters.and
import kpn.core.util.Log
import kpn.database.actions.pois.MongoQueryLocationPoiCount.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.util.Mongo

object MongoQueryLocationPoiCount {
  private val log = Log(classOf[MongoQueryLocationPoiCount])

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-experimental") { database =>
      val result = new MongoQueryLocationPoiCount(database).execute("be-2-11016", Seq("pub"))
      println(result)
    }
  }
}

class MongoQueryLocationPoiCount(database: Database) {
  def execute(locationName: String, layers: Seq[String]): Long = {
    log.debugElapsed {
      val filter = and(
        Seq(
          Some(equal("location.names", locationName)),
          LayerFilter.of(layers)
        ).flatten: _*
      )
      val locationPoiCount = database.pois.countFilteredDocuments(filter, log)
      (s"locationPoiCount: $locationPoiCount", locationPoiCount)
    }
  }
}
