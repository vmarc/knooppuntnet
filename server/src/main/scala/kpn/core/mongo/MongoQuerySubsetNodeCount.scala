package kpn.core.mongo

import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.mongo.MongoQuerySubsetNodeCount.SubsetCountResult
import kpn.core.util.Log
import org.mongodb.scala.Document
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Accumulators
import org.mongodb.scala.model.Aggregates
import org.mongodb.scala.model.Filters

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoQuerySubsetNodeCount {

  case class SubsetCountResultId(country: Country, networkTypes: Seq[NetworkType])

  case class SubsetCountResult(_id: SubsetCountResultId, nodeCount: Long)

}

class MongoQuerySubsetNodeCount(database: MongoDatabase) {

  private val log = Log(classOf[MongoQuerySubsetNodeCount])

  def nodeCountPerSubset(): Seq[SubsetNodeCount] = {

    val nodes = database.getCollection("nodes")

    log.debugElapsed {
      val future = nodes.aggregate[SubsetCountResult](
        Seq(
          Aggregates.filter(
            Filters.eq("node.active", true)
          ),
          Aggregates.group(
            Document("{country: '$node.country', networkTypes: '$node.names.networkType'}"),
            Accumulators.sum("nodeCount", 1)
          ),
        )
      ).toFuture()
      val subsetCountResults = Await.result(future, Duration(60, TimeUnit.SECONDS))

      val countries = subsetCountResults.map(_._id.country).distinct
      val networkTypes = subsetCountResults.flatMap(_._id.networkTypes).distinct
      val subsetNodeCounts = countries.flatMap { country =>
        networkTypes.flatMap { networkType =>
          val count = subsetCountResults.filter(scr => scr._id.country == country && scr._id.networkTypes.contains(networkType)).map(_.nodeCount).sum
          if (count > 0) {
            Subset.of(country, networkType).map { subset =>
              SubsetNodeCount(subset, count)
            }
          }
          else {
            None
          }
        }
      }

      ("subsetNodeCounts", subsetNodeCounts)
    }
  }
}
