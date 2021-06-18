package kpn.core.mongo.actions.locations

import kpn.api.custom.NetworkType
import kpn.core.mongo.Database
import kpn.core.mongo.NodeDoc2
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.limit
import org.mongodb.scala.model.Aggregates.lookup
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.skip
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Filters.and
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MongoQueryLocationNodes(database: Database) {

  private val log = Log(classOf[MongoQueryLocationNodes])

  def execute(networkType: NetworkType, location: String, page: Int, pageSize: Int): (Long, Seq[NodeDoc2]) = {

    val locationFilter = and(
      equal("node.active", true),
      equal("node.names.networkType", networkType.name),
      equal("node.location.names", location),
    )

    val lastSurveyFilter = exists("node.lastSurvey")

    val nodes = database.database.getCollection("nodes")

    val totalNodeCount = log.debugElapsed {
      val future = nodes.countDocuments(locationFilter).toFuture()
      val count = Await.result(future, Duration(60, TimeUnit.SECONDS))
      (s"find location node count ($count)", count)
    }

    val pipeline = Seq(
      filter(locationFilter),
      filter(lastSurveyFilter),
      sort(orderBy(ascending("node.names.name", "node.id"))),
      skip(page * pageSize),
      limit(pageSize),
      project(
        fields(
          include("node"),
          computed[String]("test1", "$node.id"),
          computed[String]("test2", "$node.lastSurvey"),
          excludeId(),
        )
      ),
      lookup(
        "nodeRouteRefs",
        "node.id",
        "nodeId",
        "routeRefs"
      )
    )

    val nodeInfos = log.debugElapsed {
      val future = nodes.aggregate[NodeDoc2](pipeline).toFuture()
      val nodeDocs = Await.result(future, Duration(60, TimeUnit.SECONDS))
      val cleanedNodeDocs = nodeDocs.map { nodeDoc =>
        nodeDoc.copy(routeRefs = nodeDoc.routeRefs.filter(_.networkType == networkType))
      }
      ("find location nodes", cleanedNodeDocs)
    }

    (totalNodeCount, nodeInfos)
  }
}
