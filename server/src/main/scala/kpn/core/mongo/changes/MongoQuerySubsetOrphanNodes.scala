package kpn.core.mongo.changes

import kpn.api.common.NodeInfo
import kpn.api.custom.Subset
import kpn.core.mongo.changes.MongoQuerySubsetOrphanNodes.log
import kpn.core.mongo.changes.MongoQuerySubsetOrphanNodes.pipelineString
import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQuerySubsetOrphanNodes extends MongoQuery {
  private val log = Log(classOf[MongoQuerySubsetOrphanNodes])
  private val pipelineString = readPipelineString("pipeline")

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      val query = new MongoQuerySubsetOrphanNodes(database)
      query.execute(Subset.deBicycle)
      val routes = query.execute(Subset.deBicycle)
      println(routes.size)
      routes.foreach { route =>
        println(route)
      }
    }
  }
}

class MongoQuerySubsetOrphanNodes(database: MongoDatabase) extends MongoQuery {

  def execute(subset: Subset): Seq[NodeInfo] = {
    log.debugElapsed {
      val pipeline = toPipeline(
        pipelineString.
          replace("@country", s"${subset.country.domain}").
          replace("@networkType", s"${subset.networkType.name}")
      )
      if (log.isTraceEnabled) {
        log.trace(Mongo.pipelineString(pipeline))
      }
      val collection = database.getCollection[NodeInfo]("nodes")
      val future = collection.aggregate[NodeInfo](pipeline).toFuture()
      val nodes = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val result = s"subset ${subset.name} orphan nodes: ${nodes.size}"
      (result, nodes)
    }
  }
}
