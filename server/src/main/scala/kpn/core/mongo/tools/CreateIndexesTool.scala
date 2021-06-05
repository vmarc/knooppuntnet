package kpn.core.mongo.tools

import kpn.core.mongo.tools.CreateIndexesTool.Index
import kpn.core.mongo.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.IndexOptions
import org.mongodb.scala.model.Indexes

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object CreateIndexesTool {
  object Index {
    def apply(
      collectionName: String,
      indexName: String,
      fieldNames: String*
    ): Index = {
      val index = Indexes.ascending(fieldNames:_*)
      Index(
        collectionName: String,
        indexName: String,
        index
      )
    }
  }

  case class Index(
    collectionName: String,
    indexName: String,
    index: Bson
  )

  private val indexes = Seq(
    Index(
      "networks",
      "network-name",
      "network.active",
      "network.attributes.name"
    ),
    Index(
      "networks",
      "network-node-references",
      "network.active",
      "network.nodeRefs"
    ),
    Index(
      "nodes",
      "location",
      "node.active",
      "node.location.names"
    ),
    Index(
      "nodes",
      "node-active",
      "node.active"
    ),
    Index(
      "routes",
      "route-node-references",
      "route.active",
      "route.nodeRefs"
    ),
    Index(
      "nodeRouteRefs",
      "nodeId",
      "nodeId",
      "routeName"
    ),
    Index(
      "change-networks",
      "time",
      "networkChange.key.time"
    ),
    Index(
      "change-networks",
      "impact",
      "networkChange.impact"
    ),
    Index(
      "change-networks",
      "changeSetId",
      "networkChange.key.replicationNumber",
      "networkChange.key.changeSetId"
    ),
    Index(
      "change-routes",
      "time",
      "routeChange.key.time"
    ),
    Index(
      "change-routes",
      "impact",
      "routeChange.impact"
    ),
    Index(
      "change-routes",
      "changeSetId",
      "routeChange.key.replicationNumber",
      "routeChange.key.changeSetId"
    ),
    Index(
      "change-nodes",
      "time",
      "nodeChange.key.time"
    ),
    Index(
      "change-nodes",
      "impact",
      "nodeChange.impact"
    ),
    Index(
      "change-nodes",
      "changeSetId",
      "nodeChange.key.replicationNumber",
      "nodeChange.key.changeSetId"
    ),
    Index(
      "change-summaries",
      "impact-time",
      "changeSetSummary.impact",
      "changeSetSummary.key.time.year",
      "changeSetSummary.key.time.month",
      "changeSetSummary.key.time.day"
    ),
    Index(
      "change-summaries",
      "changeSetId",
      "changeSetSummary.key.replicationNumber",
      "changeSetSummary.key.changeSetId"
    ),
    Index(
      "change-location-summaries",
      "time",
      "locationChangeSetSummary.key.time"
    ),
    Index(
      "change-location-summaries",
      "impact",
      "locationChangeSetSummary.impact"
    ),
    Index(
      "change-location-summaries",
      "changeSetId",
      "locationChangeSetSummary.key.replicationNumber",
      "locationChangeSetSummary.key.changeSetId"
    )
  )

  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    val database = mongoClient.getDatabase("tryout")
    val tool = new CreateIndexesTool(database)
    tool.createIndexes(indexes)
    mongoClient.close()
  }
}

class CreateIndexesTool(database: MongoDatabase) {

  private val log = Log(classOf[CreateIndexesTool])

  def createIndexes(indexes: Seq[Index]): Unit = {
    indexes.foreach(createIndex)
  }

  private def createIndex(index: Index): Unit = {
    log.elapsedSeconds {
      val collection = database.getCollection(index.collectionName)
      val future = collection.createIndex(index.index, IndexOptions().name(index.indexName)).toFuture()
      Await.result(future, Duration(25, TimeUnit.MINUTES))
      (s"Index ${index.collectionName}/${index.indexName} created", ())
    }
  }
}
