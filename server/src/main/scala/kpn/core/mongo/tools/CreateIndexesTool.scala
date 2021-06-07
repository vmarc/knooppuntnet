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
      val index = Indexes.ascending(fieldNames: _*)
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

  private val newIndexes = Seq(
    Index(
      "route-changes",
      "routeId-time-impact",
      Indexes.descending(
        "routeChange.key.elementId",
        "routeChange.key.time.year",
        "routeChange.key.time.month",
        "routeChange.key.time.day",
        "routeChange.impact"
      )
    ),
    Index(
      "node-changes",
      "nodeId-time-impact",
      Indexes.descending(
        "nodeChange.key.elementId",
        "nodeChange.key.time.year",
        "nodeChange.key.time.month",
        "nodeChange.key.time.day",
        "nodeChange.impact"
      )
    )
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
      "network-changes",
      "time",
      "networkChange.key.time"
    ),
    Index(
      "network-changes",
      "impact",
      "networkChange.impact"
    ),
    Index(
      "network-changes",
      "changeSetId",
      "networkChange.key.replicationNumber",
      "networkChange.key.changeSetId"
    ),
    Index(
      "route-changes",
      "time",
      "routeChange.key.time"
    ),
    Index(
      "route-changes",
      "impact",
      "routeChange.impact"
    ),
    Index(
      "route-changes",
      "changeSetId",
      "routeChange.key.replicationNumber",
      "routeChange.key.changeSetId"
    ),
    Index(
      "node-changes",
      "time",
      "nodeChange.key.time"
    ),
    Index(
      "node-changes",
      "impact",
      "nodeChange.impact"
    ),
    Index(
      "node-changes",
      "changeSetId",
      "nodeChange.key.replicationNumber",
      "nodeChange.key.changeSetId"
    ),
    Index(
      "changeset-summaries",
      "impact-time",
      "changeSetSummary.impact",
      "changeSetSummary.key.time.year",
      "changeSetSummary.key.time.month",
      "changeSetSummary.key.time.day"
    ),
    Index(
      "changeset-summaries",
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
    ),
    Index( // This index will not be needed anymore if we only have queries based on time instead of timestamp
      "network-changes",
      "impact-timestamp",
      Indexes.compoundIndex(
        Indexes.ascending(
          "networkChange.networkId",
          "networkChange.impact",
        ),
        Indexes.descending(
          "networkChange.key.timestamp"
        )
      )
    ),
    Index(
      "network-changes",
      "impact-time",
      Indexes.compoundIndex(
        Indexes.ascending(
          "networkChange.networkId",
          "networkChange.impact",
        ),
        Indexes.descending(
          "networkChange.key.time"
        )
      )
    ),
    Index(
      "network-changes",
      "networkId-time-impact",
      Indexes.descending(
        "networkChange.networkId",
        "networkChange.key.time.year",
        "networkChange.key.time.month",
        "networkChange.key.time.day",
        "networkChange.impact"
      )
    )
  )

  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    val database = mongoClient.getDatabase("tryout")
    val tool = new CreateIndexesTool(database)
    tool.createIndexes(newIndexes)
    mongoClient.close()
  }
}

class CreateIndexesTool(database: MongoDatabase) {

  private val log = Log(classOf[CreateIndexesTool])

  def createIndexes(indexes: Seq[Index]): Unit = {
    log.info(s"Create ${indexes.size} indexes")
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
