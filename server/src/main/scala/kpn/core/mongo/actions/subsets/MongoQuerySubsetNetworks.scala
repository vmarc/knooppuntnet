package kpn.core.mongo.actions.subsets

import kpn.api.common.network.NetworkAttributes
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Subset
import kpn.core.mongo.actions.subsets.MongoQuerySubsetNetworks.log
import kpn.core.mongo.actions.subsets.MongoQuerySubsetNetworks.pipelineString
import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQuerySubsetNetworks extends MongoQuery {
  private val log = Log(classOf[MongoQuerySubsetNetworks])
  private val pipelineString = readPipelineString("pipeline")

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      val query = new MongoQuerySubsetNetworks(database)
      query.execute(Subset.nlHiking)
      val networkAttributess = query.execute(Subset.nlHiking)
      println(networkAttributess.size)
      networkAttributess.foreach { networkAttributess =>
        println(s"${networkAttributess.id} ${networkAttributess.name}")
      }
    }
  }
}

class MongoQuerySubsetNetworks(database: MongoDatabase) extends MongoQuery {

  def execute(subset: Subset): Seq[NetworkAttributes] = {
    log.debugElapsed {
      val pipeline = toPipeline(
        pipelineString.
          replace("@country", s"${subset.country.domain}").
          replace("@networkType", s"${subset.networkType.name}")
      )
      if (log.isTraceEnabled) {
        log.trace(Mongo.pipelineString(pipeline))
      }
      val collection = database.getCollection("networks")
      val future = collection.aggregate[NetworkInfo](pipeline).toFuture()
      val networks = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val result = s"subset ${subset.name} networks: ${networks.size}"
      (result, networks.map(_.attributes))
    }
  }
}
