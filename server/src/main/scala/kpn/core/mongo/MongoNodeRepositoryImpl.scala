package kpn.core.mongo

import kpn.api.common.common.Ref
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.database.doc.NetworkDoc
import kpn.core.database.doc.RouteDoc
import kpn.core.mongo.MongoNodeRepositoryImpl.SubsetCountResult
import kpn.core.util.Log
import org.mongodb.scala._
import org.mongodb.scala.model.Accumulators
import org.mongodb.scala.model.Aggregates
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Projections
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoNodeRepositoryImpl {

  case class SubsetCountResultId(country: Country, networkTypes: Seq[NetworkType])

  case class SubsetCountResult(_id: SubsetCountResultId, nodeCount: Long)

}

class MongoNodeRepositoryImpl(database: MongoDatabase) {

  private val log = Log(classOf[MongoNodeRepositoryImpl])

  def findNodesByLocation(networkType: NetworkType, location: String, skip: Int, limit: Int): (Long, Seq[NodeDoc2]) = {

    val filter = Filters.and(
      Filters.eq("node.active", true),
      Filters.eq("node.names.networkType", networkType.name),
      Filters.eq("node.location.names", location),
    )

    val lastSurveyFilter = Filters.exists("node.lastSurvey")

    val nodes = database.getCollection("nodes")

    val totalNodeCount = log.debugElapsed {
      val future = nodes.countDocuments(filter).toFuture()
      val count = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"find location node count ($count)", count)
    }

    val nodeInfos = log.debugElapsed {
      val future = nodes.aggregate[NodeDoc2](
        Seq(
          Aggregates.filter(filter),
          // Aggregates.filter(lastSurveyFilter),
          Aggregates.sort(
            orderBy(ascending("node.names.name", "node.id"))
          ),
          Aggregates.skip(skip),
          Aggregates.limit(limit),
          Aggregates.project(
            Projections.fields(
              Projections.excludeId(),
            )
          ),
          Aggregates.lookup(
            "nodeRouteRefs",
            "node.id",
            "nodeId",
            "routeRefs"
          )
        )
      ).toFuture()
      val nodeDocs = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val cleanedNodeDocs = nodeDocs.map { nodeDoc =>
        nodeDoc.copy(routeRefs = nodeDoc.routeRefs.filter(_.networkType == networkType))
      }
      ("find location nodes", cleanedNodeDocs)
    }

    (totalNodeCount, nodeInfos)
  }

  def findNetworkReferences(nodeId: Long): Seq[Ref] = {
    log.debugElapsed {
      val networks = database.getCollection("networks")
      val future = networks.aggregate[NetworkDoc](
        Seq(
          Aggregates.filter(
            Filters.and(
              Filters.eq("network.active", true),
              Filters.eq("network.nodeRefs", nodeId),
            )
          ),
          Aggregates.project(
            Projections.fields(
              Projections.excludeId(),
              Projections.include("network.attributes.id"),
              Projections.include("network.attributes.name"),
            )
          ),
        )
      ).toFuture()
      val networkDocs = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val refs = networkDocs.map { networkDoc =>
        Ref(
          networkDoc.network.attributes.id,
          networkDoc.network.attributes.name
        )
      }
      ("find network references", refs)
    }
  }

  def findRouteReferences(nodeId: Long): Seq[Ref] = {
    log.debugElapsed {
      val networks = database.getCollection("routes")
      val future = networks.aggregate[RouteDoc](
        Seq(
          Aggregates.filter(
            Filters.and(
              Filters.eq("route.active", true),
              Filters.eq("route.nodeRefs", nodeId),
            )
          ),
          Aggregates.project(
            Projections.fields(
              Projections.excludeId(),
              Projections.include("route.summary.id"),
              Projections.include("route.summary.name"),
            )
          ),
        )
      ).toFuture()
      val routeDocs = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val refs = routeDocs.map { routeDoc =>
        Ref(
          routeDoc.route.summary.id,
          routeDoc.route.summary.name
        )
      }
      ("find route references", refs)
    }
  }

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
