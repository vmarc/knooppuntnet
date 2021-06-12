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

  private val indexes = Seq(
    Index(
      "networks",
      "network-name",
      "active",
      "attributes.name"
    ),
    Index(
      "networks",
      "network-node-references",
      "active",
      "nodeRefs"
    ),
    Index(
      "nodes",
      "location",
      "active",
      "location.names"
    ),
    Index(
      "nodes",
      "node-active",
      "active"
    ),
    Index(
      "routes",
      "route-node-references",
      "active",
      "nodeRefs"
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
      "key.time"
    ),
    Index(
      "network-changes",
      "impact",
      "impact"
    ),
    Index(
      "network-changes",
      "changeSetId",
      "key.replicationNumber",
      "key.changeSetId"
    ),
    Index( // This index will not be needed anymore if we only have queries based on time instead of timestamp
      "network-changes",
      "impact-timestamp",
      Indexes.compoundIndex(
        Indexes.ascending(
          "networkId",
          "impact",
        ),
        Indexes.descending(
          "key.timestamp"
        )
      )
    ),
    Index(
      "network-changes",
      "impact-time",
      Indexes.compoundIndex(
        Indexes.ascending(
          "networkId",
          "impact",
        ),
        Indexes.descending(
          "key.time"
        )
      )
    ),
    Index(
      "network-changes",
      "networkId-time-impact",
      Indexes.descending(
        "networkId",
        "key.time.year",
        "key.time.month",
        "key.time.day",
        "impact"
      )
    ),

    Index(
      "route-changes",
      "time",
      "key.time"
    ),
    Index(
      "route-changes",
      "impact",
      "impact"
    ),
    Index(
      "route-changes",
      "changeSetId",
      "key.replicationNumber",
      "key.changeSetId"
    ),
    Index(
      "node-changes",
      "time",
      "key.time"
    ),
    Index(
      "node-changes",
      "impact",
      "impact"
    ),
    Index(
      "node-changes",
      "changeSetId",
      "key.replicationNumber",
      "key.changeSetId"
    ),
    Index(
      "changeset-summaries",
      "impact-time",
      "impact",
      "key.time.year",
      "key.time.month",
      "key.time.day"
    ),
    Index(
      "changeset-summaries",
      "changeSetId",
      "key.replicationNumber",
      "key.changeSetId"
    ),
    Index(
      "change-location-summaries",
      "time",
      "key.time"
    ),
    Index(
      "change-location-summaries",
      "impact",
      "impact"
    ),
    Index(
      "change-location-summaries",
      "changeSetId",
      "key.replicationNumber",
      "key.changeSetId"
    ),
    Index(
      "route-changes",
      "routeId-time-impact",
      Indexes.descending(
        "key.elementId",
        "key.time.year",
        "key.time.month",
        "key.time.day",
        "impact"
      )
    ),
    Index(
      "node-changes",
      "nodeId-time-impact",
      Indexes.descending(
        "key.elementId",
        "key.time.year",
        "key.time.month",
        "key.time.day",
        "impact"
      )
    )
  )

  def main(args: Array[String]): Unit = {
    val mongoClient = Mongo.client
    val database = Mongo.database(mongoClient, "kpn-test")
    new CreateIndexesTool(database).createIndexes(indexes)
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
    Log.context(s"collection: ${index.collectionName}, index: ${index.indexName}") {
      if (hasIndex(index)) {
        log.info("Index already exists")
      }
      else {
        log.elapsedSeconds {
          val collection = database.getCollection(index.collectionName)
          val future = collection.createIndex(index.index, IndexOptions().name(index.indexName)).toFuture()
          Await.result(future, Duration(25, TimeUnit.MINUTES))
          ("Created", ())
        }
      }
    }
  }

  private def hasIndex(index: Index): Boolean = {
    val collection = database.getCollection(index.collectionName)
    val future = collection.listIndexes[MongoIndexDefinition]().toFuture()
    val indexDefinitions = Await.result(future, Duration(25, TimeUnit.MINUTES))
    indexDefinitions.exists(_.name == index.indexName)
  }
}
