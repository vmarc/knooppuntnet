package kpn.core.mongo.changes

import kpn.api.common.OrphanRouteInfo
import kpn.api.custom.Subset
import kpn.core.mongo.changes.MongoQuerySubsetOrphanRoutes.log
import kpn.core.mongo.changes.MongoQuerySubsetOrphanRoutes.pipelineString
import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQuerySubsetOrphanRoutes extends MongoQuery {
  private val log = Log(classOf[MongoQuerySubsetOrphanRoutes])
  private val pipelineString = readPipelineString("pipeline")

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      val query = new MongoQuerySubsetOrphanRoutes(database)
      query.execute(Subset.deBicycle)
      val routes = query.execute(Subset.deBicycle)
      println(routes.size)
      routes.foreach { route =>
        println(route)
      }
    }
  }
}

class MongoQuerySubsetOrphanRoutes(database: MongoDatabase) extends MongoQuery {

  def execute(subset: Subset): Seq[OrphanRouteInfo] = {
    log.debugElapsed {
      val pipeline = toPipeline(
        pipelineString.
          replace("@country", s"${subset.country.domain}").
          replace("@networkType", s"${subset.networkType.name}")
      )
      if (log.isTraceEnabled) {
        log.trace(Mongo.pipelineString(pipeline))
      }
      val collection = database.getCollection("routes")
      val future = collection.aggregate[OrphanRouteInfo](pipeline).toFuture()
      val routes = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val result = s"subset ${subset.name} orphan routes: ${routes.size}"
      (result, routes)
    }
  }
}
